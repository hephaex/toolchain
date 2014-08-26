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
# Copyright (C) 1997-2000 by Dimitri van Heesch.
# 
# Permission to use, copy, modify, and distribute this software and its
# documentation under the terms of the GNU General Public License is hereby 
# granted. No representations are made about the suitability of this software 
# for any purpose. It is provided "as is" without express or implied warranty.
# See the GNU General Public License for more details.
# 
# Documents produced by Doxygen are derivative works derived from the
# input used in their production; they are not affected by this license.
# 

all: Makefile.qtools Makefile
	$(MAKE) -f Makefile.qtools    $@

Makefile.qtools: qtools.pro 
	$(ENV) $(PERL) "$(TMAKE)" qtools.pro >Makefile.qtools

tmake: 
	$(ENV) $(PERL) "$(TMAKE)" qtools.pro >Makefile.qtools

clean: Makefile.qtools 
	$(MAKE) -f Makefile.qtools    clean 

distclean: clean

FORCE:
