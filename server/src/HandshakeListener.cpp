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
		QList< QVariant > args = message.getArguments();
		QByteArray encKeyMod = args[0].toByteArray();
		QByteArray encKeyExp = args[1].toByteArray();
		QByteArray signKeyMod = args[2].toByteArray();
		QByteArray signKeyExp = args[3].toByteArray();
		quint32 randKey = args[4].toUInt();

        // If keys match up, we can add the device's address and public keys to the connector
		if( randKey == mpConnector->GetConnectKey() )
			mpConnector->AddNewDevice( address, encKeyMod, encKeyExp, signKeyMod, signKeyExp );
    }
}
