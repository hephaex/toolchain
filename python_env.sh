#!/bin/sh

# CPython Install script from source on OSX 10.12.6
# Python Virtualenv script from source on OSX 10.12.6
# Maintain by Mario Cho <hephaex@gmail.com>
# Date: 1.2 17.Sep.2017
#   1.1: cpython base
#   1.2: direnv + pyenv + virenv

# wherever you'd like to build
BUILD_DIR=obj

mkdir $BUILD_DIR
if [ $? -ne 0 ]; then
   echo "delete previous ${BUILD_DIR}" && sudo rm -fr $BUILD_DIR && mkdir -p $BUILD_DIR
fi

cd $BUILD_DIR

# direnv install
#git clone https://github.com/direnv/direnv /usr/local/src/direnv
#BUILD_DIR=${pwd}/obj
#cd /usr/local/src/direnv
#make install && [ $? -eq 0 ] && INSTALL_ERROR=0
#[ $INSTALL_ERROR -eq 0 ] && echo 'eval "$(direnv hook bash)"' >> ~/.bash_profile

# pyenv install
git clone https://github.com/pyenv/pyenv.git ~/.pyenv
git clone https://github.com/pyenv/pyenv-virtualenv.git ~/.pyenv/plugin/virtualenv
echo 'export PYENV_ROOT="$HOME/.pyenv"' >> ~/.bash_profile
echo 'export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.bash_profile
echo -e 'if command -v pyenv 1>/dev/null 2>&1; then\n  eval "$(pyenv init -)"\nfi' >> ~/.bash_profile
echo 'eval "$(pyenv virtualenv-init -)"' >> ~/.bash_profile
exec "$SHELL"

# virtualenv install
#cd $BUILD_DIR
##curl -O https://pypi.python.org/packages/d4/0c/9840c08189e030873387a73b90ada981885010dd9aea134d6de30cd24cb8/virtualenv-15.1.0.tar.gz
#tar xvfz virtualenv-*
#cd virtual*
#sudo python3 setup.py install && [ $? -eq 0 ] && INSTALL_ERROR=0
#[ $INSTALL_ERROR -eq 0 ] && cd ../../ && sudo rm -fr $BUILD_DIR && echo "Python env install complete !!!"

# fetch source
#echo "fetch source CPython"
#git clone https://github.com/hephaex/cpython

# pull source
#cd cpython
#git checkout 3.6
#git pull origin 3.6

# install cmakeundle install && [ $? -eq 0 ] && INSTALL_ERROR=0
#./configure --with-pydebug && make -j4 && sudo make install
#[ $? =eq 0 ]  && echo "cpython install complete !!!"

