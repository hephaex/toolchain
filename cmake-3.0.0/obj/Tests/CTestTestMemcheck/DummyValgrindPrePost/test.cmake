cmake_minimum_required(VERSION 2.8.9)

# Settings:
set(CTEST_DASHBOARD_ROOT                "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck")
set(CTEST_SITE                          "Medardus.local")
set(CTEST_BUILD_NAME                    "CTestTest-Darwin-g++-MemcheckDummyValgrindPrePost")

set(CTEST_SOURCE_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindPrePost")
set(CTEST_BINARY_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindPrePost")
set(CTEST_CVS_COMMAND                   "CVSCOMMAND-NOTFOUND")
set(CTEST_CMAKE_GENERATOR               "Unix Makefiles")
set(CTEST_CMAKE_GENERATOR_TOOLSET       "")
set(CTEST_BUILD_CONFIGURATION           "$ENV{CMAKE_CONFIG_TYPE}")
set(CTEST_COVERAGE_COMMAND              "/usr/local/bin/gcov")
set(CTEST_NOTES_FILES                   "${CTEST_SCRIPT_DIRECTORY}/${CTEST_SCRIPT_NAME}")

set(CTEST_MEMORYCHECK_COMMAND "${PSEUDO_VALGRIND}")

string(REPLACE " " "\\ " PRE_POST_COMMAND "${CTEST_MEMORYCHECK_COMMAND}")

set(CTEST_CUSTOM_PRE_MEMCHECK "${PRE_POST_COMMAND} pre command")
set(CTEST_CUSTOM_POST_MEMCHECK "${PRE_POST_COMMAND} post command ")


CTEST_START(Experimental)
CTEST_CONFIGURE(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
CTEST_TEST(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
CTEST_MEMCHECK(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
