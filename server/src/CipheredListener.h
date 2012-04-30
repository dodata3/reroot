// $Id$
// Description: Deciphers encrypted messages
// (C) Cyberpad Technologies 2011
#ifndef CIPHEREDLISTENER_H_
#define CIPHEREDLISTENER_H_

#include <QDebug>
#include "OSCListener.h"
#include "Cipher.h"

class Connector;

class CipheredListener : public OSCListener, public Cipher
{
public:
	CipheredListener( Connector* connector );
    virtual void acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, QString& message ) = 0;
protected:
	Connector* mpConnector;
};

#endif // CIPHEREDLISTENER_H_
