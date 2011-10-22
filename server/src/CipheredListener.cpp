// $Id$
// Description: Decyphers encrypted messages
// (C) Cyberpad Technologies 2011

#include <QList>
#include "CipheredListener.h"
#include "Device.h"

/*
CipheredListener::CipheredListener( Connector* connector )
{
    mpConnector = connector;
}

void CipheredListener::acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
	// Decipher arguments individually
	SecretKey secretKey = mpConnector->GetSecretKey( address );
	QList< QVariant > args = message.getArguments();
	OSCMessage decipheredMessage;
	for( int i = 0; i < args.size(); i++ )
	{
		QByteArray arg = args[i].toByteArray();
		if( Decrypt( secretKey, arg ) )
			decipheredMessage.addArgument( arg );
		else
			return;
	}

	// Send the Deciphered Message
	acceptDecipheredMessage( address, time, decipheredMessage );
}
*/

CipheredListener::CipheredListener(DeviceServer * server) : mServer(server)
{

}

void CipheredListener::acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
	// Decipher arguments individually
	SecretKey secretKey = mServer->ClientKey( address.toString() );
	QList< QVariant > args = message.getArguments();
	OSCMessage decipheredMessage;
	for( int i = 0; i < args.size(); i++ )
	{
		QByteArray arg = args[i].toByteArray();
		if( Decrypt( secretKey, arg ) )
			decipheredMessage.addArgument( arg );
		else
			return;
	}

	// Send the Deciphered Message
	acceptDecipheredMessage( address, time, decipheredMessage );
}
