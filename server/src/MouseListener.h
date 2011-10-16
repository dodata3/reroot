// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef MOUSELISTENER_H_
#define MOUSELISTENER_H_

#include "CipheredListener.h"

class MouseListener : public CipheredListener
{
public:
	explicit MouseListener( Connector* connector ) : CipheredListener( connector ) {}
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
};

#endif // LISTENER_H_
