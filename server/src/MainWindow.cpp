// $Id$
// Description: Main window for RerootServer
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include <iostream>
#include "MainWindow.h"

#include "Keyboard.h"
#include "Mouse.h"
#include "Context.h"

using namespace std;

MainWindow::MainWindow() :
    mConnectDialog( &mConnector )
{
	createIconGroupBox();
	createMessageGroupBox();

	iconLabel->setMinimumWidth(durationLabel->sizeHint().width());

	createActions();
	createTrayIcon();

	connect(showMessageButton, SIGNAL(clicked()), this, SLOT(showMessage()));
	connect(showIconCheckBox, SIGNAL(toggled(bool)), trayIcon, SLOT(setVisible(bool)));
	connect(iconComboBox, SIGNAL(currentIndexChanged(int)), this, SLOT(setIcon(int)));
	connect(trayIcon, SIGNAL(messageClicked()), this, SLOT(messageClicked()));
	connect(trayIcon, SIGNAL(activated(QSystemTrayIcon::ActivationReason)),
		this, SLOT(iconActivated(QSystemTrayIcon::ActivationReason)));
    connect( &mConnector, SIGNAL( HandshakeSuccessful( QString ) ),
        &mConnectDialog, SLOT( ConnectionSuccess( QString ) ) );

	QVBoxLayout *mainLayout = new QVBoxLayout;
	mainLayout->addWidget(iconGroupBox);
	mainLayout->addWidget(messageGroupBox);
	setLayout(mainLayout);

	iconComboBox->setCurrentIndex(1);
	trayIcon->show();

	setWindowTitle(tr("Systray"));
	resize(400, 300);
	setVisible( false );
}

void MainWindow::setVisible(bool visible)
{
	advancedAction->setEnabled(!isMaximized());
	QDialog::setVisible(visible);
}

void MainWindow::closeEvent(QCloseEvent *event)
{
	if (trayIcon->isVisible()) {
		QMessageBox::information(this, tr("Systray"),
			tr("The program will keep running in the "
				"system tray. To terminate the program, "
				"choose <b>Quit</b> in the context menu "
				"of the system tray entry."));
		hide();
		event->ignore();
	}
}

void MainWindow::setIcon(int index)
{
	QIcon icon = iconComboBox->itemIcon(index);
	trayIcon->setIcon(icon);
	setWindowIcon(icon);

	trayIcon->setToolTip(iconComboBox->itemText(index));
}

void MainWindow::iconActivated(QSystemTrayIcon::ActivationReason reason)
{
	switch (reason) {
	case QSystemTrayIcon::Trigger:
        break;
	case QSystemTrayIcon::DoubleClick:
        connectNew();
		break;
	case QSystemTrayIcon::MiddleClick:
		showMessage();
		break;
	default:
		break;
	}
}

void MainWindow::showMessage()
{
	QSystemTrayIcon::MessageIcon icon = QSystemTrayIcon::MessageIcon( typeComboBox->itemData(typeComboBox->currentIndex()).toInt());
	trayIcon->showMessage(titleEdit->text(), bodyEdit->toPlainText(), icon, durationSpinBox->value() * 1000);
}

void MainWindow::messageClicked()
{
	QMessageBox::information(0, tr("Systray"),
		tr("Sorry, I already gave what help I could.\n"
			"Maybe you should try asking a human?"));
}

void MainWindow::createIconGroupBox()
{
	iconGroupBox = new QGroupBox(tr("Tray Icon"));

	iconLabel = new QLabel("Icon:");

	iconComboBox = new QComboBox;
	iconComboBox->addItem(QIcon(":/images/bad.svg"), tr("Bad"));
	iconComboBox->addItem(QIcon(":/images/heart.svg"), tr("Heart"));
	iconComboBox->addItem(QIcon(":/images/trash.svg"), tr("Trash"));

	showIconCheckBox = new QCheckBox(tr("Show icon"));
	showIconCheckBox->setChecked(true);

	QHBoxLayout *iconLayout = new QHBoxLayout;
	iconLayout->addWidget(iconLabel);
	iconLayout->addWidget(iconComboBox);
	iconLayout->addStretch();
	iconLayout->addWidget(showIconCheckBox);
	iconGroupBox->setLayout(iconLayout);
}

void MainWindow::createMessageGroupBox()
{
	messageGroupBox = new QGroupBox(tr("Balloon Message"));

	typeLabel = new QLabel(tr("Type:"));

	typeComboBox = new QComboBox;
	typeComboBox->addItem(tr("None"), QSystemTrayIcon::NoIcon);
	typeComboBox->addItem(style()->standardIcon(
		QStyle::SP_MessageBoxInformation), tr("Information"),
		QSystemTrayIcon::Information);
	typeComboBox->addItem(style()->standardIcon(
		QStyle::SP_MessageBoxWarning), tr("Warning"),
		QSystemTrayIcon::Warning);
	typeComboBox->addItem(style()->standardIcon(
		QStyle::SP_MessageBoxCritical), tr("Critical"),
		QSystemTrayIcon::Critical);
	typeComboBox->setCurrentIndex(1);

	durationLabel = new QLabel(tr("Duration:"));

	durationSpinBox = new QSpinBox;
	durationSpinBox->setRange(5, 60);
	durationSpinBox->setSuffix(" s");
	durationSpinBox->setValue(15);

	durationWarningLabel = new QLabel(tr("(some systems might ignore this hint)"));
	durationWarningLabel->setIndent(10);

	titleLabel = new QLabel(tr("Title:"));

	titleEdit = new QLineEdit(tr("Cannot connect to network"));

	bodyLabel = new QLabel(tr("Body:"));

	bodyEdit = new QTextEdit;
	bodyEdit->setPlainText(tr("Don't believe me. Honestly, I don't have a "
		"clue.\nClick this balloon for details."));

	showMessageButton = new QPushButton(tr("Show Message"));
	showMessageButton->setDefault(true);

	QGridLayout *messageLayout = new QGridLayout;
	messageLayout->addWidget(typeLabel, 0, 0);
	messageLayout->addWidget(typeComboBox, 0, 1, 1, 2);
	messageLayout->addWidget(durationLabel, 1, 0);
	messageLayout->addWidget(durationSpinBox, 1, 1);
	messageLayout->addWidget(durationWarningLabel, 1, 2, 1, 3);
	messageLayout->addWidget(titleLabel, 2, 0);
	messageLayout->addWidget(titleEdit, 2, 1, 1, 4);
	messageLayout->addWidget(bodyLabel, 3, 0);
	messageLayout->addWidget(bodyEdit, 3, 1, 2, 4);
	messageLayout->addWidget(showMessageButton, 5, 4);
	messageLayout->setColumnStretch(3, 1);
	messageLayout->setRowStretch(4, 1);
	messageGroupBox->setLayout(messageLayout);
}

void MainWindow::createActions()
{
	connectAction = new QAction(tr("&Connect New Device"), this);
	connect(connectAction, SIGNAL(triggered()), this, SLOT(connectNew()));

	advancedAction = new QAction(tr("&Advanced..."), this);
	connect(advancedAction, SIGNAL(triggered()), this, SLOT(showNormal()));

	disconnectAllAction = new QAction(tr("&Disconnect All Devices"), this);
	connect(disconnectAllAction, SIGNAL(triggered()), this, SLOT(disconnectAll()));

	exitAction = new QAction(tr("&Exit"), this);
	connect(exitAction, SIGNAL(triggered()), qApp, SLOT(quit()));
}

void MainWindow::createTrayIcon()
{
	trayIconMenu = new QMenu(this);
	trayIconMenu->addAction(connectAction);
	trayIconMenu->addAction(advancedAction);
	trayIconMenu->addAction(disconnectAllAction);
	trayIconMenu->addSeparator();
	trayIconMenu->addAction(exitAction);

	trayIcon = new QSystemTrayIcon(this);
	trayIcon->setContextMenu(trayIconMenu);
}

void MainWindow::disconnectAll()
{
	// Tell the connector to disconnect all devices
	mConnector.RemoveAllDevices();
}

void MainWindow::connectNew()
{
    /*
    for (unsigned int i = 0; i < 10000; ++i)
    {
        //printf("%u", i);
        printf("");
    }
    for (unsigned int k = 32; k < 1024; ++k)
    {
        for (unsigned int i = 0; i < 10; ++i)
        {
            //printf("%u", i);
            printf("", i);
        }
        //Keyboard::Get().Down(k);
        //Keyboard::Get().Up(k);
        //printf("\n%u\n", k);
    }
    // For testing purposes
    #ifdef OS_WINDOWS
        Keyboard::Get().ModifierDown(Keyboard::ModAlt);
        Keyboard::Get().Down(0x0009); // Tab
        Keyboard::Get().Up(0x0009); // Tab
        Keyboard::Get().ModifierUp(Keyboard::ModAlt);
    #endif
    Context::Get().Title();
    Context::Get().ProcessID();
    Context::Get().Executable();
    */

	// Spawn a new window which can be used to connect, give information to the connector
	mConnectDialog.ConnectNewDevice();

}
