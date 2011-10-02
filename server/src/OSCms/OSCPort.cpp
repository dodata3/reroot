/****************************************************************************
**
**
****************************************************************************/

#include "OSCPort.h"

/**
 * Create an OSCPort that sends to newAddress, on the standard SuperCollider port
 * @param newAddress InetAddress
 *
 * Default the port to the standard one for SuperCollider
 */
OSCPort::OSCPort(QHostAddress& LocalAddress, QHostAddress& remoteAddress)
{
    construct(LocalAddress, remoteAddress, defaultSCOSCPort());
}

/**
 * Create an OSCPort that sends to newAddress, newPort
 * @param newAddress InetAddress
 * @param newPort int
 */
OSCPort::OSCPort(QHostAddress& LocalAddress, QHostAddress& remoteAddress, qint16 newPort)
{
    construct(LocalAddress, remoteAddress, newPort);
}

void OSCPort::construct(QHostAddress& LoAddr, QHostAddress& newAddress, qint16 newPort)
{
    iSocket  = new QUdpSocket();
    iDispatcher = new OSCPacketDispatcher();
    iConverter  = new OSCByteArrayToMsgConverter();

    iAddress = newAddress;
    iPort  = newPort;
    iSocket->bind(LoAddr, iPort);
}

OSCPort::~OSCPort()
{
    iSocket->close();
    delete iSocket;
    delete iDispatcher;
    delete iConverter;
}

void OSCPort::run()
{
    QByteArray datagram(1536,0);
    connect(iSocket,SIGNAL(readyRead()),this,SLOT(DummySlot()) );
    while(ibListening)
    {
        if(!iSocket->waitForReadyRead())    continue;
        if(!iSocket->hasPendingDatagrams()) continue;

        qint32 mbytesLength;
        mbytesLength = iSocket->readDatagram(datagram.data(),1536);
        if(mbytesLength != -1)
        {
            OSCPacket& oscPacket = iConverter->convert(&datagram,mbytesLength);
            iDispatcher->dispatchPacket(oscPacket);
            delete &oscPacket;
        }
    }
}

/**
 * Start listening for incoming OSCPackets
 */
void OSCPort::startListening()
{
    if(!ibListening)
    {
        start();
        ibListening = TRUE;
    }
}

void OSCPort::stopListening()
{
    ibListening = FALSE;
}

/**
 * Register the listener for incoming OSCPackets addressed to an Address
 * @param anAddress  the address to listen for
 * @param listener   the object to invoke when a message comes in
 */
void OSCPort::addListener(QString& anAddress, OSCListener& listener)
{
    iDispatcher->addListener(anAddress, listener);
}

/**
 * Send an osc packet (message or bundle) to the receiver I am bound to.
 * @param aPacket OSCPacket
 */
void OSCPort::send(OSCPacket& aPacket)
{
    QByteArray& byteArray = aPacket.getByteArray();
    iSocket->writeDatagram(byteArray, iAddress, iPort);
}
