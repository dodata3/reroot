// $Id$
// Description: Manages connected devices
// (C) Cyberpad Technologies 2011
#ifndef CONNECTOR_H_
#define CONNECTOR_H_

#include <QMap>
#include <QMutex>
#include "Global.h"
#include "OSCPort.h"
#include "MouseListener.h"
#include "HandshakeListener.h"

struct Device
{
	OSCPort* port;
	SecretKey secretKey;
};

typedef QMap< QString, Device > DeviceMap;

class Connector
{
public:
	Connector();
	~Connector();

	void AddNewDevice( QHostAddress& inRemote, SecretKey inSecretKey );
	void RemoveDevice( QHostAddress& inRemote );
	void RemoveAllDevices();

	SecretKey GetSecretKey( QHostAddress& inRemote );
	OSCPort* GetPort( QHostAddress& inRemote );

private:
	QMutex mLock;
	QHostAddress mListenerAddress;
	OSCPort* mIncomingPort;
	MouseListener mMouse;
	HandshakeListener mHandshake;
	DeviceMap mDeviceMap;
};

#endif // CONNECTOR_H_
