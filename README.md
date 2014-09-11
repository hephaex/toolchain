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
 
# autoconf
- curl -OL http://ftpmirror.gnu.org/autoconf/autoconf-2.69.tar.gz
- mv ~/Downloads/autoconf-2.69.tar .
- tar xvf autoconf-2.69.tar 
- cd autoconf-2.69
- mkdir obj
- cd obj
- ../configure 
- make -j4
- sudo make install

# emacs
- download soruce code
- [emacs 24.3](http://ftp.gnu.org/pub/gnu/emacs/emacs-24.3.tar.gz)
- mv ~/Downloads/emacs-24.3.tar .
- tar xvf emacs-24.3.tar
- wget http://svn.sourceforge.jp/svnroot/macemacsjp/inline_patch/trunk/emacs-inline.patch
- cd emacs-24.3
- patch -p0 < ../emacs-inline.patch
- mkdir obj
- cd obj
- ../configure --with-ns --without-x
- make -j4
- sudo make install
- sudo rm /usr/bin/emacs
- sudo rm -rf /usr/share/emacs
- create an alias '.~/.bash_profile'
- alias emacs="/usr/local/bin/emacs -nw"

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

## magit
- wget https://github.com/downloads/magit/magit/magit-1.2.0.tar.gz
- tar -xf magit-1.2.0.tar.gz
- cd magit-1.2.0
- make
- sudo make install
- (add-to-list 'load-path "/usr/local/share/emacs/site-lisp/magit")
- (require 'magit)

- 실행방법
 - M-x magit-status
 - 수정할 부분에서 s를 누르면 add가 된다.
 - modified 가 되면 c를 누르고 commit -m 을 입력한다.
 - C-c C-c를 입력하면 push가 된다.

- 기능 조작 방법
|조작커맨드|기능|
|i|.gitignore에 파일 추가|
|k|물리 파일 지우기|
|S| 모든 파일 add (git add -A)|
|u|add 대상에서 빠지기|
|I|로그를 보기|
|L|자세한 로그를 보기|
|v|커밋 취소|
|F|git pull|
|P|git push|
|m|git merge|
|t|tag만들기|

- [사용자 메뉴얼](http://philjackson.github.com/magit/magit.html)

## node.js
- source [node.js](http://nodejs.org/dist/v0.10.31/node-v0.10.31.tar.gz)
- tar -zxvf node-v0.10.31.tar.gz
- cd node-v0.10.31
- ./configure
- make
- sudo make install

## MacPorts
- source [MaxPorts-2.3.1](https://distfiles.macports.org/MacPorts/MacPorts-2.3.1.tar.bz2)
- tar -zxvf MaxPorts-2.3.1.tar.bz2
- cd MacPorts-2.3.1
- mkdir obj
- cd obj
- ../configure
- make
- sudo make install
