cmake_minimum_required(VERSION 2.4)

# Settings:
set(CTEST_DASHBOARD_ROOT                "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTest")
set(CTEST_SITE                          "Medardus.local")
set(CTEST_BUILD_NAME                    "CTestTest-Darwin-g++-Upload")

set(CTEST_SOURCE_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/Tests/CTestTestUpload")
set(CTEST_BINARY_DIRECTORY              "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestUpload")
set(CTEST_CMAKE_GENERATOR               "Unix Makefiles")
set(CTEST_CMAKE_GENERATOR_TOOLSET       "")
set(CTEST_BUILD_CONFIGURATION           "$ENV{CMAKE_CONFIG_TYPE}")

CTEST_START(Experimental)
CTEST_CONFIGURE(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
CTEST_BUILD(BUILD "${CTEST_BINARY_DIRECTORY}" RETURN_VALUE res)
CTEST_UPLOAD(FILES "${CTEST_SOURCE_DIRECTORY}/sleep.c" "${CTEST_BINARY_DIRECTORY}/CMakeCache.txt")
CTEST_SUBMIT()
