#!/bin/sh

# Install autotools, including autoconf, automake and libtool on Mac OS X.

# or wherever you'd like to build
BUILD_DIR=obj

# Get the latest version of automake and libtool because they have no latest symbol link.
AUTOMAKE_VERSION=$(curl -fL http://ftpmirror.gnu.org/automake | grep -E "automake.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $2}' | sed 's/automake-//g' | sed 's/.tar.gz//g' | sort | tail -n 1)
LIBTOOL_VERSION=$(curl -fL http://ftpmirror.gnu.org/libtool | grep -E "libtool.*tar.gz" | grep -vE "sig|asc" | awk -F \" '{print $2}' | sed 's/libtool-//g' | sed 's/.tar.gz//g' | sort | tail -n 1)

mkdir -p $BUILD_DIR
cd $BUILD_DIR

# Autoconf
echo "Installing the latest autoconf..."
curl -fL http://ftpmirror.gnu.org/autoconf/autoconf-latest.tar.gz | tar xzf -
cd autoconf-*
./configure --prefix=/usr/local
make && sudo make install
[ $? -eq 0 ] && AUTOCONF_ERROR=0 && cd .. && rm -rf autoconf-*

# Automake
echo "Installing automake-${AUTOMAKE_VERSION}..."
curl -fL http://ftpmirror.gnu.org/automake/automake-${AUTOMAKE_VERSION}.tar.gz | tar xzf -
cd automake-*
./configure --prefix=/usr/local
make && sudo make install
[ $? -eq 0 ] && AUTOMAKE_ERROR=0 && cd .. && rm -rf automake-*

# Libtool
echo "Installing libtool-${LIBTOOL_VERSION}"
curl -fL http://ftpmirror.gnu.org/libtool/libtool-${LIBTOOL_VERSION}.tar.gz | tar xzf -
cd libtool-*
./configure --prefix=/usr/local
make && sudo make install
[ $? -eq 0 ] && LIBTOOL_ERROR=0 && cd .. && rm -rf libtool-*

if [ $AUTOCONF_ERROR -ne 0 ]; then
  echo "Autoconf error."
fi

if [ $AUTOMAKE_ERROR -ne 0 ]; then
  echo "Automake error."
fi

if [ $LIBTOOL_ERROR -ne 0 ]; then
  echo "Libtool error."
fi

function add_path () {
    [[ ":${PATH}:" =~ .*:$1:.* ]] || PATH="${PATH:+${PATH}:}$1"
}

[ $AUTOCONF_ERROR -eq 0 ] && [ $AUTOMAKE_ERROR -eq 0 ] && [ $LIBTOOL_ERROR -eq 0 ] && echo "Installation complete." && cd .. && cp rmdir $BUILD_DIR

