// $Id$
// Description: Listens for a handshake message from a device for a connection
// (C) Cyberpad Technologies 2011
#ifndef HANDSHAKELISTENER_H_
#define HANDSHAKELISTENER_H_

#include "OSCListener.h"

class Connector;

class HandshakeListener : public OSCListener
{

public:
	HandshakeListener( Connector* connector );
    virtual void acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );

private:
    Connector* mpConnector;
};

#endif // HANDSHAKELISTENER_H_
