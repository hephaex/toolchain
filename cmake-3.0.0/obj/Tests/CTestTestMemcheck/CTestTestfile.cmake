# CMake generated Testfile for 
# Source directory: /Users/mscho/Simon/Tools/cmake-3.0.0/Tests/CTestTestMemcheck
# Build directory: /Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(CTestTestMemcheckDummyPurify "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyPurify/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyPurify/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyPurify PROPERTIES  PASS_REGULAR_EXPRESSION "
1/1 MemCheck #1: RunCMake \\.+   Passed +[0-9]+\\.[0-9]+ sec
(ctest\\([0-9]+\\) malloc: [^
]*
)*
100% tests passed, 0 tests failed out of 1
.*
-- Processing memory checking output:( )
(ctest\\([0-9]+\\) malloc: [^
]*
)*Memory checking results:
((ctest\\([0-9]+\\) malloc: |BullseyeCoverage|==)[^
]*
)*\$")
add_test(CTestTestMemcheckDummyValgrind "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrind/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrind/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrind PROPERTIES  PASS_REGULAR_EXPRESSION "
1/1 MemCheck #1: RunCMake \\.+   Passed +[0-9]+\\.[0-9]+ sec
(ctest\\([0-9]+\\) malloc: [^
]*
)*
100% tests passed, 0 tests failed out of 1
.*
-- Processing memory checking output:( )
(ctest\\([0-9]+\\) malloc: [^
]*
)*Memory checking results:
((ctest\\([0-9]+\\) malloc: |BullseyeCoverage|==)[^
]*
)*\$")
add_test(CTestTestMemcheckDummyBC "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyBC/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyBC/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyBC PROPERTIES  PASS_REGULAR_EXPRESSION "
1/1 MemCheck #1: RunCMake \\.+   Passed +[0-9]+.[0-9]+ sec

100% tests passed, 0 tests failed out of 1
(.*
)?Error parsing XML in stream at line 1: no element found
")
add_test(CTestTestMemcheckDummyPurifyNoLogfile "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyPurifyNoLogfile/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyPurifyNoLogfile/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/valgrind")
set_tests_properties(CTestTestMemcheckDummyPurifyNoLogfile PROPERTIES  PASS_REGULAR_EXPRESSION "
Cannot find memory tester output file: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyPurifyNoLogfile/Testing/Temporary/MemoryChecker.1.log
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyPurifyNoLogfile/test.cmake
")
add_test(CTestTestMemcheckDummyValgrindNoLogfile "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindNoLogfile/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindNoLogfile/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/valgrind")
set_tests_properties(CTestTestMemcheckDummyValgrindNoLogfile PROPERTIES  PASS_REGULAR_EXPRESSION "
Cannot find memory tester output file: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindNoLogfile/Testing/Temporary/MemoryChecker.1.log
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindNoLogfile/test.cmake
")
add_test(CTestTestMemcheckDummyBCNoLogfile "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyBCNoLogfile/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyBCNoLogfile/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NoLogDummyChecker/valgrind")
set_tests_properties(CTestTestMemcheckDummyBCNoLogfile PROPERTIES  PASS_REGULAR_EXPRESSION "
Cannot find memory tester output file: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyBCNoLogfile/Testing/Temporary/MemoryChecker.1.log
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyBCNoLogfile/test.cmake
")
add_test(CTestTestMemcheckDummyValgrindPrePost "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindPrePost/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindPrePost/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindPrePost PROPERTIES  PASS_REGULAR_EXPRESSION "
1/1 MemCheck #1: RunCMake \\.+   Passed +[0-9]+\\.[0-9]+ sec
(ctest\\([0-9]+\\) malloc: [^
]*
)*
100% tests passed, 0 tests failed out of 1
.*
-- Processing memory checking output:( )
(ctest\\([0-9]+\\) malloc: [^
]*
)*Memory checking results:
((ctest\\([0-9]+\\) malloc: |BullseyeCoverage|==)[^
]*
)*\$")
add_test(CTestTestMemcheckDummyValgrindFailPost "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPost/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPost/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindFailPost PROPERTIES  PASS_REGULAR_EXPRESSION "
Problem running command: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck[^
]*fail[^
]*
(.*
)?Problem executing post-memcheck command\\(s\\).
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPost/test.cmake
")
add_test(CTestTestMemcheckDummyValgrindFailPre "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPre/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPre/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindFailPre PROPERTIES  PASS_REGULAR_EXPRESSION "
Problem running command: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck[^
]*fail[^
]*
(.*
)?Problem executing pre-memcheck command\\(s\\).
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindFailPre/test.cmake
")
add_test(CTestTestMemcheckDummyValgrindIgnoreMemcheck "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindIgnoreMemcheck/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindIgnoreMemcheck/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindIgnoreMemcheck PROPERTIES  PASS_REGULAR_EXPRESSION "
2/2 Test #2: RunCMakeAgain .*
1/1 MemCheck #1: RunCMake \\.+   Passed +[0-9]+\\.[0-9]+ sec
(ctest\\([0-9]+\\) malloc: [^
]*
)*
100% tests passed, 0 tests failed out of 1
.*
-- Processing memory checking output:( )
(ctest\\([0-9]+\\) malloc: [^
]*
)*Memory checking results:
((ctest\\([0-9]+\\) malloc: |BullseyeCoverage|==)[^
]*
)*\$")
add_test(CTestTestMemcheckDummyValgrindTwoTargets "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindTwoTargets/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindTwoTargets/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail" "-VV")
set_tests_properties(CTestTestMemcheckDummyValgrindTwoTargets PROPERTIES  PASS_REGULAR_EXPRESSION "
Memory check project /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindTwoTargets
.*
 *Start 1: RunCMake
(.*
)?Memory check command: .* \"--log-file=/Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindTwoTargets/Testing/Temporary/MemoryChecker.1.log\" \"-q\".*
 *Start 2: RunCMakeAgain
(.*
)?Memory check command: .* \"--log-file=/Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindTwoTargets/Testing/Temporary/MemoryChecker.2.log\" \"-q\".*
")
add_test(CTestTestMemcheckDummyValgrindInvalidSupFile "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindInvalidSupFile/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindInvalidSupFile/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindInvalidSupFile PROPERTIES  PASS_REGULAR_EXPRESSION "
Cannot find memory checker suppression file: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/does-not-exist
")
add_test(CTestTestMemcheckDummyValgrindCustomOptions "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindCustomOptions/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindCustomOptions/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckDummyValgrindCustomOptions PROPERTIES  PASS_REGULAR_EXPRESSION "
Cannot find memory tester output file: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/DummyValgrindCustomOptions/Testing/Temporary/MemoryChecker.1.log
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/DummyValgrindCustomOptions/test.cmake
")
add_test(CTestTestMemcheckNotExist "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NotExist/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/NotExist/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckNotExist PROPERTIES  PASS_REGULAR_EXPRESSION "Memory checker \\(MemoryCheckCommand\\) not set, or cannot find the specified program.")
add_test(CTestTestMemcheckUnknown "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/bin/ctest" "-C" "" "-S" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/Unknown/test.cmake" "-V" "--output-log" "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/Unknown/testOutput.log" "-D" "PSEUDO_BC=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/BC" "-D" "PSEUDO_PURIFY=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/purify" "-D" "PSEUDO_VALGRIND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/valgrind" "-D" "ERROR_COMMAND=/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Tests/CTestTestMemcheck/memcheck_fail")
set_tests_properties(CTestTestMemcheckUnknown PROPERTIES  PASS_REGULAR_EXPRESSION "Do not understand memory checker: /Users/mscho/Simon/Tools/cmake-3.0.0/obj/Bootstrap.cmk/cmake
(.*
)?Error in read script: /Users/mscho/Simon/Tools/cmake-3\\.0\\.0/obj/Tests/CTestTestMemcheck/Unknown/test.cmake
")
subdirs(NoLogDummyChecker)
