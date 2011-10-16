// $Id$
// Description: Listens for Mouse messages and acts on them
// (C) Cyberpad Technologies 2011

#include <iostream>
#include <QCursor>
#include "OSCMessage.h"
#include "MouseListener.h"

using namespace std;

void MouseListener::acceptDecipheredMessage( QHostAddress& address, QDateTime& time, OSCMessage& message )
{
    QList< QVariant > args = message.getArguments();
    int dX = args[0].toInt();
    int dY = args[1].toInt();
    QPoint curPos = QCursor::pos();
    QCursor::setPos( curPos.x() + dX, curPos.y() + dY );
}
