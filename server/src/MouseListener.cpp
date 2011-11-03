// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011

#include <iostream>
#include <QCursor>
#include "OSCMessage.h"
#include "MouseListener.h"
#include "Mouse.h"

using namespace std;

MouseListener::MouseListener( Connector* connector ) :
    CipheredListener( connector )
{
}

void MouseListener::acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    QList< QVariant > args = message.getArguments();
    int type = args[0].toInt();
    switch (type)
    {
    case 0:
        Mouse::get().down(Mouse::sLeft);
        Mouse::get().up(Mouse::sLeft);
        break;
    case 1:
        //Mouse::get().up(Mouse::sLeft);
        Mouse::get().down(Mouse::sRight);
        Mouse::get().up(Mouse::sRight);
        break;
    case 2:
    {
        int dX = args[1].toInt();
        int dY = args[2].toInt();
        Mouse::get().MovePosition(QPoint(dX, dY));
    }
        break;
    default:
        // unknown type
        break;
    }
}
