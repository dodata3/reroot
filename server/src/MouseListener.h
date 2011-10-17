// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef MOUSELISTENER_H_
#define MOUSELISTENER_H_

#include "CipheredListener.h"

class MouseListener : public CipheredListener
{
public:
	MouseListener( Connector* connector );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
};

#endif // LISTENER_H_
