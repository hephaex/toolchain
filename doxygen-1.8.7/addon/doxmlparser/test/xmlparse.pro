#
# This file was generated from xmlparse.pro.in on 2014년 8월  4일 월요일 15시 44분 29초 KST
#

TEMPLATE     =	app.t
CONFIG       =	console warn_on release
HEADERS      =	
SOURCES      =	main.cpp
unix:LIBS                   += -L../../../lib -ldoxmlparser -lqtools
win32:INCLUDEPATH           += .
win32-mingw:LIBS            += -L../../../lib -ldoxmlparser -lqtools
win32-msvc:LIBS             += doxmlparser.lib qtools.lib shell32.lib 
win32-msvc:TMAKE_LFLAGS     += /LIBPATH:..\..\..\lib;..\lib
win32-borland:LIBS          += doxmlparser.lib qtools.lib shell32.lib
win32-borland:TMAKE_LFLAGS  += -L..\..\..\lib
win32:TMAKE_CXXFLAGS        += -DQT_NODLL
DESTDIR                     = ../../../bin
OBJECTS_DIR                 = ../../../objects/doxmlparser/test
TARGET                      = xmlparse
INCLUDEPATH                += ../../../qtools ../include
DEPENDPATH                 += ../include
unix:TARGETDEPS             = ../../../lib/libdoxmlparser.a
win32:TARGETDEPS            = ..\..\..\lib\doxmlparser.lib

