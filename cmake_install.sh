#!/bin/sh

# Cmake Install script from source on OSX 10.12.6
# Date: 1.0 15.Sep.2017
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

mkdir $BUILD_DIR
if [ $? -ne 0 ]; then
   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
fi
cd $BUILD_DIR 

# pull source
echo "fetch source cmake"
git clone https://gitlab.kitware.com/cmake/cmake.git && cd cmake

# build && [ $? -eq 0 ] && INSTALL_ERROR=0
./bootstrap && make && sudo make install
cd ../../ && rm -fr $BUILD_DIR && echo "cmake install complete !!!"
