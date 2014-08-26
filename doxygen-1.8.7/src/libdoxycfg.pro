#
# This file was generated from libdoxycfg.pro.in on 2014년 8월  4일 월요일 15시 44분 29초 KST
#

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
# Documents produced by Doxygen are derivative works derived from the
# input used in their production; they are not affected by this license.
#
# TMake project file for doxygen

TEMPLATE     =	libdoxycfg.t
CONFIG       =	console warn_on staticlib release
HEADERS      =  config.h configoptions.h portable.h
SOURCES      =	../generated_src/doxygen/config.cpp ../generated_src/doxygen/configoptions.cpp portable.cpp portable_c.c
win32:TMAKE_CXXFLAGS       += -DQT_NODLL
win32-g++:TMAKE_CXXFLAGS   += -fno-exceptions -fno-rtti
DEPENDPATH                 += ../generated_src/doxygen
INCLUDEPATH                += ../generated_src/doxygen . ../qtools
DESTDIR                    =  ../lib
TARGET                     =  doxycfg
OBJECTS_DIR                =  ../objects/doxygen
