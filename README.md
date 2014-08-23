toolchain
=========

OSX opensource toolchain

## prerequirement
1. xcode install
 - xcode compiler (cli base)
 - xcode-select --install
2. download source code
 - gcc requires three other libraries: GMP, MPFR, MPC
 - gcc-4.9.1.tar.bz2 86MB [gcc](http://www.netgull.com/gcc/releases/gcc-4.9.1/gcc-4.9.1.tar.bz2)
 - gmp-6.0.0a.tar.bz2 2.2MB [gmp](https://ftp.gnu.org/gnu/gmp/gmp-6.0.0a.tar.bz2)
 - mpc-1.0.2.tar.gz 0.6MB [mpc](ftp://ftp.gnu.org/gnu/mpc/mpc-1.0.2.tar.gz)
 - mpfr-3.1.2.tar.bz2 1.2MB [mpfp](http://mpfr.loria.fr/mpfr-current/mpfr-3.1.2.tar.bz2)

## set up the source tree
1. decompress gcc-4.9.1
 - tar -xjvf gcc-4.9.1.tar.bz2
 - cd gcc-4.9.1
2. decompress gmp
 - tar -xjvf ../gmp-6.0.0a.tar.bz2
 - ln -s gmp-6.0.0 gmp
3. decompress mpc
 - tar -xzf ../mpc-1.0.2.tar
 - ln -s mpc-1.0.2/ mpc
4. decompress mpfr
 - tar -xjf ../mpfr-3.1.2.tar.bz2
 - ln -s mpfr-3.1.2 mpfr

## compile enviroment setting
- mkdir obj
- cd obj
- pwd
 - 00_toolchain/gcc_4.9.1/obj
- ../configure --prefix=/usr/local/gcc-4.9.1 --enable-languages=c,c++

## build up
- make -j4
- sudo make install

## enviroment setting
- check version
 - /usr/local/bin/gcc --version
 - export PATH=/usr/local/bin:$PATH
 
# cmake
- download source code
 - [cmake 3.0.0](http://www.cmake.org/files/v3.0/cmake-3.0.0.tar.gz)
 - mv ~/Downloads/cmake-3.0.0.tar .
 - tar xvf cmake-3.0.0.tar
 - cd cmake-3.0.0
 - mkdir obj
 - cd obj
 - ../bootstrap
 - make -j4
 - sudo make install
 
# wget
- download source code
 - [wget 1.15](http://ftp.gnu.org/gnu/wget/wget-1.15.tar.gz)
 - mv ~/Downloads/wget-1.15.tar .
 - tar xvf wget-1.15.tar
 - cd wget-1.15
 - mkdir obj
 - cd obj
 - ../configure --with-ssl=openssl
 - make -j4
 - sudo make install
 
