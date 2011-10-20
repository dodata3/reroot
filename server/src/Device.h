// $Id$
// Description: Server/Client Device model for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef DEVICE_H_
#define DEVICE_H_

#include <QMap>
#include <QMutex>

#include "OSCPort.h"

#include "Global.h"
#include "Message.h"
#include "HandshakeListener.h"
#include "MouseListener.h"

class Device
{
    // static
protected:
    static const qint16 sDefaultPort = 57110;
public:
    typedef int ID;

    // non-static
protected:
    const QHostAddress mAddress; // Network address
    OSCPort* mPort;
    QString mName;    // Reference name (for viewing and differentiating connected devices, etc.)
public:
    Device( const QString& name, const QHostAddress& address, OSCPort* port );
    ~Device();
};

class DeviceServer
{
    // static
private:
    static const QString sDefaultName;

    static const QString sMouseAddress;
    static const QString sKeyboardAddress;
    static const QString sJoystickAddress;
    static const QString sHandshakeAddress;

    // non-static
private:

    ID mIdNext; // Next id to assign a client device

    QMutex mClientLock;
    QMap<ID, DeviceClient> mClients;

	// Listeners
	MouseListener mMouse;
	HandshakeListener mHandshake;

public:
    DeviceServer();
    ~DeviceServer();

    void generateQR() const;

    ID addClient(QHostAddress& address, SecretKey key);
    bool removeClient(ID id);
    void removeAll();

    const DeviceClient& client(ID id) const;
    DeviceClient& client(ID id);
};

class DeviceClient
{

    // static
private:
    static const QString sDefaultName;
    static unsigned int sDefaultNameNumber = 0; // Number to append to default device name

    // non-static
private:
    const ID mId;
    SecretKey mKey;

public:
    DeviceClient( const QString& name, const QHostAddress& address, SecretKey key, ID id );
    DeviceClient( const QHostAddress& address, SecretKey key, ID id ); // Default name constructor
    ~DeviceClient();

    bool send( Message const & message ) const;
    Message const * receive() const;
};

#endif // DEVICE_H

