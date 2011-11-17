// $Id$
// Description: Widget which renders a QR Code from a connection code
// (C) Cyberpad Technologies 2011
#ifndef QRWIDGET_H_
#define QRWIDGET_H_

#include <QWidget>

class QRWidget : public QWidget
{
public:
	QRWidget();
	void RenderConnectionCode( QString& connectionCode );
	virtual void paintEvent( QPaintEvent* );

private:
	QString mConnectionCode;
};

#endif // QRWIDGET_H_
