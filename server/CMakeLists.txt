# $Id$
# Description: CMake build information for the server program
# (C) 2011 Cyberpad Technologies

CMAKE_MINIMUM_REQUIRED( VERSION 2.8 )
SET( PROJECT_NAME Reroot )
PROJECT( ${PROJECT_NAME} )

SET( CMAKE_BUILD_TYPE Release )
#SET( CMAKE_CXX_FLAGS "-Wall" )

# QT4 Handling
FIND_PACKAGE( Qt4 REQUIRED )

SET( QT_USE_QTNETWORK TRUE )

INCLUDE( ${QT_USE_FILE} )

IF( NOT CMAKE_BUILD_TYPE )
  SET( CMAKE_BUILD_TYPE Debug CACHE STRING
      "Choose the type of build, options are: None Debug Release RelWithDebInfo MinSizeRel."
      FORCE )
ENDIF( NOT CMAKE_BUILD_TYPE )

SET( QT_SOURCES
	src/MainWindow.h
)
SET( QT_UIS
)
SET( QT_RESOURCES
	res/RerootResources.qrc
)

QT4_WRAP_CPP( MOC_CPP ${QT_SOURCES} )
QT4_WRAP_UI( UI_CPP ${QT_UIS} )
QT4_ADD_RESOURCES( RES_H ${QT_RESOURCES} )

INCLUDE_DIRECTORIES(
	.
	./src
	./src/OSCms
	./src/qrencode
)

# OSCms sources
SET( OSC_CPP
	src/OSCms/OSCBundle.cpp
	src/OSCms/OSCByteArrayToMsgConverter.cpp
	src/OSCms/OSCMessage.cpp
	src/OSCms/OSCMsgToByteArrayConverter.cpp
	src/OSCms/OSCPacket.cpp
	src/OSCms/OSCPacketDispatcher.cpp
	src/OSCms/OSCPort.cpp
)

# qrencode sources
SET( QR_CPP
	src/qrencode/bitstream.c
	src/qrencode/mask.c
	src/qrencode/mmask.c
	src/qrencode/mqrspec.c
	src/qrencode/qrencode.c
	src/qrencode/qrinput.c
	src/qrencode/qrspec.c
	src/qrencode/rscode.c
	src/qrencode/split.c
)


# General
SET( SOURCES
	src/main.cpp
	src/MainWindow.cpp
	src/MouseListener.cpp
	src/KeyboardListener.cpp
	src/Connector.cpp
	src/CipheredListener.cpp
	src/Cipher.cpp
	src/HandshakeListener.cpp
	src/Mouse.cpp
	src/Keyboard.cpp
    src/Context.cpp
	${OSC_CPP}
	${QR_CPP}
	${MOC_CPP}
	${UI_CPP}
	${RES_H}
)

SET( LIBRARIES
	${QT_LIBRARIES}
	${QT_QTNETWORK_LIBRARY}
)

# We need to use the special winmain symbol...
IF (WIN32)
    SET (QT_USE_QTMAIN TRUE)
    LIST(APPEND LIBRARIES
        psapi
    )
ENDIF (WIN32)

ADD_EXECUTABLE( ${PROJECT_NAME} ${SOURCES} )
TARGET_LINK_LIBRARIES( ${PROJECT_NAME} ${LIBRARIES} )
INSTALL_TARGETS( /bin ${PROJECT_NAME} )

# Fixup the bundle
#SET(plugin_dest_dir bin)
#SET(qtconf_dest_dir bin)
#SET(APPS "\${CMAKE_INSTALL_PREFIX}/bin/QtTest")
#IF(APPLE)
#  SET(plugin_dest_dir QtTest.app/Contents/MacOS)
#  SET(qtconf_dest_dir QtTest.app/Contents/Resources)
#  SET(APPS "\${CMAKE_INSTALL_PREFIX}/QtTest.app")
#ENDIF(APPLE)
#IF(WIN32)
#  SET(APPS "\${CMAKE_INSTALL_PREFIX}/bin/QtTest.exe")
#ENDIF(WIN32)

#--------------------------------------------------------------------------------
# Install the ${PROJECT_NAME}application, on Apple, the bundle is at the root of the
# install tree, and on other platforms it'll go into the bin directory.
#INSTALL(TARGETS ${PROJECT_NAME}
#    BUNDLE DESTINATION . COMPONENT Runtime
#    RUNTIME DESTINATION bin COMPONENT Runtime
#    )

#--------------------------------------------------------------------------------
# Install needed Qt plugins by copying directories from the qt installation
# One can cull what gets copied by using 'REGEX "..." EXCLUDE'
#INSTALL(DIRECTORY "${QT_PLUGINS_DIR}/imageformats" DESTINATION ${plugin_dest_dir}/plugins COMPONENT Runtime)

#--------------------------------------------------------------------------------
# install a qt.conf file
# this inserts some cmake code into the install script to write the file
#INSTALL(CODE "
#    file(WRITE \"\${CMAKE_INSTALL_PREFIX}/${qtconf_dest_dir}/qt.conf\" \"\")
#    " COMPONENT Runtime)


#--------------------------------------------------------------------------------
# Use BundleUtilities to get all other dependencies for the application to work.
# It takes a bundle or executable along with possible plugins and inspects it
# for dependencies.  If they are not system dependencies, they are copied.

# directories to look for dependencies
#SET(DIRS ${QT_LIBRARY_DIRS})

# Now the work of copying dependencies into the bundle/package
# The quotes are escaped and variables to use at install time have their $ escaped
# An alternative is the do a configure_file() on a script and use install(SCRIPT  ...).
# Note that the image plugins depend on QtSvg and QtXml, and it got those copied
# over.
#INSTALL(CODE "
#    file(GLOB_RECURSE QTPLUGINS
#      \"\${CMAKE_INSTALL_PREFIX}/${plugin_dest_dir}/plugins/*${CMAKE_SHARED_LIBRARY_SUFFIX}\")
#    include(BundleUtilities)
#    fixup_bundle(\"${APPS}\" \"\${QTPLUGINS}\" \"${DIRS}\")
#    " COMPONENT Runtime)


# To Create a package, one can run "cpack -G DragNDrop CPackConfig.cmake" on Mac OS X
# where CPackConfig.cmake is created by including CPack
# And then there's ways to customize this as well
#set(CPACK_BINARY_DRAGNDROP ON)

