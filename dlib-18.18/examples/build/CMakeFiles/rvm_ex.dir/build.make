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
include CMakeFiles/rvm_ex.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/rvm_ex.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/rvm_ex.dir/flags.make

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o: CMakeFiles/rvm_ex.dir/flags.make
CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o: ../rvm_ex.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o -c /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/rvm_ex.cpp

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/rvm_ex.dir/rvm_ex.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/rvm_ex.cpp > CMakeFiles/rvm_ex.dir/rvm_ex.cpp.i

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/rvm_ex.dir/rvm_ex.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/rvm_ex.cpp -o CMakeFiles/rvm_ex.dir/rvm_ex.cpp.s

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.requires:
.PHONY : CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.requires

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.provides: CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.requires
	$(MAKE) -f CMakeFiles/rvm_ex.dir/build.make CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.provides.build
.PHONY : CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.provides

CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.provides.build: CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o

# Object files for target rvm_ex
rvm_ex_OBJECTS = \
"CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o"

# External object files for target rvm_ex
rvm_ex_EXTERNAL_OBJECTS =

rvm_ex: CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o
rvm_ex: CMakeFiles/rvm_ex.dir/build.make
rvm_ex: dlib_build/libdlib.a
rvm_ex: /usr/lib/libpthread.dylib
rvm_ex: /usr/X11R6/lib/libX11.dylib
rvm_ex: /usr/local/lib/libpng.dylib
rvm_ex: /usr/lib/libcblas.dylib
rvm_ex: /usr/lib/liblapack.dylib
rvm_ex: /usr/lib/libsqlite3.dylib
rvm_ex: CMakeFiles/rvm_ex.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable rvm_ex"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/rvm_ex.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/rvm_ex.dir/build: rvm_ex
.PHONY : CMakeFiles/rvm_ex.dir/build

CMakeFiles/rvm_ex.dir/requires: CMakeFiles/rvm_ex.dir/rvm_ex.cpp.o.requires
.PHONY : CMakeFiles/rvm_ex.dir/requires

CMakeFiles/rvm_ex.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/rvm_ex.dir/cmake_clean.cmake
.PHONY : CMakeFiles/rvm_ex.dir/clean

CMakeFiles/rvm_ex.dir/depend:
	cd /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/mscho/Simon/91_toolchain/dlib-18.18/examples /Users/mscho/Simon/91_toolchain/dlib-18.18/examples /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build /Users/mscho/Simon/91_toolchain/dlib-18.18/examples/build/CMakeFiles/rvm_ex.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/rvm_ex.dir/depend

