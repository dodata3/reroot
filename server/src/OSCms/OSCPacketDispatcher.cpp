/****************************************************************************
**
**
****************************************************************************/

#include "OSCPacketDispatcher.h"

OSCPacketDispatcher::OSCPacketDispatcher()
{
    iAddressToClassTable = new QHash<QString,OSCListener*>();
}

OSCPacketDispatcher::~OSCPacketDispatcher()
{
    iAddressToClassTable->clear();
    delete iAddressToClassTable;
}

void OSCPacketDispatcher::addListener(QString& address, OSCListener& listener)
{
    iAddressToClassTable->insert(address, &listener);
}

void OSCPacketDispatcher::dispatchPacket(OSCPacket& packet, QHostAddress& address, QDateTime* timestamp)
{
    if(packet.isBundle)
    {
        dispatchBundle((OSCBundle&)packet, address);
    }else
    {
        if(timestamp)
        {
            dispatchMessage((OSCMessage&)packet, address, *timestamp);
        }else
        {
            dispatchMessage((OSCMessage&)packet, address);
        }
    }
}

void OSCPacketDispatcher::dispatchBundle(OSCBundle& bundle, QHostAddress& address)
{
    QDateTime& timestamp = bundle.getTimestamp();
    QList<OSCPacket>& packets = bundle.getPackets();
    for (int i = 0; i < packets.length(); i++)
    {
        dispatchPacket((OSCPacket&)packets.at(i), address, &timestamp);
    }
}

void OSCPacketDispatcher::dispatchMessage(OSCMessage& message, QHostAddress& address)
{
    QDateTime dtimeNull = QDateTime();
    dispatchMessage(message, address, dtimeNull);
}

void OSCPacketDispatcher::dispatchMessage(OSCMessage& message, QHostAddress& address, QDateTime& time)
{
    QList<QString> mkeys = iAddressToClassTable->keys();
    for(int i=0; i < mkeys.length(); i++)
    {
        const QString& addresskey = mkeys.at(i);
        if(addresskey==message.getAddress())
        {
            OSCListener* olistener = iAddressToClassTable->value(addresskey);
            olistener->acceptMessage(address,time,message);
        }
    }
}
