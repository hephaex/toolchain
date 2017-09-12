#!/bin/sh

# Vagrant Install script from source on OSX 10.12.6
# Date: 1.0 11.Sep.2017
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

mkdir $BUILD_DIR
if [ $? -ne 0 ]; then
   echo "delete previous ${BUILD_DIR}" && rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
fi
cd $BUILD_DIR 

bundle --version
if [ $? -ne 0 ] ; then
    sudo gem update --system
    sudo gem install bundler
fi

# pull source
echo "fetch source vagant"
git clone https://github.com/mitchellh/vagrant.git && cd vagrant

# install vagrant
bundle install && [ $? -eq 0 ] && INSTALL_ERROR=0
[ $INSTALL_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "vagrant install complete !!!"

