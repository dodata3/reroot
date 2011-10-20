#include "Device.h"

Device::Device(const QString& name, const QString& address) : mName(name), mAddress(address)
{

}

const QString DeviceServer::mDefaultName(tr("Server"));

DeviceServer::DeviceServer() : Device(mDefaultName, QHostAddress::Any), mIdNext(1), mPort(new OSCPort(mAddress, mDefaultPort))
{

}

DeviceServer::~DeviceServer()
{
	mPort->stopListening();
    delete mPort;
    mPort = NULL;
	removeAll();
}

type_id DeviceServer::addClient(QHostAddress& address, SecretKey key)
{
    mClientsLock.lock();
    mClients.insert(mIdNext, DeviceClient(address, key, mIdNext));
    type_id addedID = mIdNext++;
    mClientsLock.unlock();

    return addedID;
}

bool DeviceServer::removeClient(type_id id)
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

const QString DeviceClient::mDefaultName(tr("Device "));

DeviceClient::DeviceClient(const QString& name, const QHostAddress& address, SecretKey key, id_type id) : Device(name, address, new OSCPort( inRemote, mDefaultPort )), mId(id), mKey(key)
{

}

DeviceClient::DeviceClient(const QHostAddress& address, SecretKey key, id_type id) : Device(QString(mDefaultName).append(QString().number(++mDefaultNameNumber)), address, new OSCPort( inRemote, mDefaultPort )), mId(id), mKey(key)
{
    // Create a DeviceClient with a default name, incrementing the number count
}

bool DeviceClient::send(Message const * message) const{
    OSCMessage omessage(mAddress, message->serialize());


}

Message const * DeviceClient::receive() const{

}



