#!/bin/sh
# Emacs build script for OSX 10.12.6
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

# you'd like to build version of emacs
#EMACS=26.1.x
EMACS=$(curl -fL http://ftpmirror.gnu.org/emacs | grep -E "emacs.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $5}' | sed 's/>emacs-//g' |sed 's/lisp//g' | sed 's/.tar.gz//g' | sed 's; .*$;;'| sed 's/............$//' |  sort | tail -n 1)

mkdir -p $BUILD_DIR
cd $BUILD_DIR

# pull emacs source archive
echo "fetch emacs-${EMACS}"
curl -fL http://ftpmirror.gnu.org/emacs/emacs-$EMACS.tar.gz | tar zxf -
# pull emacs patch file which no title bar & unflicker on terminal env.
curl -LO https://github.com/hephaex/toolchain/blob/master/emacs-26.x.patch/emacs-20170905-inline.patch.gz

# configure Makefile
./autogen.sh
./configure --without-x --with-ns --with-modules

# build
echo "build emacs-${EMACS}"
make bootstrap -j4

# install
echo "Installing emacs-${EMACS}"
make install -j4
[ $? -eq 0 ] && EMACS_ERROR=0 && cp -R ./nextstep/Emacs.app /Applications/.
[ $EMACS_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "emacs install complete !!!"
alias emacs="/Applications/Emacs.app/Contents/MacOS/Emacs -nw"
