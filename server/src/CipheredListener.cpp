// $Id$
// Description: Decyphers encrypted messages
// (C) Cyberpad Technologies 2011

#include <QList>
#include "CipheredListener.h"
#include "Connector.h"

CipheredListener::CipheredListener( Connector* connector )
{
    mpConnector = connector;
}

void CipheredListener::acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
	// Decipher arguments individually
	QList< QVariant > args = message.getArguments();
	QString m = args[0].toString();
	QString s = args[1].toString();
	if( Decrypt( mpConnector->PrivateEncKey(), mpConnector->GetClientSignKey( address ), m, s ) )
        acceptDecipheredMessage( address, time, m );
}
