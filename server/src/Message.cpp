#include "Message.h"

MessageString::MessageString(const QString& string) : mString(string)
{

}

QList<QVariant> MessageString::serialize() const
{


    return OSCMessage(mString);
}
