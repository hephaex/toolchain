#!/bin/sh

# GoLang build script for OSX 10.12.6 
# Date: 1.0 19.Sep.2017
# Maintain by Mario Cho <hephaex@gmail.com>

export GOROOT_BOOTSTRAP=/usr/local/src/go1.4

# wherever you'd like to build
#BUILD_DIR=${pwd}/obj

#mkdir $BUILD_DIR
#if [ $? -ne 0 ]; then
#   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
#fi
#cd $BUILD_DIR 


# go bootstrap go1.4
curl -fL https://storage.googleapis.com/golang/go1.4-bootstrap-20170531.tar.gz |tar zxvf -
mv go ${GOROOT_BOOTSTRAP}
cd ${GOROOT_BOOTSTRAP}/src
./make.bash


# you'd like to build version of emacs
GO=1.9

echo "fetch Go-${GO}"
git clone https://go.googlesource.com/go $GOROOT_BOOTSTRAP/../go${GO}
cd $GOROOT_BOOTSTRAP/../go${GO}
git checkout go${GO}

# build
echo "build go-${GO}"
cd src
./all.bash $GOROOT_BOOTSTRAP

#sudo ln -sf /usr/local/src/go1.9/bin/go /usr/local/bin/go
ln -sf ${GOROOT_BOOTSTRAP}/../go${go}/bin/go /usr/local/bin/go
echo "go install complete !!!"
