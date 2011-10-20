// $Id$
// Description: Server/Client Device model for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef DEVICE_H_
#define DEVICE_H_

#include <QMap>
#include <QMutex>

#include "OSCPort.h"

#include "Global.h"

class Device
{
    // static
protected:
    static const qint16 mDefaultPort = 57110;
public:
    typedef int id_type;

    // non-static
protected:
    const QHostAddress mAddress; // Network address
    OSCPort* mPort;
    QString mName;    // Reference name (for viewing and differentiating connected devices, etc.)
public:
    Device( const QString& name, const QHostAddress& address, OSCPort* port );
};

class DeviceServer
{
    // static
private:
    static const QString mDefaultName;

    // non-static
private:

    id_type mIdNext; // Next id to assign a client device

    QMutex mClientLock;
	QHostAddress mListenerAddress;
	MouseListener mMouse;
	HandshakeListener mHandshake;

	QString mouseAddress = QString( "/mouse" );
	mIncomingPort->addListener( mouseAddress, mMouse );
	mIncomingPort->startListening();

public:
    DeviceServer();
    ~DeviceServer();
    void generateQR() const;
    type_id addClient(QHostAddress& address, SecretKey key);
    bool removeClient(type_id id);
    void removeAll();

    QMap<type_id, DeviceClient> clients;
};

class DeviceClient
{

    // static
private:
    static const QString mDefaultName;
    static unsigned int mDefaultNameNumber = 0; // Number to append to default device name
    // non-static
private:
    const type_id mId;
    SecretKey mKey;

public:
    DeviceClient( const QString& name, const QHostAddress& address, SecretKey key, id_type id );
    DeviceClient( const QHostAddress& address, SecretKey key, id_type id ); // Default name constructor
    ~DeviceClient();

    bool send( Message const & message ) const;
    Message const * receive() const;
};

#endif // DEVICE_H

