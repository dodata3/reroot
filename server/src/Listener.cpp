// $Id$
// Description: Main window for RerootServer
// (C) Cyberpad Technologies 2011

#include <iostream>
#include "Listener.h"

using namespace std;

void Listener::acceptMessage( QDateTime& time, OSCMessage& message )
{
    cout << "Received message!" << endl;
}
