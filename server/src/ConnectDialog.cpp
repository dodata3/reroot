// $Id$
// Description: Dialog which displays and handles connection protocol
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include <QtGlobal>
#include <QNetworkInterface>
#include <QVBoxLayout>
#include <QDateTime>
#include <QMap>
#include <iostream>
#include "ConnectDialog.h"
#include "Connector.h"

#define CONNECT_TIME 60000 // One Minute to Connect
#define FEEDBACK_TIME 2000 // 2 Seconds of Visual Feedback

using namespace std;

ConnectDialog::ConnectDialog( Connector* connector )
{
    mpConnector = connector;
	setWindowTitle( tr( "Reroot: Connect a Device" ) );
    mConnectionCode.setAlignment( Qt::AlignHCenter );
	QVBoxLayout* layout = new QVBoxLayout;
	mQRCode.setSizePolicy( QSizePolicy::Expanding, QSizePolicy::Expanding );
	mQRCode.sizePolicy().setHeightForWidth( true );
	mConnectionCode.setSizePolicy( QSizePolicy::Minimum, QSizePolicy::Minimum );
	mConnectionCode.setScaledContents( true );
	layout->addWidget( &mQRCode );
	layout->addWidget( &mConnectionCode );
	setLayout( layout );
	resize( 400, 450 );
	setVisible( false );
	connect( &mTimeout, SIGNAL( timeout() ), this, SLOT( ConnectionTimeout() ) );
	mTimeout.setSingleShot( true );
}

void ConnectDialog::setVisible( bool visible )
{
	QDialog::setVisible( visible );
}

void ConnectDialog::closeEvent(QCloseEvent *event)
{
    mpConnector->SetConnectKey();
}

QHostAddress ConnectDialog::AcquireServerIP()
{
    // Issue: PROXY SERVERS.  Blarg.
    QStringList items;
    QMap< QString, QHostAddress > addressmap;
    foreach(QNetworkInterface interface, QNetworkInterface::allInterfaces())
    {
        if (interface.flags().testFlag(QNetworkInterface::IsRunning))
            foreach (QNetworkAddressEntry entry, interface.addressEntries())
            {
                if ( interface.hardwareAddress() != "00:00:00:00:00:00" && entry.ip().toString().contains("."))
                    addressmap[ interface.name() ] = entry.ip();
            }
    }

    // Eventually we want to prefer wired interface connections over wireless connections
    // It just so happens that on linux, wired connections are typically named "eth#"
    // and wireless connections are "wlan#"  As such, we can lazily just pull the first
    // interface and be done with it.
    return addressmap.begin().value();
}

void ConnectDialog::ConnectNewDevice()
{
    qDebug() << "=============== INITIATING CONNECT SEQUENCE ===============";
    QHostAddress hostaddress = AcquireServerIP();
    qsrand( QDateTime::currentDateTime().toTime_t() );
    qint32 randomNumber = 0;
    while( !randomNumber ) randomNumber = qrand() % 127;
    quint32 ip = hostaddress.toIPv4Address();
    qDebug() << "Ip: " << hostaddress.toString() << " = " << ip;
    qDebug() << "Random Number: " << randomNumber;
    QString connectionCode = QString( "%1%2" )
        .arg( ip, 8, 16, QLatin1Char('0') )
        .arg( randomNumber, 2, 16, QLatin1Char('0') ).toUpper();
    qDebug() << "Connection Code: " << connectionCode;
    mConnectionCode.setText( connectionCode );
    mQRCode.RenderConnectionCode( connectionCode );
    mpConnector->SetConnectKey( randomNumber );
    setVisible( true );

	// Schedule a ConnectionTimeout event
	mTimeout.start( CONNECT_TIME );
	qDebug() << "Waiting for connection from client...";
}

void ConnectDialog::ConnectionSuccess( QString name )
{
	qDebug() << "Connection Success: " << name << " Connected.";
    mpConnector->SetConnectKey();

	// Cancel Timeout Timer
	mTimeout.stop();

	// Show the success pane

	// This is likely a problem: What if they open a new QR Code while this timer is running?
	QTimer::singleShot( FEEDBACK_TIME, this, SLOT( hide() ) );
	qDebug() << "=============== FINISHED CONNECT SEQUENCE ===============";
}

void ConnectDialog::ConnectionTimeout()
{
    qDebug() << "Connection attempt failed: Timeout.";
    mpConnector->SetConnectKey();

	// Show the Failure Pane

	// This is likely a problem: What if they open a new QR Code while this timer is running?
	QTimer::singleShot( FEEDBACK_TIME, this, SLOT( hide() ) );
    qDebug() << "=============== FINISHED CONNECT SEQUENCE ===============";
}
