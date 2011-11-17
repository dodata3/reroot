/****************************************************************************
**
**
****************************************************************************/

#ifndef OSCLISTENER_H
#define OSCLISTENER_H

#include <QDateTime>
#include <QHostAddress>

class OSCMessage;

class OSCListener : public QObject
{
public:
    virtual void acceptMessage( QHostAddress& address, QDateTime& time, OSCMessage& message ) {}

};

#endif
