# CMake generated Testfile for 
# Source directory: /Users/mscho/Simon/Tools/cmake-3.0.0
# Build directory: /Users/mscho/Simon/Tools/cmake-3.0.0/obj
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
include("/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/EnforceConfig.cmake")
add_test(SystemInformationNew "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/cmake" "--system-information" "-G" "Unix Makefiles")
subdirs(Utilities/KWIML)
subdirs(Source/kwsys)
subdirs(Utilities/cmzlib)
subdirs(Utilities/cmcurl)
subdirs(Utilities/cmcompress)
subdirs(Utilities/cmbzip2)
subdirs(Utilities/cmlibarchive)
subdirs(Utilities/cmexpat)
subdirs(Source/CursesDialog/form)
subdirs(Source)
subdirs(Utilities)
subdirs(Tests)
subdirs(Auxiliary)
