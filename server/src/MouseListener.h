// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef MOUSELISTENER_H_
#define MOUSELISTENER_H_

#include "CipheredListener.h"

class MouseListener : public CipheredListener
{
    enum Button
    {
        Mouse1 = 0x1,
        Mouse2 = 0x2,
        Mouse3 = 0x4,
        Mouse4 = 0x8,
        Mouse5 = 0x16,

        MouseLeft = 0x1,
        MouseRight = 0x2,
        MouseMiddle = 0x4,
    };

public:
	MouseListener( DeviceServer* server );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message );
};

#endif // MOUSELISTENER_H_
