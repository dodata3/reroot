// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#ifndef CONNECTOR_H_
#define CONNECTOR_H_

#include <QObject>
#include <QMap>
#include <QMutex>
#include <QByteArray>
#include <cryptopp/rsa.h>
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

class Connector : public QObject
{
    Q_OBJECT

public:
	Connector();
	~Connector();

	void AddNewDevice( QHostAddress& inRemote, QByteArray inEncMod, QByteArray inEncExp,
                    QByteArray inSignMod, QByteArray inSignExp );
	void RemoveDevice( QHostAddress& inRemote );
	void RemoveAllDevices();
	void SetConnectKey( quint32 key = 0 );
    quint32 GetConnectKey();

	CryptoPP::RSA::PublicKey GetClientEncKey( QHostAddress& inRemote );
	CryptoPP::RSA::PublicKey GetClientSignKey( QHostAddress& inRemote );
	OSCPort* GetClientPort( QHostAddress& inRemote );


	CryptoPP::RSA::PublicKey PublicEncKey() { return mPublicEncKey; }
	CryptoPP::RSA::PrivateKey PrivateEncKey() { return mPrivateEncKey; }
	CryptoPP::RSA::PublicKey PublicSignKey() { return mPublicSignKey; }
	CryptoPP::RSA::PrivateKey PrivateSignKey() { return mPrivateSignKey; }

signals:
	void HandshakeSuccessful( QString name );

private:
	QMutex mLock;
	QHostAddress mListenerAddress;
	OSCPort* mIncomingPort;
	ControlListener mControl;
	HandshakeListener mHandshake;
	DeviceMap mDeviceMap;
	CryptoPP::RSA::PublicKey mPublicEncKey;
	CryptoPP::RSA::PrivateKey mPrivateEncKey;
	CryptoPP::RSA::PublicKey mPublicSignKey;
	CryptoPP::RSA::PrivateKey mPrivateSignKey;
	quint32 mConnectKey;
};

#endif // CONNECTOR_H_
