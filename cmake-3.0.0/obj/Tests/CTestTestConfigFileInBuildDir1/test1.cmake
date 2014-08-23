cmake_minimum_required(VERSION 2.8)

# Settings:
set(CTEST_DASHBOARD_ROOT                "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTest")
set(CTEST_SITE                          "Medardus.local")
set(CTEST_BUILD_NAME                    "CTestTest-Darwin-g++-ConfigFileInBuildDir1")

set(CTEST_SOURCE_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/Tests/CTestTestConfigFileInBuildDir")
set(CTEST_BINARY_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestConfigFileInBuildDir1")
set(CTEST_CVS_COMMAND                   "CVSCOMMAND-NOTFOUND")
set(CTEST_CMAKE_GENERATOR               "Unix Makefiles")
set(CTEST_CMAKE_GENERATOR_TOOLSET       "")
set(CTEST_BUILD_CONFIGURATION           "$ENV{CMAKE_CONFIG_TYPE}")
set(CTEST_COVERAGE_COMMAND              "/usr/local/bin/gcov")
set(CTEST_NOTES_FILES                   "${CTEST_SCRIPT_DIRECTORY}/${CTEST_SCRIPT_NAME}")

CTEST_START(Experimental)
CTEST_CONFIGURE(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
