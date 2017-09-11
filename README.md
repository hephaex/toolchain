Toolchain
=========

OSX opensource toolchain

## prerequirement
1. xcode install
 - xcode compiler (cli base)
 - xcode-select --install

## autoconf & automake & libtool & texinfo
- ./autoconf_install.sh

## emacs 25.2.1 no-x-windows 
- ./osx_emacs25-nox.sh 
- alias emacs="/usr/local/bin/emacs -nw"

## openssl-1.0.1i
- ./openssl.sh

## ruby
- ./ruby.sh

## vagrant
- vagrant binary install from web.
 - wget https://dl.bintray.com/mitchellh/vagrant/vagrant_1.6.5.dmg
- virtualbox binary install from web.
 - wget http://download.virtualbox.org/virtualbox/4.3.16/VirtualBox-4.3.16-95972-OSX.dmg
- vagrant init centos-6-x64 http://puppet-vagrant-boxes.puppetlabs.com/centos-64-x64-vbox4210.box
- vagrant up
 - start virtual machine
- vagrant ssh
 - connect virtual machine
- vagrant halt
 - vagrant service stop
 


2. download source code
 - gcc required three other libraries: GMP, MPFR, MPC, isl
 - gcc-6.2.0.tar.bz2 99MB [gcc-6.2.0.bz2](http://ftp.tsukuba.wide.ad.jp/software/gcc/releases/gcc-6.2.0/gcc-6.2.0.tar.bz2)
 - gmp-6.1.1.tar.bz2 2.3MB [gmp-6.1.1.tar.bz2](https://ftp.gnu.org/gnu/gmp/gmp-6.1.1.tar.bz2)
 - mpc-1.0.3.tar.gz 0.6MB [mpc-1.0.3.tar.gz](http://fossies.org/linux/misc/mpc-1.0.3.tar.gz)
 - mpfr-3.1.5.tar.bz2 1.2MB [mpfr-3.1.5.tar.bz2](http://www.mpfr.org/mpfr-current/mpfr-3.1.5.tar.bz2)
 - isl-0.17.1.tar.bz2 1.6MB [isl-0.17.1.tar.bz2](http://isl.gforge.inria.fr/isl-0.17.1.tar.bz2)


## gmp-6.1.1

* Prepare GMP configurations

```
# mkdir build && cd build
# ../configure --prefix=/usr/local \
               --enable-cxx \
			   --disable-static \
			   --docdir=/usr/local/share/doc/gmp-6.1.1
```

* the meaning of configuration options

> --enable-cxx
>> This parameter enables C++ support

> --docdir=/usr/share/doc/gmp-6.1.1
>> This variable specifies the correct place for the documentation.

* build

```
# make
# make html
```

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
 

## git
- curl "https://www.kernel.org/pub/software/scm/git/git-2.0.4.tar.gz"
- mv ~/Downloads/git-2.0.4.tar .
- tar xvf git-2.0.4.tar
- cd git-2.0.4
- make configure
- make -j4
- sudo make install
- sudo rm /usr/bin/git

## cscope
- source code [cscope-15.8a](http://downloads.sourceforge.net/project/cscope/cscope/15.8a/cscope-15.8a.tar.gz?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fcscope%2Ffiles%2F&ts=1407129629&use_mirror=jaist)
- mv ~/Download/cscope-15.8a.tar .
- tar xvf cscope-15.8a.tar
- cd cscope-15.8a
- emacs src/constants.h

```c
#if (BSD || V9 ) && !__NetBSD__ && !__FreeBSD__
```


```c
#if (BSD || V9 ) && !__NetBSD__ && !__FreeBSD__ && !__APPLE__
```
- ./configure
- make -j4
- sudo make install


## doxygen
- source code [doxygen-1.8.7](http://ftp.stack.nl/pub/users/dimitri/doxygen-$
- tar xvf doxygen-1.8.7
- cd doxygen-1.8.7
- configure
- make -j4
- sudo make install 

## graphviz
- source down [graphviz-2.38.0](http://www.graphviz.org/pub/graphviz/stable/$
- tar xvf graphviz-2.38.0.tar
- cd graphviz-2.38.0
- ./configure
- make -j4
- sudo make install 

## node.js
- source [node.js](http://nodejs.org/dist/v0.10.31/node-v0.10.31.tar.gz)
- tar -zxvf node-v0.10.31.tar.gz
- cd node-v0.10.31
- ./configure
- make
- sudo make install

## gradle
- wget https://services.gradle.org/distributions/gradle-2.1-src.zip
- Move downloaded package to a directory of your choice :
- mv ~/Downloads/gradle-2.1  ~/toolchain/
- Edit your profile file ( vi ~/.profile)
- brew install gradle
```
#add Java Home to path
export JAVA_HOME=$(/usr/libexec/java_home)
export GRADLE_HOME="/Users/{yourAccountName}/toolchain/gradle-2.1"
export PATH=${PATH}:${GRADLE_HOME}/bin
```


## readline6.4
- [readline-6.3](ftp://ftp.cwru.edu/pub/bash/readline-6.3.tar.gz)
- tar xvf readline-6.3.tar.gz
- cd readline-6.3
- ./configure
- make
- sudo make install

## mdwiki
- [mdwiki](https://github.com/Dynalon/mdwiki)
- git clone https://github.com/Dynalon/mdwiki
- cd mdwiki
- Install node.js >= 0.10 and npm (if not included)
- Install deps and build MDwiki (you need automake installed - if you are on Windows check the contents of the Makefile for the list of commands to run them manually):
```
make
```
- Find the mdwiki.html in the dist/ folder
- Development

- For development, use
```
grunt devel
```
- To get unminified source code compiled to dist/mdwiki-debug.html, as well as auto file watching and livereload support. Symlink the development mdwiki file into your webroot for testing.


## zlib-1.2.8
- [source](http://zlib.net/zlib-1.2.8.tar.gz)
- tar xvf zlib-1.2.8.tar.gz
- cd zlib-1.2.8
- ./configure
- make
- sudo make install

## openssh-6.6.p1
- [source](http://ftp.jaist.ac.jp/pub/OpenBSD/OpenSSH/portable/openssh-6.6p1.tar.gz)
- tar xvf openssh-6.6.p1.tar.gz
- cd openssh-6.6.p1
- ./configure
- make
- sudo make install

