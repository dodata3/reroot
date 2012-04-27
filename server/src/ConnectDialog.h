// $Id$
// Description: Widget which displays and handles connection protocol
// (C) Cyberpad Technologies 2011
#ifndef CONNECTDIALOG_H_
#define CONNECTDIALOG_H_

#include <QSystemTrayIcon>
#include <QDialog>
#include <QLabel>
#include <QTimer>
#include <QHostAddress>
#include "QRWidget.h"

class Connector;
class QNetworkAddress;

class ConnectDialog : public QDialog
{
	Q_OBJECT

public:
	ConnectDialog( Connector* connector );

	void setVisible( bool visible );
	void ConnectNewDevice();
	QHostAddress AcquireServerIP();

protected:
	void closeEvent( QCloseEvent *event );

private slots:
	void ConnectionTimeout();
	void ConnectionSuccess( QString name );

private:
    Connector* mpConnector;
    QRWidget mQRCode;
    QLabel mConnectionCode;
    QLabel mCheck;
    QLabel mCross;
	QTimer mTimeout;
};

#endif // CONNECTDIALOG_H_
