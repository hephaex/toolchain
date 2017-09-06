#!/bin/sh

# wherever you'd like to build
BUILD_DIR=obj
# you'd like to build version of emacs
#EMACS=25.2
EMACS=$(curl -fL http://ftpmirror.gnu.org/emacs | grep -E "emacs.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $4}' | sed 's/emacs-//g' |sed 's/lisp//g' | sed 's/.tar.gz//g' | sort | tail -n 1)

mkdir -p $BUILD_DIR
cd $BUILD_DIR

echo "build emacs version is ${EMACS}"

# pull emacs source archive
curl -LO http://ftpmirror.gnu.org/emacs/emacs-$EMACS.tar.xz
# pull emacs patch file which 2byte code itterations on terminal env.
#curl -LO https://gist.githubusercontent.com/takaxp/3314a153f6d02d82ef1833638d338ecf/raw/156aaa50dc028ebb731521abaf423e751fd080de/emacs-25.2-inline.patch

# defile emacs source archive

tar zxvf emacs-$EMACS.tar.xz
cd ./emacs-$EMACS
#patch -p1 < ../emacs-$EMACS-inline.patch
#sleep 5
#./autogen.sh
./configure --without-x --with-ns --with-modules

# build
echo "build emacs-${EMACS}"
make bootstrap -j4

# install 
echo "Installing emacs-${EMACS}"
make install -j4
#cd ./nextstep
#open .
