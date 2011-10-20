// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011

#include <iostream>
#include <QCursor>
#include "OSCMessage.h"
#include "MouseListener.h"

using namespace std;

MouseListener::MouseListener( Connector* connector ) :
    CipheredListener( connector )
{
}

void MouseListener::acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    QList< QVariant > args = message.getArguments();
    int dX = args[1].toInt();
    int dY = args[2].toInt();

    if (args.size() > 4)
    {
        // It doesn't look like Qt can do anything beyond cursor position
        int pressed = args[3].toInt();
        int released = args[4].toInt();

        if (pressed & Mouse1)
        {
            // Left click
        }
        if (pressed & Mouse2)
        {
            // Right click
        }
        // etc...

        if (released & Mouse1)
        {
            // Left unclick
        }
        if (released & Mouse2)
        {
            // Right unclick
        }
        // etc...

    }

    QPoint curPos = QCursor::pos();
    QCursor::setPos( curPos.x() + dX, curPos.y() + dY );
}
