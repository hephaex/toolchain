Toolchain
=========

OSX opensource high sierra toolchain

## prerequirement
1. xcode install
> $ xcode-select --install

# cmake
> $ ./cmake_install.sh

## autoconf & automake & libtool & texinfo
> $ ./autoconf_install.sh

## emacs 25.2.1 no-x-windows
> $ ./osx_emacs25-nox.sh
> $ alias emacs="/usr/local/bin/emacs -nw"


## openssl-1.0.1i
> $ ./openssl.sh

## ruby
> $ ./ruby.sh

## vagrant
> $ ./vagrant.sh

## Virtualbox
* get virtual-box 5.2.x image
> $ wget http://download.virtualbox.org/virtualbox/5.2.4/virtualbox-5.2_5.2.4-11978$
* get virtual-box 5.2.x extention pack
> $ wget http://download.virtualbox.org/virtualbox/5.2.4/Oracle_VM_VirtualBox_Exten$
* install virtual-box 5.2.x
> $ sudo dpkg -i virtualbox-5.2_5.2.4-119785~Ubuntu~xenial_amd64.deb
* install libsdl1.2debian
> $ sudo apt-get install libsdl1.2debian
* install virtual-box extention pack
> * sudo VBoxManage extpack install Oracle_VM_VirtualBox_Extension_Pack-5.2.4-11978$
