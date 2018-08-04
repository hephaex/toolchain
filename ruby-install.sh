#!/bin/sh

# Ruby build script for OSX 10.12.6
# Date: 03.Aug.2018
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

# you'd like to build version of ruby
RUBY=2.5.1
#RUBY=$(curl -fL https://ftp.ruby-lang.org/pub/ruby/ |grep -E "ruby.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $2}' |sed 's/ruby-//g' |sed 's/.tar.gz//g' |sort |tail -n 1)


echo 'make dir ${BUILD_DIR}' && \
    mkdir -p $BUILD_DIR && \
    rm -fr $BUILD_DIR && \
    mkdir $BUILD_DIR && \
    cd $BUILD_DIR 

# pull ruby source archive
echo "fetch ruby-${RUBY}" && \
    curl -fL https://ftp.ruby-lang.org/pub/ruby/ruby-$RUBY.tar.gz | tar zxf -

# configure Makefile
cd ./ruby-$RUBY && \
    ./configure --prefix=/usr/local \
		--with-openssl-dir=/usr/local/share/openssl \
		--with-readline-dir=/usr/local/share/readline \
		LDFLAGS="-L/usr/local/share/openssl/lib -L/usr/local/lib" \
		CPPFLAGS="-I/usr/local/share/openssl/include -I/usr/local/share/include/readline"

# build
echo "build ruby-${RUBY}" && \
    make -j4

# install 
echo "Installing ruby-${RUBY}" && \
    make install -j4
[ $? -eq 0 ] && INSTALL_ERROR=0 && ruby --version
[ $INSTALL_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "ruby install complete !!!"

