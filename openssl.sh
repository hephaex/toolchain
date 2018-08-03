#!/bin/sh

# OpenSSL build script for OSX 10.12.6 
# Date: 1.1 27.May.2018
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

mkdir $BUILD_DIR
if [ $? -ne 0 ]; then
   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
fi
cd $BUILD_DIR 

# you'd like to build version of emacs
#OpenSSL=1.1.0h
OPENSSL=$(curl -fL https://www.openssl.org/source/ | grep -E "openssl.*tar.gz" |grep -vE "fips" | awk -F \" '{print $2}' | sed 's/openssl-//g' |sed 's/.tar.gz//g' |tail -n 1)

# pull emacs source archive
echo "fetch OpenSSL-${OPENSSL}" && \
    curl -fL https://www.openssl.org/source/openssl-$OPENSSL.tar.gz | tar zxf -

cd openssl-$OPENSSL

# configure Makefile
sudo ./config --prefix=/usr/local/openssl-$OPENSSL

# build
echo "build openssl-${OPENSSL}" && \
    make -j4

# install 
echo "Installing openssl-${OPENSSL}" && \
    sudo make install -j4

echo "System Env. Configuration openssl-${OPENSSL}" && \
    sudo rm -f /usr/bin/openssl && \
    cd ../../ && \
    sudo rm -fr $BUILD_DIR && \
    sudo ln -s /usr/local/openssl-$OPENSSL/bin/openssl /usr/bin/openssl | echo "openssl install complete !!!"
