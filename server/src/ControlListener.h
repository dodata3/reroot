// $Id$
// Description: Listens for Control messages and acts on them
// (C) Cyberpad Technologies 2011
#ifndef CONTROLLISTENER_H_
#define CONTROLLISTENER_H_

#include "CipheredListener.h"

class ControlListener : public CipheredListener
{
public:
	ControlListener( Connector* connector );
    virtual void acceptDecipheredMessage( QHostAddress& address, QDateTime& time, QString& message );

    typedef enum DeviceEnum
    {
        Mouse = 0,
        Keyboard,
        Gamepad
    } DeviceEnum;

    typedef enum ActionEnum
    {
        Down = 0,
        Up,
        Move
    } ActionEnum;

    struct Instruction
    {
        DeviceEnum Device;
        int Control;
        ActionEnum Action;
        int Data1;
        int Data2;
    };

private:
    Instruction ParseMessage( QString& message );
};

#endif // CONTROLLISTENER_H_
