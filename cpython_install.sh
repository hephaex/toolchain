#!/bin/sh

# CPython Install script from source on OSX 10.12.6
# Date: 1.0 15.Sep.2017
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
#BUILD_DIR=obj

#mkdir $BUILD_DIR
#if [ $? -ne 0 ]; then
#   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
#fi
#cd $BUILD_DIR 

# pull source
echo "fetch source CPython"
#git clone https://github.com/hephaex/cpython


cd cpython
git pull 

# install cmakeundle install && [ $? -eq 0 ] && INSTALL_ERROR=0
./configure --with-pydebug && make -j4
sudo make install && [ $? =eq 0 ] ; INSTALL_ERROR=0 
[ $INSTALL_ERROR -eq 0 ] ; cd ../../ && rm -fr $BUILD_DIR && echo "cpython install complete !!!"

