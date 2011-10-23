#include "Device.h"

Device::Device(const QString& name, const QHostAddress& address, OSCPort* port ) : mAddress(address), mPort(port), mName(name)
{

}

Device::~Device()
{
    delete mPort;
    mPort = NULL;
}

const QString DeviceServer::sDefaultName(QObject::tr("Server"));

QString DeviceServer::sMouseAddress("/mouse");
QString DeviceServer::sKeyboardAddress("/keyboard");
QString DeviceServer::sJoystickAddress("/joystick");
QString DeviceServer::sHandshakeAddress("/handshake");

//DeviceServer::DeviceServer() : Device(sDefaultName, QHostAddress::Any, new OSCPort(mAddress, (const qint16)(sDefaultPort))), mIdNext(1),
DeviceServer::DeviceServer() : Device(sDefaultName, QHostAddress::Any, NULL), mIdNext(1),
mMouse(this),
//mKeyboard(this),
//mJoystick(this),
mHandshake()
{
    // Initialize only once we have a mAddress initialized
    mPort = new OSCPort(mAddress, (const qint16)(sDefaultPort));

	mPort->addListener(sMouseAddress,       mMouse);
	//mPort->addListener(sKeyboardAddress,  mKeyboard);
	//mPort->addListener(sJoystickAddress,  mJoystick);
	mPort->addListener(sHandshakeAddress,   mHandshake);
	mPort->startListening();
}

DeviceServer::~DeviceServer()
{
    mPort->stopListening();
	removeAll();
}

Device& Device::SetName(const QString& inString)
{
    mName = inString;

    return *this;
}

QString Device::Name() const
{
    return mName;
}

QHostAddress Device::Address() const
{
    return mAddress;
}

Device::ID DeviceServer::addClient(QHostAddress& address, SecretKey key)
{
    DeviceClient* newclient = new DeviceClient(address, key, mIdNext);
    mClientsLock.lock();
    mClients.insert(address.toString(), newclient);
    mClientsLock.unlock();

    mClientsIDLock.lock();
    mClientsID.insert(mIdNext, newclient);
    mClientsIDLock.unlock();

    ID addedID = mIdNext++;

    return addedID;
}

bool DeviceServer::removeClient(QString ip)
{
    mClientsLock.lock();

    QMap<QString, DeviceClient*>::iterator i = mClients.find(ip);
    if (i != mClients.end())
    {
        mClientsIDLock.lock();
        mClients.remove(i.value()->Address().toString());
        mClientsIDLock.unlock();
        delete i.value();
        mClients.remove(ip);

        mClientsLock.unlock();

        return true;
    }

    mClientsLock.unlock();

    return false;
}

bool DeviceServer::removeClient(ID id)
{
    mClientsIDLock.lock();

    QMap<ID, DeviceClient*>::iterator i = mClientsID.find(id);
    if (i != mClientsID.end())
    {
        mClientsLock.lock();
        mClients.remove(i.value()->Name());
        mClientsLock.unlock();
        delete i.value();
        mClientsID.remove(id);

        mClientsIDLock.unlock();

        return true;
    }

    mClientsIDLock.unlock();

    return false;
}

void DeviceServer::removeAll()
{
    mClientsLock.lock();
    for (QMap<QString, DeviceClient*>::iterator i = mClients.begin(); i != mClients.end(); ++i)
    {
        delete i.value();
    }
    mClients.clear();
    mClientsLock.unlock();

    mClientsIDLock.lock();
    mClientsID.clear();
    mClientsIDLock.unlock();
}

// Currently will dereference end iterator if given an invalid id
// Will need to either throw an exception or a 'not-found' DeviceClient object
const DeviceClient& DeviceServer::client(ID id) const
{
    mClientsIDLock.lock();
    const DeviceClient& found(**mClientsID.constFind(id)); // Double dereferencing (iterator -> pointer -> value)
    mClientsIDLock.unlock();
    return found;
}
DeviceClient& DeviceServer::client(ID id)
{
    mClientsIDLock.lock();
    DeviceClient& found(**mClientsID.find(id));
    mClientsIDLock.unlock();
    return found;
}

const DeviceClient& DeviceServer::client(QString ip) const
{
    mClientsLock.lock();
    const DeviceClient& found(**mClients.constFind(ip));
    mClientsLock.unlock();
    return found;
}
DeviceClient& DeviceServer::client(QString ip)
{
    mClientsLock.lock();
    DeviceClient& found(**mClients.find(ip));
    mClientsLock.unlock();
    return found;
}

SecretKey DeviceServer::ClientKey(QString ip) const
{
    return client(ip).Key();
}
SecretKey DeviceServer::ClientKey(ID id) const
{
    return client(id).Key();
}


const QString DeviceClient::sDefaultName(QObject::tr("Device "));
unsigned int DeviceClient::sDefaultNameNumber = 0;

DeviceClient::DeviceClient(const QString& name, QHostAddress& address, SecretKey key, ID id) : Device(name, address, new OSCPort( address, sDefaultPort )), mId(id), mKey(key)
{

}

DeviceClient::DeviceClient(QHostAddress& address, SecretKey key, ID id) : Device(QString(sDefaultName).append(QString().number(++sDefaultNameNumber)), address, new OSCPort( address, sDefaultPort )), mId(id), mKey(key)
{
    // Create a DeviceClient with a default name, incrementing the number count
}

DeviceClient::~DeviceClient()
{

}

/*
bool DeviceClient::send(Message const * message) const{
    OSCMessage omessage(mAddress, message->serialize());


}

Message const * DeviceClient::receive() const{

}
*/

Device::ID DeviceClient::ConnectionID() const
{
    return mId;
}

SecretKey DeviceClient::Key() const
{
    return mKey;
}
