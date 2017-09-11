#!/bin/sh

# Ruby build script for OSX 10.12.6
# Date: 1.0 08.Sep.2017
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

# you'd like to build version of ruby
#RUBY=2.4.1
RUBY=$(curl -fL https://ftp.ruby-lang.org/pub/ruby/ |grep -E "ruby.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $2}' |sed 's/ruby-//g' |sed 's/.tar.gz//g' |sort |tail -n 1)

mkdir -p $BUILD_DIR
echo 'make dir {$BUILD_DIR}' && rm -fr $BUILD_DIR && mkdir $BUILD_DIR && cd $BUILD_DIR 


# pull ruby source archive
echo "fetch ruby-${RUBY}"
curl -fL https://ftp.ruby-lang.org/pub/ruby/ruby-$RUBY.tar.gz | tar zxf -

cd ./ruby-$RUBY
# configure Makefile
./configure --with-openssl-dir=/usr/local/ssl

# build
echo "build ruby-${RUBY}"
make -j4

# install 
echo "Installing ruby-${RUBY}"
sudo make install -j4
[ $? -eq 0 ] && INSTALL_ERROR=0 && ruby --version
[ $INSTALL_ERROR -eq 0 ] && cd ../../ rm -fr $BUILD_DIR && echo "ruby install complete !!!"

