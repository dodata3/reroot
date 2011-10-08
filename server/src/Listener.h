// $Id$
// Description: Main window for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef LISTENER_H_
#define LISTENER_H_

#include "OSCListener.h"

class Listener : public OSCListener
{
public:
    virtual void acceptMessage(QDateTime& time, OSCMessage& message);
};

#endif // LISTENER_H_
