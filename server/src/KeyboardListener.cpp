// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011

#include <iostream>
#include <QCursor>
#include "OSCMessage.h"
#include "KeyboardListener.h"
#include "Keyboard.h"

using namespace std;

KeyboardListener::KeyboardListener( Connector* connector ) :
    CipheredListener( connector )
{
}

void KeyboardListener::acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    QList< QVariant > args = message.getArguments();
    int type = args[0].toInt();
    int keycode = args[1].toInt();
    int character = args[2].toInt();
    switch (type)
    {
    case 0:
        Keyboard::get().down(keycode);
        printf("Key down message: keycode %i character %i\n", keycode, character);
        break;
    case 1:
        Keyboard::get().down(keycode);
        printf("Key up message: keycode %i character %i\n", keycode, character);
        break;
    default:
        // unknown type
        break;
    }
}
