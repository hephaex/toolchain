#!/bin/sh
# Emacs build script for OSX 10.13.5
# Maintain by Mario Cho <hephaex@gmail.com>

# wherever you'd like to build
BUILD_DIR=obj

# you'd like to build version of emacs
#EMACS=26.1.x

echo "*** fetch emacs  lastest version" && \
    EMACS=$(curl -fL http://ftpmirror.gnu.org/emacs | grep -E "emacs.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $4}' | sed 's/>emacs-//g' |sed 's/lisp//g' | sed 's/.tar.gz//g' | sed 's; .*$;;'| sed 's/............$//' |  sort | tail -n 1) && \
    echo "*** Lastest version of emacs-${EMACS}"

echo "*** prepare ${BUILD_DIR}" && \
    mkdir -p $BUILD_DIR && cd $BUILD_DIR

# pull emacs source archive
echo "*** fetch emacs-${EMACS}" && \
    curl -fL http://ftpmirror.gnu.org/emacs/emacs-$EMACS.tar.gz | tar zxf - 

# pull emacs patch file which no title bar & unflicker on terminal env.
echo "*** patch emacs-${EMACS}" && \
    cd emacs-$EMACS && \    
    curl -LO https://github.com/hephaex/toolchain/blob/master/emacs-26.x.patch/emacs-26.x-inline-googleime.patch && \
    curl -LO https://github.com/hephaex/toolchain/blob/master/emacs-26.x.patch/ns-private.patch && \
    patch -p1 < ./emacs-26.x-inline-googleime.patch && \
    patch -p1 < ./ns-private.patch && \
    sleep 5

# configure Makefile
# osx: clang, linux: gcc    
echo "*** configuration ***" && \
    ./autogen.sh && \
    ./configure CC=clang --without-x --with-ns 

# build
echo "*** build emacs-${EMACS} ***" && \
    make bootstrap -j6

# install
echo "*** Installing emacs-${EMACS} ***" && \
    make install -j6

# ckeck error & link
[ $? -eq 0 ] && EMACS_ERROR=0 && cp -R ./nextstep/Emacs.app /Applications/.
[ $EMACS_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "\n*** emacs install complete !!!"
alias emacs="/Applications/Emacs.app/Contents/MacOS/Emacs -nw"
