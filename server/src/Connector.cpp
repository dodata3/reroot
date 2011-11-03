// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#include "Global.h"
#include "Connector.h"
#include "Cipher.h"

#include <cryptopp/integer.h>
using CryptoPP::Integer;
using CryptoPP::RSA;

// Listen in *nix using
// sudo tcpdump -s 0 -pnli wlan0 proto UDP and port 57110

Connector::Connector() :
	mListenerAddress( QHostAddress::Any ),
	mControl( this )
{
    // Generate keys
    Cipher::GenerateKeypair( mPublicEncKey, mPrivateEncKey );
    Cipher::GenerateKeypair( mPublicSignKey, mPrivateSignKey );
	mIncomingPort = new OSCPort( mListenerAddress, REROOT_PORT );
	QString controlAddress = QString( "/control" );
	mIncomingPort->addListener( controlAddress, mControl );
	mIncomingPort->startListening();
}

Connector::~Connector()
{
	mIncomingPort->stopListening();
	mIncomingPort->close();
	mIncomingPort->terminate();
	if( !mIncomingPort->wait() )
        qWarning( "Incoming port thread termination timeout." );
	delete mIncomingPort;
	mIncomingPort = NULL;
	RemoveAllDevices();
}

void Connector::AddNewDevice( QHostAddress& inRemote, QByteArray inMod, QByteArray inEncExp, QByteArray inSignExp )
{
	Device dev;
	dev.port = new OSCPort( inRemote, REROOT_PORT );
	dev.encKey.SetModulus( Integer( reinterpret_cast< byte* >( inMod.data() ), inMod.size(), Integer::UNSIGNED ) );
	dev.encKey.SetPublicExponent( Integer( reinterpret_cast< byte* >( inEncExp.data() ), inEncExp.size(), Integer::UNSIGNED ) );
	dev.signKey.SetModulus( Integer( reinterpret_cast< byte* >( inMod.data() ), inMod.size(), Integer::UNSIGNED ) );
	dev.signKey.SetPublicExponent( Integer( reinterpret_cast< byte* >( inSignExp.data() ), inSignExp.size(), Integer::UNSIGNED ) );
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

RSA::PublicKey Connector::GetClientEncKey( QHostAddress& address )
{
    RSA::PublicKey key;
	mLock.lock();
	if( mDeviceMap.contains( address.toString() ) )
        key = mDeviceMap[ address.toString() ].encKey;
	mLock.unlock();
	return key;
}

RSA::PublicKey Connector::GetClientSignKey( QHostAddress& address )
{
    RSA::PublicKey key;
	mLock.lock();
	if( mDeviceMap.contains( address.toString() ) )
        key = mDeviceMap[ address.toString() ].signKey;
	mLock.unlock();
	return key;
}

OSCPort* Connector::GetClientPort( QHostAddress& address )
{
    OSCPort* port = NULL;
	mLock.lock();
	if( mDeviceMap.contains( address.toString() ) )
        port = mDeviceMap[ address.toString() ].port;
	mLock.unlock();
	return port;
}
