#
# This file was generated from doxyapp.pro.in on 2014년 8월  4일 월요일 15시 44분 29초 KST
#

TEMPLATE     =	app.t
CONFIG       =	console warn_on debug
HEADERS      =	
SOURCES      =	doxyapp.cpp
LIBS          += -L../../lib -ldoxygen -lqtools -lmd5 -ldoxycfg -lpthread -liconv
DESTDIR        = 
OBJECTS_DIR    = ../../objects/doxyapp
TARGET         = ../../bin/doxyapp
INCLUDEPATH   += ../../qtools ../../src
DEPENDPATH    += ../../src
TARGETDEPS     = ../../lib/libdoxygen.a

