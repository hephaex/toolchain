#!/bin/sh
# Emacs build script for OSX 10.13.5
# Maintain by Mario Cho <hephaex@gmail.com>

# git log test 
# wherever you'd like to build
BUILD_DIR=obj

# you'd like to build version of emacs
#EMACS=26.1.x
EMACS=$(curl -fL http://ftpmirror.gnu.org/emacs | grep -E "emacs.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $5}' | sed 's/>emacs-//g' |sed 's/lisp//g' | sed 's/.tar.gz//g' | sed 's; .*$;;'| sed 's/............$//' |  sort | tail -n 1)

mkdir -p $BUILD_DIR && cd $BUILD_DIR

# pull emacs source archive
echo "*** fetch emacs-${EMACS}"
curl -fL http://ftpmirror.gnu.org/emacs/emacs-$EMACS.tar.gz | tar zxf -
cd emacs-$EMACS

echo "*** patch emacs-${EMACS}"
# pull emacs patch file which no title bar & unflicker on terminal env.
curl -LO https://github.com/hephaex/toolchain/blob/master/emacs-26.x.patch/emacs-26.x-inline-googleime.patch
curl -LO https://github.com/hephaex/toolchain/blob/master/emacs-26.x.patch/ns-private.patch
patch -p1 < ./emacs-26.x-inline-googleime.patch
patch -p1 < ./ns-private.patch
sleep 5

# configure Makefile
./autogen.sh
<<<<<<< HEAD
#./configure --without-x --with-ns --with-modules
./configure CC=clang --without-x --with-gnutls=yes --with-ns --with-modules --with-imagemagick
=======
./configure CC=clang --without-x --with-gnutls=no --with-ns --with-modules --with-imagemagick
>>>>>>> 24a184d8474fa3509803d7d0c5abccc484981cc9

# build
echo "build emacs-${EMACS}"
make bootstrap -j4

# install
echo "Installing emacs-${EMACS}"
make install -j4

# ckeck error & link
[ $? -eq 0 ] && EMACS_ERROR=0 && cp -R ./nextstep/Emacs.app /Applications/.
[ $EMACS_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "\n*** emacs install complete !!!"
alias emacs="/Applications/Emacs.app/Contents/MacOS/Emacs -nw"
