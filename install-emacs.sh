#!/bin/sh
# Emacs build script for OSX 10.14.x
# Maintain by Mario Cho <hephaex@gmail.com>

# require dependencies 
# brew update ; brew install autoconf

git clone git://git.sv.gnu.org/emacs.git
curl -LO https://gist.githubusercontent.com/takaxp/5294b6c52782d0be0b25342be62e4a77/raw/9c9325288ff03a50ee26e4e32c8ca57c0dd81ace/emacs-25.2-inline-googleime.patch

# pull emacs patch file which no title bar & unflicker on terminal env.
cd emacs
# git reset --hard 6217746dd6
git checkout -b emacs-26
git reset --hard 9ad0f1d15c
patch -p1 < ../emacs-25.2-inline-googleime.patch

sleep 5

# configure Makefile
# osx: clang, linux: gcc    
echo "*** configuration ***" && \
    ./autogen.sh && \
    ./configure CC=clang --without-x --with-ns 

# build
echo "*** build emacs-${EMACS} ***" && \
    make bootstrap -j4

# install
echo "*** Installing emacs-${EMACS} ***" && \
    make install -j4

# ckeck error & link
[ $? -eq 0 ] && EMACS_ERROR=0 && cp -R ./nextstep/Emacs.app /Applications/.
[ $EMACS_ERROR -eq 0 ] && cd ../../ && rm -fr $BUILD_DIR && echo "\n*** emacs install complete !!!"
alias emacs="/Applications/Emacs.app/Contents/MacOS/Emacs -nw"
