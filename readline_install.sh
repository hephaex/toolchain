#!/bin/sh

# Install readline Mac OS X.
# Maintain by Mario Cho <hephaex@gmail.com>

# or wherever you'd like to build
BUILD_DIR=obj

echo && \
    echo "check latest version of readline-${READLINE_VERSION}" && \
    READLINE=7.0 && \
    mkdir -p $BUILD_DIR && \
    cd $BUILD_DIR

echo "Installing readline-${READLINE}" && \
    curl -fL http://git.savannah.gnu.org/cgit/readline.git/snapshot/readline-${READLINE}.tar.gz | tar xzf -

echo "build readline-${READLINE}" && \
    cd readline-${READLINE} && \
    ./configure --prefix=/usr/local && \
    make && \
    sudo make install

echo "Installation complete." && \
    cd .. && \
    rm -fr $BUILD_DIR

