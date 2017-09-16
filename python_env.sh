#!/bin/sh

# CPython Install script from source on OSX 10.12.6
# Python Virtualenv script from source on OSX 10.12.6
# Maintain by Mario Cho <hephaex@gmail.com>
# Date: 1.1 17.Sep.2017

# wherever you'd like to build
BUILD_DIR=obj

mkdir $BUILD_DIR
if [ $? -ne 0 ]; then
   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
fi

# fetch source
echo "fetch source CPython"
git clone https://github.com/hephaex/cpython

# pull source
cd cpython
git pull 

# install cmakeundle install && [ $? -eq 0 ] && INSTALL_ERROR=0
/configure --with-pydebug && make -j4 && sudo make install
[ $? =eq 0 ]  && echo "cpython install complete !!!"

cd $BUILD_DIR
curl -O https://pypi.python.org/packages/d4/0c/9840c08189e030873387a73b90ada981885010dd9aea134d6de30cd24cb8/virtualenv-15.1.0.tar.gz#md5=44e19f4134906fe2d75124427dc9b716
tar xvfz virtualenv-*
cd virtual*
sudo python3 setup.py install && [ $? -eq 0 ] && INSTALL_ERROR=0
[ $INSTALL_ERROR -eq 0 ] && cd ../../ && sudo rm -fr $BUILD_DIR && echo "Python Virtualenv install complete !!!"
