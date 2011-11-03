// $Id$
// Description: Widget which displays and handles connection protocol
// (C) Cyberpad Technologies 2011
#ifndef CONNECTDIALOG_H_
#define CONNECTDIALOG_H_

#include <QSystemTrayIcon>
#include <QDialog>
#include "Connector.h"

class QNetworkAddress;

class ConnectDialog : public QDialog
{
	Q_OBJECT

public:
	ConnectDialog();

	void setVisible( bool visible );
	void ConnectNewDevice( Connector* connector );
	QHostAddress AcquireServerIP();

protected:
	void closeEvent( QCloseEvent *event );

private slots:
	void showMessage();
};

#endif // CONNECTDIALOG_H_
