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
    int dX = args[1].toInt();
    int dY = args[2].toInt();

    Mouse::get().MovePosition(QPoint(dX, dY));
    //QPoint curPos = QCursor::pos();
    //QCursor::setPos( curPos.x() + dX, curPos.y() + dY );
}
