// $Id$
// Description: Dialog which displays and handles connection protocol
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include <QtGlobal>
#include <QHostAddress>
#include <QNetworkInterface>
#include <QVBoxLayout>
#include <QDateTime>
#include <QMap>
#include <iostream>
#include "ConnectDialog.h"

using namespace std;

ConnectDialog::ConnectDialog()
{
	//iconLabel->setMinimumWidth(durationLabel->sizeHint().width());

	setWindowTitle( tr( "Reroot: Connect a Device" ) );
    mConnectionCode.setAlignment( Qt::AlignHCenter );
	QVBoxLayout* layout = new QVBoxLayout;
	layout->addWidget( &mQRCode );
	layout->addWidget( &mConnectionCode );
	setLayout( layout );
	resize( 400, 300 );
	setVisible( false );
}

void ConnectDialog::setVisible( bool visible )
{
	QDialog::setVisible( visible );
}

void ConnectDialog::closeEvent(QCloseEvent *event)
{

}

void ConnectDialog::showMessage()
{

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

void ConnectDialog::ConnectNewDevice( Connector* connector )
{
    QHostAddress hostaddress = AcquireServerIP();
    qsrand( QDateTime::currentDateTime().toTime_t() );
    quint32 randomNumber = qrand();
    quint32 ip = hostaddress.toIPv4Address();
    qDebug() << "Ip: " << hostaddress.toString() << " = " << ip;
    qDebug() << "RandomNumber: " << randomNumber;
    QString connectionCode = QString( "%1%2" )
        .arg( ip, 8, 16, QLatin1Char('0') )
        .arg( randomNumber, 8, 16, QLatin1Char('0') ).toUpper();
    qDebug() << connectionCode;
    mConnectionCode.setText( connectionCode );
    mQRCode.RenderConnectionCode( connectionCode );
    setVisible( true );
}
