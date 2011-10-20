#include "Device.h"

Device::Device(const QString& name, const QString& address) : mName(name), mAddress(address)
{

}

const QString DeviceServer::sDefaultName(tr("Server"));

// I'm assuming these shouldn't be (and don't need to be) localized
const QString DeviceServer::sMouseAddress("/mouse");
const QString DeviceServer::sKeyboardAddress("/keyboard");
const QString DeviceServer::sJoystickAddress("/joystick");
const QString DeviceServer::sHandshakeAddress("/handshake");

DeviceServer::DeviceServer() : Device(sDefaultName, QHostAddress::Any), mIdNext(1), mPort(new OSCPort(mAddress, mDefaultPort))
{
	mPort->addListener(sMouseAddress,       mMouse);
	//mPort->addListener(sKeyboardAddress,  mKeyboard);
	//mPort->addListener(sJoystickAddress,  mJoystick);
	mPort->addListener(sHandshakeAddress,   mHandshake);
	mPort->startListening();
}

DeviceServer::~DeviceServer()
{
	mPort->stopListening();
    delete mPort;
    mPort = NULL;
	removeAll();
}

idType DeviceServer::addClient(QHostAddress& address, SecretKey key)
{
    mClientsLock.lock();
    mClients.insert(mIdNext, DeviceClient(address, key, mIdNext));
    idType addedID = mIdNext++;
    mClientsLock.unlock();

    return addedID;
}

bool DeviceServer::removeClient(idType id)
{
    mClientsLock.lock();
    bool result = mClients.remove(id);
    mClientsLock.unlock();

    return result;
}

void DeviceServer::removeAll()
{
    mClientsLock.lock();
    mClients.clear();
    mClientsLock.unlock();
}

// Currently will dereference end iterator if given an invalid id
// Will need to either throw an exception or a 'not-found' DeviceClient object
const DeviceClient& DeviceServer::client(idType id) const
{
    mClientsLock.lock();
    const DeviceClient& found(*mClients.constFind());
    mClientsLock.unlock();
    return found;
}
DeviceClient& DeviceServer::client(idType id)
{
    mClientsLock.lock();
    DeviceClient& found(*mClients.find());
    mClientsLock.unlock();
    return
}

const QString DeviceClient::sDefaultName(tr("Device "));

DeviceClient::DeviceClient(const QString& name, const QHostAddress& address, SecretKey key, idType id) : Device(name, address, new OSCPort( address, sDefaultPort )), mId(id), mKey(key)
{

}

DeviceClient::DeviceClient(const QHostAddress& address, SecretKey key, idType id) : Device(QString(sDefaultName).append(QString().number(++sDefaultNameNumber)), address, new OSCPort( address, sDefaultPort )), mId(id), mKey(key)
{
    // Create a DeviceClient with a default name, incrementing the number count
}

bool DeviceClient::send(Message const * message) const{
    OSCMessage omessage(mAddress, message->serialize());


}

Message const * DeviceClient::receive() const{

}



