/****************************************************************************
**
**
****************************************************************************/

#ifndef OSCMESSAGE_H
#define OSCMESSAGE_H

#include "OSCPacket.h"
#include "OSCms_global.h"


class OSCMSSHARED_EXPORT OSCMessage : public OSCPacket
{
public:
    OSCMessage();
    OSCMessage(QString& newAddress);
    OSCMessage(QString& newAddress, QList<QVariant>& newArguments);
    void construct(QString& newAddress, QList<QVariant>& newArguments);

    ~OSCMessage();

    QString getAddress();
    void setAddress(QString& anAddress);
    void addArgument(QVariant argument);
    QList<QVariant> getArguments();

protected:
    void computeAddressByteArray(OSCMsgToByteArrayConverter& stream);
    void computeArgumentsByteArray(OSCMsgToByteArrayConverter& stream);
    void computeByteArray(OSCMsgToByteArrayConverter& stream);

    QVector<QVariant>* iArguments;
    QString iAddress;
};

#endif
