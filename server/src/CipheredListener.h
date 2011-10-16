// $Id$
// Description: Deciphers encrypted messages
// (C) Cyberpad Technologies 2011
#ifndef CIPHEREDLISTENER_H_
#define CIPHEREDLISTENER_H_

#include "OSCListener.h"
#include "Cipher.h"

class Connector;

class CipheredListener : public OSCListener, public Cipher
{
public:
	explicit CipheredListener( Connector* connector ) : mpConnector( connector ) {}
    virtual void acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message ) = 0;
private:
	Connector* mpConnector;
};

#endif // CIPHEREDLISTENER_H_
