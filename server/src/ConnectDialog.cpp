// $Id$
// Description: Dialog which displays and handles connection protocol
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include <iostream>
#include "ConnectDialog.h"

using namespace std;

ConnectDialog::ConnectDialog()
{
	//iconLabel->setMinimumWidth(durationLabel->sizeHint().width());

	setWindowTitle(tr("Reroot: Connect a Device"));
	resize(400, 300);
	setVisible( false );
}

void ConnectDialog::setVisible( bool visible )
{
	QDialog::setVisible(visible);
}

void ConnectDialog::closeEvent(QCloseEvent *event)
{

}

void ConnectDialog::showMessage()
{

}
