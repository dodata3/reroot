// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#ifndef CONNECTOR_H_
#define CONNECTOR_H_

#include <cryptopp/rsa.h>
#include <QMap>
#include <QMutex>
#include <QByteArray>
#include "Global.h"
#include "OSCPort.h"
#include "ControlListener.h"
#include "HandshakeListener.h"

struct Device
{
	OSCPort* port;
	CryptoPP::RSA::PublicKey encKey;
	CryptoPP::RSA::PublicKey signKey;
};

typedef QMap< QString, Device > DeviceMap;

class Connector
{
public:
	Connector();
	~Connector();

	void AddNewDevice( QHostAddress& inRemote, QByteArray inMod, QByteArray inEncExp, QByteArray inSignExp );
	void RemoveDevice( QHostAddress& inRemote );
	void RemoveAllDevices();

	CryptoPP::RSA::PublicKey GetClientEncKey( QHostAddress& inRemote );
	CryptoPP::RSA::PublicKey GetClientSignKey( QHostAddress& inRemote );
	OSCPort* GetClientPort( QHostAddress& inRemote );

	CryptoPP::RSA::PublicKey PublicEncKey() { return mPublicEncKey; }
	CryptoPP::RSA::PrivateKey PrivateEncKey() { return mPrivateEncKey; }
	CryptoPP::RSA::PublicKey PublicSignKey() { return mPublicSignKey; }
	CryptoPP::RSA::PrivateKey PrivateSignKey() { return mPrivateSignKey; }

private:
	QMutex mLock;
	QHostAddress mListenerAddress;
	OSCPort* mIncomingPort;
	ControlListener mControl;
	DeviceMap mDeviceMap;
	CryptoPP::RSA::PublicKey mPublicEncKey;
	CryptoPP::RSA::PrivateKey mPrivateEncKey;
	CryptoPP::RSA::PublicKey mPublicSignKey;
	CryptoPP::RSA::PrivateKey mPrivateSignKey;
};

#endif // CONNECTOR_H_
