// $Id$
// Description: Listens for a handshake message from a device for a connection
// (C) Cyberpad Technologies 2011

#include "OSCMessage.h"
#include "HandshakeListener.h"
#include "Connector.h"

HandshakeListener::HandshakeListener( Connector* connector ) :
    mpConnector( connector )
{
}

void HandshakeListener::acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    qDebug() << "Handshake Recieved!";
    quint32 connectKey = mpConnector->GetConnectKey();
    if( connectKey )
    {
        // The connect key is nonzero, we're waiting for a handshake.

        // Parse the message

        // If keys match up, we can add the device's address and public keys to the connector

        // Call back up to the connector to indicate success in handshake

    }
}
