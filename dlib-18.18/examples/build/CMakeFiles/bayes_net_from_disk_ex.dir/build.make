# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.0

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/bin/cmake

# The command to remove a file.
RM = /usr/local/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/mscho/Simon/91_toolchain/dlib-18.18/examples

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build

# Include any dependencies generated for this target.
include CMakeFiles/bayes_net_from_disk_ex.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/bayes_net_from_disk_ex.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/bayes_net_from_disk_ex.dir/flags.make

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o: CMakeFiles/bayes_net_from_disk_ex.dir/flags.make
CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o: ../bayes_net_from_disk_ex.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o -c /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/bayes_net_from_disk_ex.cpp

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/bayes_net_from_disk_ex.cpp > CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.i

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/bayes_net_from_disk_ex.cpp -o CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.s

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.requires:
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.requires

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.provides: CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.requires
	$(MAKE) -f CMakeFiles/bayes_net_from_disk_ex.dir/build.make CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.provides.build
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.provides

CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.provides.build: CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o

# Object files for target bayes_net_from_disk_ex
bayes_net_from_disk_ex_OBJECTS = \
"CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o"

# External object files for target bayes_net_from_disk_ex
bayes_net_from_disk_ex_EXTERNAL_OBJECTS =

bayes_net_from_disk_ex: CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o
bayes_net_from_disk_ex: CMakeFiles/bayes_net_from_disk_ex.dir/build.make
bayes_net_from_disk_ex: dlib_build/libdlib.a
bayes_net_from_disk_ex: /usr/lib/libpthread.dylib
bayes_net_from_disk_ex: /usr/X11R6/lib/libX11.dylib
bayes_net_from_disk_ex: /usr/local/lib/libpng.dylib
bayes_net_from_disk_ex: /usr/lib/libcblas.dylib
bayes_net_from_disk_ex: /usr/lib/liblapack.dylib
bayes_net_from_disk_ex: /usr/lib/libsqlite3.dylib
bayes_net_from_disk_ex: CMakeFiles/bayes_net_from_disk_ex.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable bayes_net_from_disk_ex"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/bayes_net_from_disk_ex.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/bayes_net_from_disk_ex.dir/build: bayes_net_from_disk_ex
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/build

CMakeFiles/bayes_net_from_disk_ex.dir/requires: CMakeFiles/bayes_net_from_disk_ex.dir/bayes_net_from_disk_ex.cpp.o.requires
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/requires

CMakeFiles/bayes_net_from_disk_ex.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/bayes_net_from_disk_ex.dir/cmake_clean.cmake
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/clean

CMakeFiles/bayes_net_from_disk_ex.dir/depend:
	cd /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/mscho/Simon/91_toolchain/dlib-18.18/examples /Users/mscho/Simon/91_toolchain/dlib-18.18/examples /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build/CMakeFiles/bayes_net_from_disk_ex.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/bayes_net_from_disk_ex.dir/depend

