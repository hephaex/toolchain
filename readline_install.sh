#!/bin/sh

# Install readline Mac OS X.
# Maintain by Mario Cho <hephaex@gmail.com>

# or wherever you'd like to build
BUILD_DIR=obj

# Get the latest version of automake and libtool because they have no latest symbol link.
echo && echo "check latest version of readline-${READLINE_VERSION}"
READLINE_VERSION=$(curl -fL http://ftpmirror.gnu.org/texinfo | grep -E "texinfo.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $4}' | sed 's/texinfo-//g' | sed 's/.tar.gz//g' | sort | tail -n 1) 


mkdir -p $BUILD_DIR && cd $BUILD_DIR


echo "Installing automake-${AUTOMAKE_VERSION}..."
curl -fL http://ftpmirror.gnu.org/automake/automake-${AUTOMAKE_VERSION}.tar.gz | tar xzf -
cd automake-*
./configure --prefix=/usr/local
make && sudo make install
[ $? -eq 0 ] && AUTOMAKE_ERROR=0 && cd .. && rm -rf automake-*

[ $TEXINFO_ERROR -eq 0 ] && echo "Installation complete." && cd .. && rm -fr $BUILD_DIR

