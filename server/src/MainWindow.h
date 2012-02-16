// $Id$
// Description: Main window for RerootServer
// (C) Cyberpad Technologies 2011
#ifndef MAINWINDOW_H_
#define MAINWINDOW_H_

#include <QSystemTrayIcon>
#include <QDialog>
#include "Connector.h"
#include "ConnectDialog.h"

class QAction;
class QCheckBox;
class QComboBox;
class QGroupBox;
class QLabel;
class QLineEdit;
class QMenu;
class QPushButton;
class QSpinBox;
class QTextEdit;

class MainWindow : public QDialog
{
	Q_OBJECT

public:
	MainWindow();

	void setVisible(bool visible);

protected:
	void closeEvent(QCloseEvent *event);

private slots:
	void setIcon(int index);
	void iconActivated(QSystemTrayIcon::ActivationReason reason);
	void showMessage();
	void messageClicked();
	void connectNew();
	void disconnectAll();

private:
	void createIconGroupBox();
	void createMessageGroupBox();
	void createActions();
	void createTrayIcon();

	QGroupBox *iconGroupBox;
	QLabel *iconLabel;
	QComboBox *iconComboBox;
	QCheckBox *showIconCheckBox;

	QGroupBox *messageGroupBox;
	QLabel *typeLabel;
	QLabel *durationLabel;
	QLabel *durationWarningLabel;
	QLabel *titleLabel;
	QLabel *bodyLabel;
	QComboBox *typeComboBox;
	QSpinBox *durationSpinBox;
	QLineEdit *titleEdit;
	QTextEdit *bodyEdit;
	QPushButton *showMessageButton;

	QAction *connectAction;
	QAction *advancedAction;
	QAction *disconnectAllAction;
	QAction *exitAction;

	QSystemTrayIcon *trayIcon;
	QMenu *trayIconMenu;

	Connector mConnector;
	ConnectDialog mConnectDialog;
};

#endif // MAINWINDOW_H_
