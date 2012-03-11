// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#include "Global.h"
#include "Connector.h"
#include "Cipher.h"

#include <sstream>
#include <cryptopp/integer.h>
#include <cryptopp/hex.h>

using namespace CryptoPP;
using namespace std;

// Listen in *nix using
// sudo tcpdump -s 0 -pnli wlan0 proto UDP and port 57110

Connector::Connector() :
	mListenerAddress( QHostAddress::Any ),
	mControl( this ),
	mHandshake( this ),
	mConnectKey( 0 )
{
    // Generate keys
    Cipher::GenerateKeypair( mPublicEncKey, mPrivateEncKey );
    Cipher::GenerateKeypair( mPublicSignKey, mPrivateSignKey );
	mpIncomingPort = new OSCPort( mListenerAddress, REROOT_SERVER_PORT );
	QString controlAddress = QString( "/control" );
	mpIncomingPort->addListener( controlAddress, mControl );
	QString handshakeAddress = QString( "/handshake_client" );
	mpIncomingPort->addListener( handshakeAddress, mHandshake );
	mpIncomingPort->startListening();
}

Connector::~Connector()
{
	mpIncomingPort->stopListening();
	mpIncomingPort->close();
	mpIncomingPort->terminate();
	if( !mpIncomingPort->wait() )
        qWarning( "Incoming port thread termination timeout." );
	delete mpIncomingPort;
	mpIncomingPort = NULL;
	RemoveAllDevices();
}

void Connector::AddNewDevice( QHostAddress& inRemote, QByteArray inEncMod, QByteArray inEncExp, QByteArray inSignMod, QByteArray inSignExp )
{
	Device dev;
	dev.port = new OSCPort( inRemote, REROOT_CLIENT_PORT );
	dev.encKey.SetModulus( Integer( reinterpret_cast< byte* >( inEncMod.data() ), inEncMod.size(), Integer::UNSIGNED ) );
	dev.encKey.SetPublicExponent( Integer( reinterpret_cast< byte* >( inEncExp.data() ), inEncExp.size(), Integer::UNSIGNED ) );
	dev.signKey.SetModulus( Integer( reinterpret_cast< byte* >( inSignMod.data() ), inSignMod.size(), Integer::UNSIGNED ) );
	dev.signKey.SetPublicExponent( Integer( reinterpret_cast< byte* >( inSignExp.data() ), inSignExp.size(), Integer::UNSIGNED ) );
	mLock.lock();
	mDeviceMap[ inRemote.toString() ] = dev;
	mLock.unlock();
	SendHandshake( inRemote.toString() );
	qDebug() << "Added new device: " << inRemote.toString() << " to list of allowed devices";
	emit HandshakeSuccessful( inRemote.toString() );

}

void Connector::SendHandshake( QString inDeviceName )
{
    QList< QVariant > args;
    args << IntegerToHexString( mPublicEncKey.GetModulus() );
    args << IntegerToHexString( mPublicEncKey.GetPublicExponent() );
    args << IntegerToHexString( mPublicSignKey.GetModulus() );
    args << IntegerToHexString( mPublicSignKey.GetPublicExponent() );

    qDebug() << "Handshake: " << args;

    QString address = QString( "/handshake_server" );
    OSCMessage handshake( address, args );

    mLock.lock();
    mDeviceMap[ inDeviceName ].port->send( handshake );
    mLock.unlock();
}

void Connector::SetConnectKey( qint32 key )
{
    mLock.lock();
    mConnectKey = key;
    mLock.unlock();
}

quint32 Connector::GetConnectKey()
{
    quint32 key = 0;
    mLock.lock();
    key = mConnectKey;
    mLock.unlock();
    return key;
}

void Connector::RemoveDevice( QString& name )
{
	mLock.lock();
	CloseOSCPort( mDeviceMap[ name ].port );
	delete mDeviceMap[ name ].port;
	mDeviceMap.remove( name );
	mLock.unlock();
}

void Connector::CloseOSCPort( OSCPort* port )
{
  port->stopListening();
  port->terminate();
  if( !port->wait() )
        qWarning( "Incoming port thread termination timeout." );
  port->close();
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

QString Connector::IntegerToHexString( Integer integer )
{
    string s;
    byte* buffer = new byte[ 1024 ];
    for( unsigned int i = 0; i < integer.ByteCount(); i++ )
        buffer[ integer.ByteCount() - 1 - i ] = integer.GetByte( i );
    buffer[ integer.ByteCount() ] = 0;

    StringSource( buffer, integer.ByteCount(), true,
        new HexEncoder( new StringSink( s ) ) );

    return QString::fromStdString( s );
}
