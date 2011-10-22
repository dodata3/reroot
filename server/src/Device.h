// $Id$
// Description: Server/Client Device model for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef DEVICE_H_
#define DEVICE_H_

#include <QString>
#include <QObject>
#include <QMap>
#include <QMutex>

#include "OSCPort.h"

#include "Global.h"
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
    QHostAddress mAddress; // Network address
    OSCPort* mPort;
    QString mName;    // Reference name (for viewing and differentiating connected devices, etc.)
public:
    Device( const QString& name, const QHostAddress& address, OSCPort* port );
    ~Device();

    Device& SetName(const QString& inString);
    QString Name() const;
    QHostAddress Address() const;
};

class DeviceClient;

class DeviceServer : public Device
{
    // static
private:
    static const QString sDefaultName;

    // Not const because OSCListener takes a non-const reference for some reason
    static QString sMouseAddress;
    static QString sKeyboardAddress;
    static QString sJoystickAddress;
    static QString sHandshakeAddress;

    // non-static
private:

    ID mIdNext; // Next id to assign a client device

    mutable QMutex mClientsLock;
    QMap<QString, DeviceClient*> mClients;
    mutable QMutex mClientsIDLock;
    QMap<ID, DeviceClient*> mClientsID;

	// Listeners
	MouseListener mMouse;
	//KeyboardListener mKeyboard;
	//JoystickListener mJoystick;
	HandshakeListener mHandshake;

public:
    DeviceServer();
    ~DeviceServer();

    void generateQR() const;

    ID addClient(QHostAddress& address, SecretKey key);
    bool removeClient(ID id);
    bool removeClient(QString ip);
    void removeAll();

    SecretKey ClientKey(QString ip) const;
    SecretKey ClientKey(ID id) const;

    // Access connected clients
    const DeviceClient& client(ID id) const;
    DeviceClient& client(ID id);
    const DeviceClient& client(QString ip) const;
    DeviceClient& client(QString ip);
};

class DeviceClient : public Device
{

    // static
private:
    static const QString sDefaultName;
    static unsigned int sDefaultNameNumber; // Number to append to default device name

    // non-static
private:
    const ID mId;
    SecretKey mKey;

public:
    DeviceClient( const QString& name, QHostAddress& address, SecretKey key, ID id );
    DeviceClient( QHostAddress& address, SecretKey key, ID id ); // Default name constructor
    ~DeviceClient();

    //bool send( Message const & message ) const;
    //Message const * receive() const;

    ID ConnectionID() const;
    SecretKey Key() const;
};

#endif // DEVICE_H

