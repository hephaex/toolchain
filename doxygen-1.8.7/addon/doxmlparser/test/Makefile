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
all clean depend: Makefile.xmlparse 
	$(MAKE) -f Makefile.xmlparse $@

distclean: clean
	$(RM) -rf Makefile.xmlparse xmlparse.pro Makefile obj

realclean: distclean

tmake:
	$(ENV) $(PERL) $(TMAKE) xmlparse.pro >Makefile.xmlparse

Makefile.xmlparse: xmlparse.pro
	$(ENV) $(PERL) $(TMAKE) xmlparse.pro >Makefile.xmlparse

install:
