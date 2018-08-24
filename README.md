Toolchain
=========

open source toolchain for OSX  high sierra 

## prerequirement toolchain
1. xcode install
> $ xcode-select --install

# cmake install
> $ ./cmake_install.sh

## autoconf & automake & libtool & texinfo
> $ ./autoconf_install.sh

## emacs no-x-windows
> $ ./osx_emacs26-nox.sh


## openssl-$(latest)
> $ ./openssl.sh

## ruby-$(latest)
> $ ./ruby.sh

## vagrant-$(latest)
> $ ./vagrant.sh

## Virtualbox (for Linux Enviroment.)
* get virtual-box 5.2.x image
> $ wget http://download.virtualbox.org/virtualbox/$(latest)/virtualbox-$(latest)$
* get virtual-box 5.2.x extention pack
> $ wget http://download.virtualbox.org/virtualbox/$(latest)/Oracle_VM_VirtualBox_Exten$
* install virtual-box 
> $ sudo dpkg -i virtualbox-$(latest)_amd64.deb
* install virtual-box extention pack
> $ sudo VBoxManage extpack install Oracle_VM_VirtualBox_Extension_Pack-$(latest)
