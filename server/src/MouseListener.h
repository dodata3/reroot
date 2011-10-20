// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef MOUSELISTENER_H_
#define MOUSELISTENER_H_

#include "CipheredListener.h"

class MouseListener : public CipheredListener
{
    enum buttonType
    {
        Mouse1 = 0x1,
        Mouse2 = 0x2,
        Mouse3 = 0x4,
        Mouse4 = 0x8,
        Mouse5 = 0x16
    };

    typedef Mouse1 MouseLeft;
    typedef Mouse2 MouseRight;
    typedef Mouse3 MouseMiddle;
public:
	MouseListener( Connector* connector );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
};

#endif // MOUSELISTENER_H_
