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

using namespace std;

ConnectDialog::ConnectDialog( Connector* connector )
{
	//iconLabel->setMinimumWidth(durationLabel->sizeHint().width());
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
    QHostAddress hostaddress = AcquireServerIP();
    qsrand( QDateTime::currentDateTime().toTime_t() );
    quint32 randomNumber = 0;
    while( !randomNumber ) randomNumber = qrand();
    quint32 ip = hostaddress.toIPv4Address();
    qDebug() << "Ip: " << hostaddress.toString() << " = " << ip;
    qDebug() << "RandomNumber: " << randomNumber;
    QString connectionCode = QString( "%1%2" )
        .arg( ip, 8, 16, QLatin1Char('0') )
        .arg( randomNumber, 8, 16, QLatin1Char('0') ).toUpper();
    qDebug() << connectionCode;
    mConnectionCode.setText( connectionCode );
    mQRCode.RenderConnectionCode( connectionCode );
    mpConnector->SetConnectKey( randomNumber );

    setVisible( true );
}

void ConnectDialog::ConnectionSuccess()
{
    mpConnector->SetConnectKey();
}

void ConnectDialog::ConnectionTimeout()
{
    mpConnector->SetConnectKey();

}
