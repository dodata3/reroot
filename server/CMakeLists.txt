# $Id$
# Description: CMake build information for the server program
# (C) 2011 Cyberpad Technologies

CMAKE_MINIMUM_REQUIRED( VERSION 2.6 )
PROJECT( RerootServer )

SET( CMAKE_BUILD_TYPE Release )
SET( CMAKE_CXX_FLAGS "-Wall" )

# QT4 Handling
FIND_PACKAGE( Qt4 REQUIRED )

SET( QT_USE_QTNETWORK TRUE )

INCLUDE( ${QT_USE_FILE} )

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


# General
SET( SOURCES
	src/main.cpp
	src/MainWindow.cpp
	${OSC_CPP}
	${MOC_CPP}
	${UI_CPP}
	${RES_H}
)

SET( LIBRARIES
	${QT_LIBRARIES}
)

ADD_EXECUTABLE( RerootServer ${SOURCES} )
TARGET_LINK_LIBRARIES( RerootServer ${LIBRARIES} ${QT_QTNETWORK_LIBRARY} )

INSTALL_TARGETS( /bin RerootServer )
