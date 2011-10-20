// $Id$
// Description: Server-Client Message abstraction for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef MESSAGE_H_
#define MESSAGE_H_

#include <QtGui>

#include "OSCMessage.h"

class Message
{
public:
    Message( const OSCMessage& oscmessage );
    virtual OSCMessage serialize() const = 0;
};

class MessageInput : public Message
{
public:
    virtual void simulate() const = 0;
}

class MessageMouse : public MessageInput
{
public:
    virtual void simulate() const;
}

class MessageData : public Message
{
public:

}

class MessageString : public Message
{
    QString mString;
public:
    MessageString( const QString& string );
    OSCMessage serialize() const;
}

#endif // MESSAGE_H_
