// $Id$
// Description: Listens for Control messages and acts on them
// (C) Cyberpad Technologies 2011

#include <QtDebug>
#include "OSCMessage.h"
#include "ControlListener.h"
#include "Mouse.h"
#include "Keyboard.h"

using namespace std;

ControlListener::ControlListener( Connector* connector ) :
    CipheredListener( connector )
{
}

void ControlListener::acceptDecipheredMessage( QHostAddress& address, QDateTime& time, QString& inMessage )
{
    // Parse out the deciphered message
    Instruction i = ParseMessage( inMessage );

    // Determine what to do with this message
    switch( i.Device )
    {

    case Mouse:
        switch( i.Action )
        {
        case Down:
            Mouse::Get().Down(Mouse::sLeft);
            break;
        case Up:
            Mouse::Get().Up(Mouse::sLeft);
            break;
        case Move:
            Mouse::Get().MovePosition( QPoint( i.Data1, i.Data2 ) );
            break;
        default:
            // unknown action
            break;
        }
        break;

    case Keyboard:
        switch( i.Action )
        {
        case Down:
            Keyboard::Get().Down( i.Control );
            qDebug() << "Key down message: keycode " << i.Control << " character " << i.Data1;
            break;
        case Up:
            Keyboard::Get().Up( i.Control );
            qDebug() << "Key down message: keycode " << i.Control << " character " << i.Data2;
            break;
        default:
            // unknown action
            break;
        }
        break;

    case Gamepad:
        break;

    default:
        // unknown device
        break;
    }
}

ControlListener::Instruction ControlListener::ParseMessage( QString& inMessage )
{
    Instruction ret;
    ret.Device = DeviceEnum( inMessage.section( ',', 0, 0 ).toInt() );
    ret.Control = inMessage.section( ',', 1, 1 ).toInt();
    ret.Action = ActionEnum( inMessage.section( ',', 2, 2 ).toInt() );
    ret.Data1 = inMessage.section( ',', 3, 3 ).toInt();
    ret.Data2 = inMessage.section( ',', 4, 4 ).toInt();
    return ret;
}
