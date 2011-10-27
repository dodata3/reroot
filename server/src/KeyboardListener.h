// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef KEYBOARDLISTENER_H_
#define KEYBOARDLISTENER_H_

#include "CipheredListener.h"

class KeyboardListener : public CipheredListener
{
public:
	KeyboardListener( Connector* connector );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
};

#endif // KEYBOARDLISTENER_H_
