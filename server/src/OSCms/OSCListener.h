/****************************************************************************
**
**
****************************************************************************/

#ifndef OSCLISTENER_H
#define OSCLISTENER_H

#include <QDateTime>

class OSCMessage;

class OSCListener
{
public:

    virtual void acceptMessage(QDateTime& time, OSCMessage& message) {}

};

#endif
