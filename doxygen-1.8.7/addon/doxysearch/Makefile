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

all clean depend: Makefile.doxysearch Makefile.doxyindexer
	$(MAKE) -f Makefile.doxysearch $@
	$(MAKE) -f Makefile.doxyindexer $@

distclean: clean
	$(RM) -rf Makefile doxysearch.pro Makefile.doxysearch
	$(RM) -rf Makefile doxyindexer.pro Makefile.doxyindexer

tmake:
	$(ENV) $(PERL) "$(TMAKE)" doxysearch.pro >Makefile.doxysearch
	$(ENV) $(PERL) "$(TMAKE)" doxyindexer.pro >Makefile.doxyindexer

strip:
	strip doxysearch

Makefile.doxysearch: doxysearch.pro
	$(ENV) $(PERL) "$(TMAKE)" doxysearch.pro >Makefile.doxysearch

Makefile.doxyindexer: doxyindexer.pro
	$(ENV) $(PERL) "$(TMAKE)" doxyindexer.pro >Makefile.doxyindexer

install:
	$(INSTTOOL) -d $(INSTALL)/bin	
	$(INSTTOOL) -m 755 ../../bin/doxysearch.cgi $(INSTALL)/bin	
	$(INSTTOOL) -m 755 ../../bin/doxyindexer $(INSTALL)/bin	
	$(INSTTOOL) -d $(INSTALL)/$(MAN1DIR)
	cat ../../doc/doxyindexer.1 | sed -e "s/DATE/$(DATE)/g" -e "s/VERSION/$(VERSION)/g" > doxyindexer.1
	$(INSTTOOL) -m 644 doxyindexer.1 $(INSTALL)/$(MAN1DIR)/doxyindexer.1
	rm doxyindexer.1
	cat ../../doc/doxysearch.1 | sed -e "s/DATE/$(DATE)/g" -e "s/VERSION/$(VERSION)/g" > doxysearch.1
	$(INSTTOOL) -m 644 doxysearch.1 $(INSTALL)/$(MAN1DIR)/doxysearch.1
	rm doxysearch.1

