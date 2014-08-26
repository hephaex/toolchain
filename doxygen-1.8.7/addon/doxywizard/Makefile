#
# This file was generated from Makefile.in on 2014년 8월  4일 월요일 15시 44분 29초 KST
#

DOXYGEN   = /Users/mscho/Simon/00_toolchain/doxygen-1.8.7
TMAKEPATH = /Users/mscho/Simon/00_toolchain/doxygen-1.8.7/tmake/lib/macosx-c++
ENV       = env TMAKEPATH="$(TMAKEPATH)"
TMAKE     = /Users/mscho/Simon/00_toolchain/doxygen-1.8.7/tmake/bin/tmake
MAKE      = /usr/bin/make
PYTHON    = /usr/bin/python
LEX       = /usr/bin/flex
RM        = rm -f
CP        = cp
VERSION   = 1.8.7
INSTALL   = /usr/local
INSTTOOL  = /usr/bin/install
DOXYDOCS  = ..
DOCDIR    = $(INSTALL)/share/doc/packages/doxygen
QTDIR     = 
MKSPECS   = -spec macx-g++
#
# 
#
# Copyright (C) 1997-2014 by Dimitri van Heesch.
# 
# Permission to use, copy, modify, and distribute this software and its
# documentation under the terms of the GNU General Public License is hereby 
# granted. No representations are made about the suitability of this software 
# for any purpose. It is provided "as is" without express or implied warranty.
# See the GNU General Public License for more details.
# 

QMAKE=$(QTDIR)/bin/qmake $(MKSPECS)
INCBUFSIZE=$(PYTHON) ../../src/increasebuffer.py

all: Makefile.doxywizard  
	$(MAKE) -f Makefile.doxywizard LEX=$(LEX) PYTHON=$(PYTHON) INCBUFSIZE="$(INCBUFSIZE)"

Makefile.doxywizard: doxywizard.pro
	$(QMAKE) doxywizard.pro -o Makefile.doxywizard

qmake: 
	$(QMAKE) doxywizard.pro -o Makefile.doxywizard

clean: Makefile.doxywizard 
	$(MAKE) -f Makefile.doxywizard clean 

distclean: Makefile.doxywizard
	$(MAKE) -f Makefile.doxywizard distclean 
	$(RM) Makefile.doxywizard

install:
	$(INSTTOOL) -d $(INSTALL)/bin	
	$(INSTTOOL) -m 755 ../../bin/doxywizard $(INSTALL)/bin	
	$(INSTTOOL) -d $(INSTALL)/$(MAN1DIR)
	cat ../../doc/doxywizard.1 | sed -e "s/DATE/$(DATE)/g" -e "s/VERSION/$(VERSION)/g" > doxywizard.1
	$(INSTTOOL) -m 644 doxywizard.1 $(INSTALL)/$(MAN1DIR)/doxywizard.1
	rm doxywizard.1

FORCE:
