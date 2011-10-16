/****************************************************************************
**
**
****************************************************************************/

#ifndef OSCPACKETDSPCH_H
#define OSCPACKETDSPCH_H

#include "OSCListener.h"
#include "OSCBundle.h"
#include"OSCMessage.h"
//#include <QHash>

class OSCPacketDispatcher
{
public:
    OSCPacketDispatcher();
    ~OSCPacketDispatcher();

    void addListener(QString& address, OSCListener& listener);
    void dispatchPacket(OSCPacket& packet, QHostAddress& address, QDateTime* timestamp = NULL);

private:
    void dispatchBundle(OSCBundle& bundle, QHostAddress& address);
    void dispatchMessage(OSCMessage& message, QHostAddress& address );
    void dispatchMessage(OSCMessage& message, QHostAddress& address, QDateTime& time);

    QHash<QString,OSCListener>* iAddressToClassTable;

};

#endif
