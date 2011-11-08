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
    std::cout << "Type: " << type << '\n';
    switch (type)
    {
    case 0:
        Mouse::Get().Down(Mouse::sLeft);
        Mouse::Get().Up(Mouse::sLeft);
        std::cout << "Left click\n";
        break;
    case 1:
        std::cout << "Release\n";
        break;
    case 2:
    {
        int dX = args[1].toInt();
        int dY = args[2].toInt();
        std::cout << "x: " << dX << " y: " << dY << '\n';
        Mouse::Get().MovePosition(QPoint(dX, dY));
    }
        break;
    case 3:
        Mouse::Get().Down(Mouse::sRight);
        Mouse::Get().Up(Mouse::sRight);
        std::cout << "Right click\n";
        break;
    default:
        // unknown type
        printf("Unknown mouse type\n");
        break;
    }
}
