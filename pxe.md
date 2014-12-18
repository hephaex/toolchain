# pxe setting
* connect host
* ssh key generate
 - ssh-keygen -t rsa
* mv ~/.ssh/id_rsa authrized_keys

* woking check
 - ssh -i ~/.ssh/id_rsa 192.168.0.30

* edit sshd_config
 - vi /etc/ssh/sshd_config
   - // 48 line
     - PermitRootLogin no
   - // 77 line
     - PermitEmptyPasswords no
   - // 78 line
     - PasswordAuthentication no
 - systemctl restart sshd
 
* update system
 - yum -y update

## setting
* install dnsmasq

```
[root@pxe mscho]# yum install dnsmasq
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: centos.tt.co.kr
 * extras: centos.tt.co.kr
 * updates: centos.tt.co.kr
Package dnsmasq-2.66-12.el7.x86_64 already installed and latest version
Nothing to do
```

* edit dnsmasq.conf
 - [root@pxe mscho]# mv /etc/dnsmasq.conf /etc/dnsmasq.conf.backup
 - [root@pxe mscho]# vi /etc/dnsmasq.conf

* change each setting
 - interface: Interfaces that the server should listen and provide services.
 - bind-interfaces: Uncomment to bind only on this interface.
 - domain: Replace it with your domain name.
 - dhcp-range: Replace it with IP range defined by your network mask on this segment.
 - dhcp-boot: Replace the IP statement with your interface IP Address.
 - dhcp-option=3,192.168.1.1: Replace the IP Address with your network segment Gateway.
 - dhcp-option=6,92.168.1.1: Replace the IP Address with your DNS Server IP – several DNS IPs can be defined.
 - server=8.8.4.4: Put your DNS forwarders IPs Addresses.
 - dhcp-option=28,10.0.0.255: Replace the IP Address with network broadcast address –optionally.
 - dhcp-option=42,0.0.0.0: Put your network time servers – optionally (0.0.0.0 Address is for self-reference).
 - pxe-prompt: Leave it as default – means to hit F8 key for entering menu 60 with seconds wait time..
 - pxe=service: Use x86PC for 32-bit/64-bit architectures and enter a menu description prompt under string quotes. Other values types can be: PC98, IA64_EFI, Alpha, Arc_x86, Intel_Lean_Client, IA32_EFI, BC_EFI, Xscale_EFI and X86-64_EFI.
 - enable-tftp: Enables the build-in TFTP server.
 - tftp-root: Use /var/lib/tftpboot – the location for all netbooting files.


```
interface=eno16777736,lo
#bind-interfaces
domain=centos7.lan
# DHCP range-leases
dhcp-range= eno16777736,192.168.1.3,192.168.1.253,255.255.255.0,1h
# PXE
dhcp-boot=pxelinux.0,pxeserver,192.168.1.20
# Gateway
dhcp-option=3,192.168.1.1
# DNS
dhcp-option=6,92.168.1.1, 8.8.8.8
server=8.8.4.4
# Broadcast Address
dhcp-option=28,10.0.0.255
# NTP Server
dhcp-option=42,0.0.0.0

pxe-prompt="Press F8 for menu.", 60
pxe-service=x86PC, "Install CentOS 7 from network server 192.168.1.20", pxelinux
enable-tftp
tftp-root=/var/lib/tftpboot
```

## Install SYSLINUX Bootloaders
* yum install -y syslinux

```
[root@pxe mscho]# yum install syslinux
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: centos.tt.co.kr
 * extras: centos.tt.co.kr
 * updates: centos.tt.co.kr
Resolving Dependencies
 --> Running transaction check
 ---> Package syslinux.x86_64 0:4.05-8.el7 will be installed
 --> Processing Dependency: mtools for package: syslinux-4.05-8.el7.x86_64
 --> Running transaction check
 ---> Package mtools.x86_64 0:4.0.18-5.el7 will be installed
 --> Finished Dependency Resolution

Dependencies Resolved

================================================================================
 Package           Arch            Version                  Repository     Size
================================================================================
Installing:
 syslinux          x86_64          4.05-8.el7               base          1.0 M
 Installing for dependencies:
 mtools            x86_64          4.0.18-5.el7             base          203 k

Transaction Summary
================================================================================
Install  1 Package (+1 Dependent package)

Total download size: 1.2 M
Installed size: 2.6 M
Is this ok [y/d/N]: y
Downloading packages:
(1/2): mtools-4.0.18-5.el7.x86_64.rpm                      | 203 kB   00:00
(2/2): syslinux-4.05-8.el7.x86_64.rpm                      | 1.0 MB   00:00
--------------------------------------------------------------------------------
Total                                              1.6 MB/s | 1.2 MB  00:00
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : mtools-4.0.18-5.el7.x86_64                                   1/2
  Installing : syslinux-4.05-8.el7.x86_64                                   2/2
  Verifying  : syslinux-4.05-8.el7.x86_64                                   1/2
  Verifying  : mtools-4.0.18-5.el7.x86_64                                   2/2

Installed:
  syslinux.x86_64 0:4.05-8.el7

Dependency Installed:
  mtools.x86_64 0:4.0.18-5.el7

Complete!
```

* check files on /usr/share/syslinux

```
[root@pxe mscho]# ls /usr/share/syslinux/
altmbr.bin     ethersel.c32  isohdpfx.bin        mbr_c.bin     sanboot.c32
altmbr_c.bin   gfxboot.c32   isohdpfx_c.bin      mbr_f.bin     sdi.c32
altmbr_f.bin   gptmbr.bin    isohdpfx_f.bin      memdisk       sysdump.c32
cat.c32        gptmbr_c.bin  isohdppx.bin        memdump.com   syslinux64.exe
chain.c32      gptmbr_f.bin  isohdppx_c.bin      meminfo.c32   syslinux.com
cmd.c32        gpxecmd.c32   isohdppx_f.bin      menu.c32      syslinux.exe
config.c32     gpxelinux.0   isolinux.bin        pcitest.c32   ver.com
cpuid.c32      gpxelinuxk.0  isolinux-debug.bin  pmload.c32    vesainfo.c32
cpuidtest.c32  hdt.c32       kbdmap.c32          poweroff.com  vesamenu.c32
diag           host.c32      linux.c32           pwd.c32       vpdtest.c32
disk.c32       ifcpu64.c32   ls.c32              pxechain.com  whichsys.c32
dmitest.c32    ifcpu.c32     lua.c32             pxelinux.0    zzjson.c32
dosutil        ifplop.c32    mboot.c32           reboot.c32
elf.c32        int18.com     mbr.bin             rosh.c32
```

## Install TFTP-server
* yum -y install tftp-server

```
[root@pxe mscho]# yum -y install tftp-server
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: centos.tt.co.kr
 * extras: centos.tt.co.kr
 * updates: centos.tt.co.kr
Resolving Dependencies
 --> Running transaction check
 ---> Package tftp-server.x86_64 0:5.2-11.el7 will be installed
 --> Processing Dependency: xinetd for package: tftp-server-5.2-11.el7.x86_64
 --> Running transaction check
 ---> Package xinetd.x86_64 2:2.3.15-12.el7 will be installed
 --> Finished Dependency Resolution

Dependencies Resolved

================================================================================
 Package             Arch           Version                  Repository    Size
================================================================================
Installing:
  tftp-server         x86_64         5.2-11.el7               base          44 k
Installing for dependencies:
  xinetd              x86_64         2:2.3.15-12.el7          base         128 k

Transaction Summary
================================================================================
Install  1 Package (+1 Dependent package)

Total download size: 172 k
Installed size: 325 k
Downloading packages:
(1/2): tftp-server-5.2-11.el7.x86_64.rpm                   |  44 kB   00:00
(2/2): xinetd-2.3.15-12.el7.x86_64.rpm                     | 128 kB   00:00
--------------------------------------------------------------------------------
Total                                              296 kB/s | 172 kB  00:00
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
Installing : 2:xinetd-2.3.15-12.el7.x86_64                                1/2
Installing : tftp-server-5.2-11.el7.x86_64                                2/2
Verifying  : 2:xinetd-2.3.15-12.el7.x86_64                                1/2
Verifying  : tftp-server-5.2-11.el7.x86_64                                2/2

Installed:
  tftp-server.x86_64 0:5.2-11.el7

Dependency Installed:
  xinetd.x86_64 2:2.3.15-12.el7

Complete!
```

* cp -r /usr/share/syslinux/* /var/lib/tftpboot

## setup PXE server configuration file
* mkdir /var/lib/tftpboot/pxelinux.cfg
* touch /var/lib/tftpboot/pxelinux.cfg/default
* vi /var/lib/tftpboot/pxelinux.cfg/default

```
default menu.c32
prompt 0
timeout 300
ONTIMEOUT local

menu title ########## PXE Boot Menu ##########

label 1
menu label ^1) Install CentOS 7 x64 with Local Repo
kernel centos7/vmlinuz
append initrd=centos7/initrd.img method=ftp://192.168.1.20/pub devfs=nomount

label 2
menu label ^2) Install CentOS 7 x64 with http://mirror.centos.org Repo
kernel centos7/vmlinuz
append initrd=centos7/initrd.img method=http://mirror.centos.org/centos/7/os/x86_64/ devfs=nomount ip=dhcp

label 3
menu label ^3) Install CentOS 7 x64 with Local Repo using VNC
kernel centos7/vmlinuz
append  initrd=centos7/initrd.img method=ftp://192.168.1.20/pub devfs=nomount inst.vnc inst.vncpassword=password

label 4
menu label ^4) Boot from local drive
```

* another setting

```
DEFAULT vesamenu.c32
MENU BACKGROUND centos7pxebg.png
MENU COLOR BORDER 0 #ffffffff #00000000 std
MENU COLOR TITLE 0 #ffffffff #00000000 std
MENU COLOR SEL 0 #ffffffff #ff000000 std
MENU TITLE CentOS 7 PXE Boot Menu
PROMPT 0
TIMEOUT 300
ONTIMEOUT local
#Local Boot
LABEL local
MENU LABEL Boot Local HDD
LOCALBOOT 0
#CentOS 7 x86_64
LABEL centos764
MENU LABEL CentOS 7 x86_64
KERNEL images/centos/x86_64/7/vmlinuz
APPEND initrd=images/centos/x86_64/7/initrd.img inst.repo=ftp://192.168.1.254/install/centos/x86_64/7
# ks=ftp://192.168.1.254/install/centos/x86_64/7/ks/ks.cfg # add this to APPEND if you have a kickstart file
#Fedora 20 x86_64
LABEL fedora2064
MENU LABEL Fedora 20 x86_64
KERNEL mages/fedora/x86_64/20/vmlinuz
APPEND initrd=images/fedora/x86_64/20/initrd.img inst.repo=ftp://192.168.1.254/install/fedora/x86_64/20
# ks=ftp://192.168.1.254/install/fedora/x86_64/20/ks/ks.cfg # add this to APPEND if you have a kickstart file
#Fedora 20 i386
LABEL Fedora2032
MENU LABEL Fedora 20 i386
KERNEL images/fedora/i386/20/vmlinuz
APPEND initrd=images/fedora/i386/20/initrd.img inst.repo=ftp://192.168.1.254/install/fedora/i386/20
```

## Add CentOS7 Boot Images to PXE Server
* wget http://mirrors.xservers.ro/centos/7.0.1406/isos/x86_64/CentOS-7.0-1406-x86_64-DVD.iso
* mount -o loop /home/mscho/CentOS-7.0-1406-x86_64-DVD.iso /mnt
* mkdir /var/lib/tftpboot/centos7
* cp /mnt/images/pxeboot/vmlinuz /var/lib/tftpboot/centos7/
* cp /mnt/images/pxeboot/initrd.img /var/lib/tftpboot/centos7/

```
[root@pxe mscho]# yum install vsftpd
Loaded plugins: fastestmirror
base                                                     | 3.6 kB     00:00
extras                                                   | 3.4 kB     00:00
updates                                                  | 3.4 kB     00:00
updates/7/x86_64/primary_db                                | 5.4 MB   00:02
Loading mirror speeds from cached hostfile
 * base: centos.tt.co.kr
 * extras: centos.tt.co.kr
 * updates: centos.tt.co.kr
Resolving Dependencies
 --> Running transaction check
 ---> Package vsftpd.x86_64 0:3.0.2-9.el7 will be installed
 --> Finished Dependency Resolution

Dependencies Resolved

================================================================================
 Package          Arch             Version                 Repository      Size
 ================================================================================
 Installing:
  vsftpd           x86_64           3.0.2-9.el7             base           165 k

Transaction Summary
================================================================================
Install  1 Package

Total download size: 165 k
Installed size: 343 k
Is this ok [y/d/N]: y
Downloading packages:
vsftpd-3.0.2-9.el7.x86_64.rpm                              | 165 kB   00:00
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : vsftpd-3.0.2-9.el7.x86_64                                    1/1
  Verifying  : vsftpd-3.0.2-9.el7.x86_64                                    1/1

Installed:
  vsftpd.x86_64 0:3.0.2-9.el7

Complete!
```

* cp -r /mnt/* /var/ftp/pub/

## Start DNSMASQ & VSFTPD server

```
[root@pxe mscho]# systemctl start dnsmasq
[root@pxe mscho]# systemctl status dnsmasq
dnsmasq.service - DNS caching server.
   Loaded: loaded (/usr/lib/systemd/system/dnsmasq.service; disabled)
   Active: active (running) since Mon 2014-12-15 15:18:37 KST; 10s ago
 Main PID: 10929 (dnsmasq)
   CGroup: /system.slice/dnsmasq.service
           └─10929 /usr/sbin/dnsmasq -k

Dec 15 15:18:37 pxe dnsmasq[10929]: compile time options: IPv6 GNU-getopt D...th
Dec 15 15:18:37 pxe dnsmasq[10929]: warning: interface eno16777736 does not...st
Dec 15 15:18:37 pxe dnsmasq-dhcp[10929]: DHCP, IP range 192.168.1.3 -- 192.1...h
Dec 15 15:18:37 pxe dnsmasq-tftp[10929]: TFTP root is /var/lib/tftpboot
Dec 15 15:18:37 pxe dnsmasq[10929]: using nameserver 8.8.4.4#53
Dec 15 15:18:37 pxe dnsmasq[10929]: reading /etc/resolv.conf
Dec 15 15:18:37 pxe dnsmasq[10929]: using nameserver 8.8.4.4#53
Dec 15 15:18:37 pxe dnsmasq[10929]: using nameserver 8.8.8.8#53
Dec 15 15:18:37 pxe dnsmasq[10929]: using nameserver 8.8.4.4#53
Dec 15 15:18:37 pxe dnsmasq[10929]: read /etc/hosts - 2 addresses
Hint: Some lines were ellipsized, use -l to show in full.
[root@pxe mscho]# systemctl start vsftpd
[root@pxe mscho]# systemctl status vsftpd
vsftpd.service - Vsftpd ftp daemon
   Loaded: loaded (/usr/lib/systemd/system/vsftpd.service; disabled)
   Active: active (running) since Mon 2014-12-15 15:19:04 KST; 9s ago
  Process: 10933 ExecStart=/usr/sbin/vsftpd /etc/vsftpd/vsftpd.conf (code=exited, status=0/SUCCESS)
 Main PID: 10934 (vsftpd)
   CGroup: /system.slice/vsftpd.service
           └─10934 /usr/sbin/vsftpd /etc/vsftpd/vsftpd.conf

Dec 15 15:19:04 pxe systemd[1]: Starting Vsftpd ftp daemon...
Dec 15 15:19:04 pxe systemd[1]: Started Vsftpd ftp daemon.
[root@pxe mscho]# systemctl enable dnsmasq
ln -s '/usr/lib/systemd/system/dnsmasq.service' '/etc/systemd/system/multi-user.target.wants/dnsmasq.service'
[root@pxe mscho]# systemctl enable vsftpd
ln -s '/usr/lib/systemd/system/vsftpd.service' '/etc/systemd/system/multi-user.target.wants/vsftpd.service'
```

```
[root@pxe mscho]# firewall-cmd --add-service=ftp --permanent
success
[root@pxe mscho]# firewall-cmd --add-service=dns --permanent  ## Port 53

success
[root@pxe mscho]# firewall-cmd --add-service=dhcp --permanent  ## Port 67
success
[root@pxe mscho]# firewall-cmd --add-port=69/udp --permanent  ## Port for TFTP
success
[root@pxe mscho]# firewall-cmd --add-port=4011/udp --permanent  ## Port for ProxyDHCP
success
[root@pxe mscho]# firewall-cmd --reload  ## Apply rules
success
```

## iscsi setting
* chekc blkid /dev/sda1
 - blkid /dev/sda1
 - dev/sda1: UUID="b96b91c8-ac41-4be2-af25-07754a776569" TYPE="xfs" PARTUUID="069af106-ca30-4854-87eb-78acc8181a89"
* edit /etc/fstab
* 
