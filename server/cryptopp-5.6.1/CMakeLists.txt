# Copyright (c) 2010, 2011 Sergiu Dotenco
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

CMAKE_MINIMUM_REQUIRED (VERSION 2.8)
PROJECT (cryptopp)

OPTION (CRYPTOPP_FIPS_COMPLIANT "CryptoPP FIPS 140 compliant" OFF)
OPTION (CRYPTOPP_BUILD_TESTS "Build CryptoPP tests" ON)
OPTION (CRYPTOPP_BUILD_STATIC_LIBS "Build static CryptoPP libraries" ON)
OPTION (CRYPTOPP_BUILD_SHARED_LIBS "Build shared CryptoPP libraries" ON)
OPTION (CRYPTOPP_INSTALL "Create CryptoPP install target" ON)

IF (NOT CRYPTOPP_BUILD_STATIC_LIBS AND CRYPTOPP_BUILD_TESTS)
  SET (CRYPTOPP_BUILD_STATIC_LIBS ON CACHE BOOL "" FORCE)
ENDIF (NOT CRYPTOPP_BUILD_STATIC_LIBS AND CRYPTOPP_BUILD_TESTS)

IF (NOT CRYPTOPP_BUILD_SHARED_LIBS AND CRYPTOPP_BUILD_TESTS)
  SET (CRYPTOPP_BUILD_SHARED_LIBS ON CACHE BOOL "" FORCE)
ENDIF (NOT CRYPTOPP_BUILD_SHARED_LIBS AND CRYPTOPP_BUILD_TESTS)

FILE (GLOB PROJECT_CPP *.cpp)
FILE (GLOB PROJECT_H *.h)
FILE (GLOB PROJECT_PCH pch.h)

SET (PROJECT_TEST bench bench2 test validat1 validat2 validat3 adhoc datatest
  regtest fipsalgt fipstest)

IF (WIN32)
  LIST (APPEND PROJECT_TEST dlltest)
ENDIF (WIN32)

SET (PROJECT_TEST_CPP)

FOREACH (_PROJECT_TEST_FILE ${PROJECT_TEST})
  FOREACH (_PROJECT_FILE ${PROJECT_CPP})
    IF (${_PROJECT_FILE} MATCHES ".*${_PROJECT_TEST_FILE}\\.cpp$")
      LIST (REMOVE_ITEM PROJECT_CPP ${_PROJECT_FILE})
    ENDIF (${_PROJECT_FILE} MATCHES ".*${_PROJECT_TEST_FILE}\\.cpp$")
  ENDFOREACH (_PROJECT_FILE)
  LIST (APPEND PROJECT_TEST_CPP ${_PROJECT_TEST_FILE}.cpp)
ENDFOREACH(_PROJECT_TEST_FILE)

SET (PROJECT_CPP_LIB ${PROJECT_CPP})
SET (PROJECT_CPP_SHARED ${PROJECT_CPP})

LIST (APPEND PROJECT_CPP_SHARED ${CMAKE_CURRENT_SOURCE_DIR}/fipstest.cpp)
LIST (REMOVE_ITEM PROJECT_CPP_SHARED ${CMAKE_CURRENT_SOURCE_DIR}/ttmac.cpp)

FOREACH (_PROJECT_FILE ${PROJECT_CPP_LIB})
  IF (${_PROJECT_FILE} MATCHES ".*dll.+\\.cpp$")
    LIST (REMOVE_ITEM PROJECT_CPP_LIB ${_PROJECT_FILE})
  ENDIF (${_PROJECT_FILE} MATCHES ".*dll.+\\.cpp$")
ENDFOREACH (_PROJECT_FILE)

# Intel compiler
IF (CMAKE_CXX_COMPILER MATCHES icl)
  ADD_DEFINITIONS (-wd811)
  # Variable of static storage duration was declared but never referenced
  ADD_DEFINITIONS (-wd2415)
  # Function was declared but never referenced
  ADD_DEFINITIONS (-wd177)
ENDIF (CMAKE_CXX_COMPILER MATCHES icl)

ADD_DEFINITIONS (-DCRYPTOPP_DISABLE_ASM)
REMOVE_DEFINITIONS (-DUNICODE -D_UNICODE)

IF (MINGW)
  SET (CMAKE_RC_COMPILER_INIT windres)
  ENABLE_LANGUAGE (RC)
  SET (CMAKE_RC_COMPILE_OBJECT "-O coff -o <OBJECT> <SOURCE>")
ENDIF (MINGW)

IF (WIN32)
  LIST (APPEND PROJECT_CPP_SHARED cryptopp.rc)
ENDIF (WIN32)

IF (CRYPTOPP_BUILD_SHARED_LIBS)
  ADD_LIBRARY (cryptopp SHARED ${PROJECT_CPP_SHARED} ${PROJECT_H}
    ${PROJECT_PCH})
ENDIF (CRYPTOPP_BUILD_SHARED_LIBS)

IF (CRYPTOPP_BUILD_STATIC_LIBS)
  ADD_LIBRARY (cryptlib STATIC ${PROJECT_CPP_LIB} ${PROJECT_H} ${PROJECT_PCH})
ENDIF (CRYPTOPP_BUILD_STATIC_LIBS)

IF (CRYPTOPP_BUILD_TESTS)
  ADD_EXECUTABLE (crypttest ${PROJECT_TEST_CPP})

  IF (CMAKE_COMPILER_IS_GNUCXX)
    SET_TARGET_PROPERTIES (crypttest PROPERTIES LINK_FLAGS -pthread)
  ENDIF (CMAKE_COMPILER_IS_GNUCXX)

  IF (WIN32)
    ADD_EXECUTABLE (dlltest dlltest.cpp)
  ENDIF (WIN32)
ENDIF (CRYPTOPP_BUILD_TESTS)

SET (_CRYPTOPP_COMPILE_DEFINITIONS USE_PRECOMPILED_HEADERS)

IF (CRYPTOPP_FIPS_COMPLIANT)
  LIST (APPEND _CRYPTOPP_COMPILE_DEFINITIONS
    CRYPTOPP_ENABLE_COMPLIANCE_WITH_FIPS_140_2=1)
ELSE (CRYPTOPP_FIPS_COMPLIANT)
  LIST (APPEND _CRYPTOPP_COMPILE_DEFINITIONS
    CRYPTOPP_ENABLE_COMPLIANCE_WITH_FIPS_140_2=0)
ENDIF (CRYPTOPP_FIPS_COMPLIANT)

IF (CRYPTOPP_BUILD_STATIC_LIBS)
  LIST (APPEND _CRYPTOPP_INSTALL_TARGETS cryptlib)
ENDIF (CRYPTOPP_BUILD_STATIC_LIBS)

IF (CRYPTOPP_BUILD_SHARED_LIBS)
  LIST (APPEND _CRYPTOPP_INSTALL_TARGETS cryptopp)
ENDIF (CRYPTOPP_BUILD_SHARED_LIBS)

IF (MSVC)
  SET_TARGET_PROPERTIES (${_CRYPTOPP_INSTALL_TARGETS} PROPERTIES
    COMPILE_DEFINITIONS "${_CRYPTOPP_COMPILE_DEFINITIONS}")
  # Setup precompiled headers
  SET_TARGET_PROPERTIES (${_CRYPTOPP_INSTALL_TARGETS} PROPERTIES COMPILE_FLAGS
    -Yupch.h)
  SET_SOURCE_FILES_PROPERTIES (pch.cpp PROPERTIES COMPILE_FLAGS -Yc)
  # These files don't use precompiled headers
  SET_SOURCE_FILES_PROPERTIES (dll.cpp cryptlib_bds.cpp iterhash.cpp
    PROPERTIES COMPILE_FLAGS -Y-)
ENDIF (MSVC)

IF (_CRYPTOPP_INSTALL_TARGETS AND WIN32)
  SET_TARGET_PROPERTIES (${_CRYPTOPP_INSTALL_TARGETS} PROPERTIES DEFINE_SYMBOL
    CRYPTOPP_EXPORTS)
ENDIF (_CRYPTOPP_INSTALL_TARGETS AND WIN32)

IF (TARGET crypttest)
  TARGET_LINK_LIBRARIES (crypttest cryptlib cryptopp)
ENDIF (TARGET crypttest)

IF (TARGET dlltest)
  TARGET_LINK_LIBRARIES (dlltest cryptopp)
ENDIF (TARGET dlltest)

IF (WIN32)
  GET_TARGET_PROPERTY (_TEMP cryptopp COMPILE_DEFINITIONS)

  IF (NOT _TEMP)
    UNSET (_TEMP)
  ENDIF (NOT _TEMP)

  LIST (APPEND _TEMP _USRDLL)

  IF (TARGET cryptopp)
    SET_TARGET_PROPERTIES (cryptopp PROPERTIES COMPILE_DEFINITIONS "${_TEMP}")
    SET_TARGET_PROPERTIES (cryptopp PROPERTIES DEBUG_POSTFIX d)
  ENDIF (TARGET cryptopp)

  IF (TARGET cryptlib)
    SET_TARGET_PROPERTIES (cryptlib PROPERTIES DEBUG_POSTFIX d)
  ENDIF (TARGET cryptlib)

  IF (CRYPTOPP_BUILD_TESTS)
    SET_TARGET_PROPERTIES (crypttest PROPERTIES COMPILE_DEFINITIONS _CONSOLE)

    SET (_TEMP CRYPTOPP_DLL_ONLY _CONSOLE)
    SET_TARGET_PROPERTIES (dlltest PROPERTIES COMPILE_DEFINITIONS
      "${_TEMP};CRYPTOPP_NO_LIB")
  ENDIF (CRYPTOPP_BUILD_TESTS)
ENDIF (WIN32)

IF (TARGET cryptopp)
  SET_TARGET_PROPERTIES (cryptopp PROPERTIES PUBLIC_HEADER "${PROJECT_H}")
ENDIF (TARGET cryptopp)

IF (TARGET cryptlib)
  SET_TARGET_PROPERTIES (cryptlib PROPERTIES PUBLIC_HEADER "${PROJECT_H}")
ENDIF (TARGET cryptlib)

IF (WIN32)
  IF (TARGET cryptopp)
    TARGET_LINK_LIBRARIES (cryptopp Ws2_32)
  ENDIF (TARGET cryptopp)

  IF (TARGET cryptlib)
    TARGET_LINK_LIBRARIES (cryptlib Ws2_32)
  ENDIF (TARGET cryptlib)
ENDIF (WIN32)

IF (CRYPTOPP_INSTALL AND _CRYPTOPP_INSTALL_TARGETS)
  INSTALL (TARGETS ${_CRYPTOPP_INSTALL_TARGETS}
    RUNTIME DESTINATION bin
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
    PUBLIC_HEADER DESTINATION include/cryptopp)
ENDIF (CRYPTOPP_INSTALL AND _CRYPTOPP_INSTALL_TARGETS)

IF (TARGET crypttest)
  ENABLE_TESTING ()
  # Run validation tests
  ADD_TEST (NAME cryptopp-validation COMMAND crypttest v WORKING_DIRECTORY
    ${CMAKE_CURRENT_SOURCE_DIR})
ENDIF (TARGET crypttest)

# Intel compiler, Windows
IF (CMAKE_CXX_COMPILER MATCHES icl)
  SET_TARGET_PROPERTIES (${_CRYPTOPP_INSTALL_TARGETS} PROPERTIES COMPILE_FLAGS
    /Qopenmp)
# Intel compiler, non-Windows
ELSEIF (CMAKE_CXX_COMPILER MATCHES icc)
  SET_TARGET_PROPERTIES (${_CRYPTOPP_INSTALL_TARGETS} PROPERTIES COMPILE_FLAGS
    -fopenmp)
ENDIF (CMAKE_CXX_COMPILER MATCHES icl)

IF (TARGET crypttest)
  GET_TARGET_PROPERTY (crypttest RUNTIME_OUTPUT_DIRECTORY _DATA_DIR)

  IF (NOT _DATA_DIR)
    SET (_DATA_DIR ${CMAKE_RUNTIME_OUTPUT_DIRECTORY})
  ENDIF (NOT _DATA_DIR)

  SET (_DATA_DIR ${_DATA_DIR}/${CMAKE_CFG_INTDIR})

  ADD_CUSTOM_COMMAND (TARGET crypttest POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
            ${CMAKE_CURRENT_SOURCE_DIR}/TestData ${_DATA_DIR}/TestData
    COMMAND ${CMAKE_COMMAND} -E copy_directory
            ${CMAKE_CURRENT_SOURCE_DIR}/TestVectors ${_DATA_DIR}/TestVectors
    COMMENT "Copying cryptopp test data")
ENDIF (TARGET crypttest)