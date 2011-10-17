// $Id$
// Description: Listens for a handshake message from a device for a connection
// (C) Cyberpad Technologies 2011

#include <QCursor>
#include "OSCMessage.h"
#include "HandshakeListener.h"

HandshakeListener::HandshakeListener()
{
}

void HandshakeListener::acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    qDebug() << "Handshake";
}
