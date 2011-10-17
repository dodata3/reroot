// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#include "Global.h"
#include "Connector.h"

// Listen in unix using
// sudo tcpdump -s 0 -pnli wlan0 proto UDP and port 57110

Connector::Connector() :
	mListenerAddress( QHostAddress::Any ),
	mMouse( this )
{
	mIncomingPort = new OSCPort( mListenerAddress, REROOT_PORT );
	QString mouseAddress = QString( "/mouse" );
	mIncomingPort->addListener( mouseAddress, mMouse );
	mIncomingPort->startListening();
}

Connector::~Connector()
{
	mIncomingPort->stopListening();
	delete mIncomingPort;
	mIncomingPort = NULL;
	RemoveAllDevices();
}

void Connector::AddNewDevice( QHostAddress& inRemote, SecretKey inSecretKey )
{
	Device dev;
	dev.port = new OSCPort( inRemote, REROOT_PORT );
	dev.secretKey = inSecretKey;
	mLock.lock();
	mDeviceMap[ inRemote.toString() ] = dev;
	mLock.unlock();
}

void Connector::RemoveDevice( QHostAddress& inRemote )
{
	mLock.lock();
	delete mDeviceMap[ inRemote.toString() ].port;
	mDeviceMap.remove( inRemote.toString() );
	mLock.unlock();
}

void Connector::RemoveAllDevices()
{
	mLock.lock();
	for( DeviceMap::iterator itr = mDeviceMap.begin(); itr != mDeviceMap.end(); ++itr )
		delete itr->port;
	mDeviceMap.clear();
	mLock.unlock();
}

SecretKey Connector::GetSecretKey( QHostAddress& address )
{
	mLock.lock();
	SecretKey key = mDeviceMap[ address.toString() ].secretKey;
	mLock.unlock();
	return key;
}

OSCPort* Connector::GetPort( QHostAddress& address )
{
	mLock.lock();
	OSCPort* port = mDeviceMap[ address.toString() ].port;
	mLock.unlock();
	return port;
}
