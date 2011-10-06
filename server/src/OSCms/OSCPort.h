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
    OSCPort(QHostAddress& LocalAddress, QHostAddress& remoteAddress);
    OSCPort(QHostAddress& LocalAddress, QHostAddress& remoteAddress, qint16 newPort);
    ~OSCPort();

    /**
     * The port that the SuperCollider <b>synth</b> engine ususally listens to &mdash; 57110.
     */
    inline static qint16 defaultSCOSCPort()     { return 57110; }
    /**
     * The port that the SuperCollider <b>language</b> engine ususally listens to &mdash; 57120.
     */
    inline static qint16 defaultSCLangOSCPort() { return 57120; }
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
    void construct(QHostAddress& LocalAddress, QHostAddress& newAddress, qint16 newPort);

protected:
    void run();
    void DummySlot() { };

    QHostAddress iAddress;
    bool ibListening;
    OSCPacketDispatcher* iDispatcher;
    OSCByteArrayToMsgConverter* iConverter;

    QUdpSocket* iSocket;
    qint16 iPort;
};

#endif
