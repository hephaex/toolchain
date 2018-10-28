#!/bin/bash
# build shell-script tensorflow
# The MIT License (MIT)

[ -f "$1" ] && { 
  source "$1" 
  } || { 
  echo "Use: $0 <config>"
  exit 1
}

DIR="$(realpath $(dirname $0))"
source "${DIR}/patch.sh"

# builtin variables
RED='\033[0;31m'
BLUE='\033[1;36m'
NC='\033[0m'
TF_PYTHON_VERSION=${TF_PYTHON_VERSION:-"3.6"}
TF_VERSION=${TF_VERSION:-"v1.12.0"}
BAZEL_VERSION=${BAZEL_VERSION:-"0.11.1"}
TF_GIT_URL=${TF_GIT_URL:-"https://github.com/tensorflow/tensorflow"}
WORKDIR=${WORKDIR:-"$DIR"}
BAZEL_BIN="$(command -v bazel)"

function log_failure_msg() {
	echo -ne "[${RED}ERROR${NC}] $@\n"
}

function log_app_msg() {
	echo -ne "[${BLUE}INFO${NC}] $@\n"
}

function create_tempdir() {
  WORKDIR=${WORKDIR}/sources/
  if [ ! -d $WORKDIR ]; then
    mkdir -p ${WORKDIR} || {
      log_failure_msg "error when creates workdir $WORKDIR"
      exit 1
    }
  fi
  return 0
}

function build_bazel() {
  mkdir -p ${WORKDIR}/bin/

  if [ -z "$BAZEL_BIN" ] || [ "$($BAZEL_BIN version | grep -i 'label' | awk '{ print $3 }' | tr -d '-')" != "${BAZEL_VERSION}" ]; then
      BAZEL_BIN="${WORKDIR}/bin/bazel-${BAZEL_VERSION}"
  fi

  PATH="${WORKDIR}/bin/:${PATH}"

  if [ -f "$BAZEL_BIN" ]; then
    log_app_msg "bazel already installed."
    ln -sf $BAZEL_BIN ${WORKDIR}/bin/bazel
    return 0
  fi

  cd $WORKDIR

  if [ ! -f bazel-${BAZEL_VERSION}-dist.zip ]; then
    wget --no-check-certificate https://github.com/bazelbuild/bazel/releases/download/${BAZEL_VERSION}/bazel-${BAZEL_VERSION}-dist.zip
  fi

  if [ ! -d bazel-${BAZEL_VERSION} ]; then
    mkdir bazel-${BAZEL_VERSION}
    unzip bazel-${BAZEL_VERSION}-dist.zip -d bazel-${BAZEL_VERSION}/
    rm -f bazel-${BAZEL_VERSION}-dist.zip
    cd bazel-${BAZEL_VERSION}/
    if [ "$BAZEL_PATCH" == "yes" ]; then
      bazel_patch || {
        log_failure_msg "error when apply patch"
        exit 1
      }
    fi
  else
    cd bazel-${BAZEL_VERSION}/
  fi

  ./compile.sh || {
    log_failure_msg "error when compile bazel"
    exit 1
  }

  chmod +x output/bazel
  mv output/bazel $BAZEL_BIN

  return 0
}

function toolchain() {
  [ "$CROSSTOOL_COMPILER" != "yes" ] && return 0

  CROSSTOOL_DIR="${WORKDIR}/toolchain/${CROSSTOOL_DIR}/"

  [ ! -d "${CROSSTOOL_DIR}/${CROSSTOOL_NAME}/bin/" ] && {
    mkdir -p ${WORKDIR}/toolchain/
    wget --no-check-certificate $CROSSTOOL_URL -O toolchain.tar.xz || {
      log_failure_msg "error when download crosstool"
      exit 1
    }
    tar xf toolchain.tar.xz -C ${WORKDIR}/toolchain/ || {
      log_failure_msg "error when extract crosstool"
      exit 1
    }
    rm toolchain.tar.xz &>/dev/null
  }
}

function download_tensorflow() {
  cd ${WORKDIR}
  if [ ! -d tensorflow ]; then
    git clone --recurse-submodules ${TF_GIT_URL} || return 1
    cd tensorflow/
  else
    cd tensorflow/
    $BAZEL_BIN clean &>/dev/null

    git reset --hard
    git clean -f -d
    git checkout master
    git branch -D __temp__
  fi

  [ "${TF_VERSION}" != "master" ] && {
    git checkout ${TF_VERSION} || {
      git pull
      # tries checkout again
      git checkout ${TF_VERSION} || {
        log_failure_msg "error when using tensorflow version ${TF_VERSION}"
        exit 1
      }
    }
  }

  git checkout -b __temp__

  git config user.email "hephaex@gmail.com"
  git config user.name "hephaex"

  if [ "$TF_PATCH" == "yes" ]; then
     tf_patch || {
       log_failure_msg "error when apply patch"
       exit 1
     }
  fi

  if [ ! -z "$CROSSTOOL_DIR" ] && [ ! -z "$CROSSTOOL_NAME" ]; then
    tf_toolchain_patch "$CROSSTOOL_NAME" "$CROSSTOOL_DIR" "$CROSSTOOL_EXTRA_INCLUDE" || {
      log_failure_msg "error when apply crosstool patch"
      exit 1
    }
  fi

  git add .
  git commit -m "temp modifications"

  return 0
}

function configure_tensorflow() {
  cd ${WORKDIR}/tensorflow
  $BAZEL_BIN clean
  export PYTHON_BIN_PATH=$(command -v python${TF_PYTHON_VERSION})
  export ${TF_BUILD_VARS}

  if [ "$TF_NEED_CUDA" == "1" ]; then
     local nvcc_path=$(command -v nvcc)

     if [ ! -z "$nvcc_path" ]; then
         local cuda_location=$(echo $nvcc_path | sed 's/\/bin\/nvcc//')
         local cuda_version=$(cat "${cuda_location}/version.txt" | awk '{ print $3 }' | cut -d'.' -f-2)
         local cudnn_version=$(readlink $(find "${cuda_location}/" -iname '*libcudnn.so') | cut -d'.' -f3)

         export CUDA_TOOLKIT_PATH="$cuda_location"
         export TF_CUDA_VERSION=$cuda_version
         export TF_CUDNN_VERSION=$cudnn_version
     else
         export TF_NEED_CUDA=0
     fi
  fi

  yes '' | ./configure || {
      log_failure_msg "error when configure tensorflow"
      exit 1
  }
  return 0
}

function build_tensorflow() {
  cd ${WORKDIR}/tensorflow

  if [ ! -z "$BAZEL_AVALIABLE_RAM" ] && [ ! -z "$BAZEL_AVALIABLE_CPU" ] && [ ! -z "$BAZEL_AVALIABLE_IO" ]; then
    BAZEL_LOCAL_RESOURCES="--local_resources ${BAZEL_AVALIABLE_RAM},${BAZEL_AVALIABLE_CPU},${BAZEL_AVALIABLE_IO}"
  fi

  $BAZEL_BIN build ${BAZEL_LOCAL_RESOURCES} -c opt ${BAZEL_COPT_FLAGS} --verbose_failures ${BAZEL_EXTRA_FLAGS} || return 1

  [[ "${BAZEL_EXTRA_FLAGS}" == *"build_pip_package"* ]] && {
    unset BDIST_OPTS
    if [ ! -z "$CROSSTOOL_DIR" ] && [ ! -z "$CROSSTOOL_NAME" ]; then
      export BDIST_OPTS="--universal"
    fi

    local output="/tmp/tensorflow_pkg"

    bazel-bin/tensorflow/tools/pip_package/build_pip_package $output || return 1

    if [ ! -z "$BDIST_OPTS" ]; then
      local f="${output}/$(ls $output | grep -i '.whl' | tail -n1)"
      local new_f="$(echo $f | sed -rn 's/tensorflow-([^-]+)-([^-]+)-.*/tensorflow-\1-\2-none-any.whl/p')"
      mv $f $new_f
      log_app_msg "wheel was renamed of $f for $new_f"
    fi
  }

  [[ "${BAZEL_EXTRA_FLAGS}" == *"libtensorflow.so"* ]] && {
    local output="/tmp/tensorflow_lib"

    # Clean and/or create output dir
    if [ -d $output ]; then
      rm -rf ${output} || {
        log_failure_msg "error when removes output dir $output"
        exit 1
      }
    fi
    mkdir -p ${output} || {
      log_failure_msg "error when creates output dir $output"
      exit 1
    }

    cp bazel-bin/tensorflow/libtensorflow.so $output
    cp tensorflow/c/c_api.h $output

    log_app_msg "Library files moved to $output"
  }

  log_app_msg "Done."
}

function main() {
    create_tempdir
    build_bazel
    toolchain
    download_tensorflow
    configure_tensorflow
    build_tensorflow
}

main
