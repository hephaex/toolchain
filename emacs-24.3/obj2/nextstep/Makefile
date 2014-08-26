### nextstep/Makefile for GNU Emacs

## Copyright (C) 2012-2013 Free Software Foundation, Inc.

## This file is part of GNU Emacs.

## GNU Emacs is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
## 
## GNU Emacs is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with GNU Emacs.  If not, see <http://www.gnu.org/licenses/>.

### Commentary:

### Code:
SHELL = /bin/sh

srcdir = /Users/mscho/Simon/00_toolchain/emacs-24.3/nextstep
EXEEXT = 


MKDIR_P = ../../build-aux/install-sh -c -d

ns_appdir = /Users/mscho/Simon/00_toolchain/emacs-24.3/obj2/nextstep/Emacs.app
ns_appbindir = /Users/mscho/Simon/00_toolchain/emacs-24.3/obj2/nextstep/Emacs.app/Contents/MacOS
ns_appsrc = Cocoa/Emacs.base

${ns_appdir}: ${srcdir}/${ns_appsrc} ${ns_appsrc}
	rm -rf ${ns_appdir}
	${MKDIR_P} ${ns_appdir}
	( cd ${srcdir}/${ns_appsrc} ; tar cfh - . ) | \
	  ( cd ${ns_appdir} ; umask 022; tar xf - )
	[ `cd ${srcdir} && /bin/pwd` = `/bin/pwd` ] || \
	  ( cd ${ns_appsrc} ; tar cfh - . ) | \
	    ( cd ${ns_appdir} ; umask 022; tar xf - )

${ns_appbindir}/Emacs: ${ns_appdir} ../src/emacs${EXEEXT}
	${MKDIR_P} ${ns_appbindir}
	cp -f ../src/emacs${EXEEXT} ${ns_appbindir}/Emacs

.PHONY: all

all: ${ns_appdir} ${ns_appbindir}/Emacs


.PHONY: clean distclean maintainer-clean

clean:
	rm -rf ${ns_appdir}

distclean: clean
	rm -f Makefile
	rm -f GNUstep/Emacs.base/Resources/Info-gnustep.plist \
	  GNUstep/Emacs.base/Resources/Emacs.desktop \
	  Cocoa/Emacs.base/Contents/Info.plist \
	  Cocoa/Emacs.base/Contents/Resources/English.lproj/InfoPlist.strings

maintainer-clean: distclean

### Makefile.in ends here
