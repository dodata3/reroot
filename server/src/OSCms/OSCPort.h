/****************************************************************************
**
**
****************************************************************************/

#ifndef OSCPORT_H
#define OSCPORT_H

#include <QThread>
#include <QUdpSocket>
#include "OSCPacketDispatcher.h"
#include "OSCByteArrayToMsgConverter.h"

class OSCPort : public QThread
{
public:
    OSCPort(QHostAddress& remoteAddress);
    OSCPort(QHostAddress& remoteAddress, quint16 newPort);
    ~OSCPort();

    /**
     * The port that the SuperCollider <b>synth</b> engine ususally listens to &mdash; 57110.
     */
    inline static quint16 defaultSCOSCPort()     { return 57110; }
    /**
     * The port that the SuperCollider <b>language</b> engine ususally listens to &mdash; 57120.
     */
    inline static quint16 defaultSCLangOSCPort() { return 57120; }
    /**
     * Close the socket and free-up resources. It's recommended that clients call
     * this when they are done with the port.
     */
    inline void close() { iSocket->close(); }

    void startListening();
    void stopListening();
    bool isListening() { return ibListening; }
    void addListener(QString& anAddress, OSCListener& listener);
    void send(OSCPacket& aPacket);
    void construct(QHostAddress& newAddress, quint16 newPort);

protected:
    void run();

    QHostAddress iAddress;
    bool ibListening;
    OSCPacketDispatcher* iDispatcher;
    OSCByteArrayToMsgConverter* iConverter;

    QUdpSocket* iSocket;
    quint16 iPort;
};

#endif
