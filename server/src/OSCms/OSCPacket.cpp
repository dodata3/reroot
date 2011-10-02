/****************************************************************************
**
**
****************************************************************************/

#include "OSCPacket.h"

OSCPacket::OSCPacket()
{
}

OSCPacket::~OSCPacket()
{
}

/**
 * Return the OSC byte stream for this packet.
 * @return byte[]
 */
QByteArray& OSCPacket::getByteArray()
{
    if(!isByteArrayComputed)
    {
        computeByteArray();
        isByteArrayComputed = TRUE;
    }
    return iByteArray;
}

/**
 * Generate a representation of this packet conforming to the
 * the OSC byte stream specification. Used Internally.
 */
void OSCPacket::computeByteArray()
{
    OSCMsgToByteArrayConverter* stream = new OSCMsgToByteArrayConverter(iByteArray);
    computeByteArray(*stream);
    delete stream;
}
