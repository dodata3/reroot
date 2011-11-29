// $Id$
// Description: Widget which renders a QR Code from a connection code
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include "QRWidget.h"
#include <string.h>
#include "qrencode.h"
#include <QPainter>
using namespace std;

QRWidget::QRWidget()
{
    QPalette Pal( palette() );
    Pal.setColor( QPalette::Background, Qt::white );
    this->setAutoFillBackground(true);
    this->setPalette(Pal);
}

void QRWidget::paintEvent( QPaintEvent* paintEvent )
{
	string address = mConnectionCode.toStdString();
	QRcode* code = QRcode_encodeString( address.c_str(), 0, QR_ECLEVEL_L, QR_MODE_8, 0 );

	unsigned int codeWidth = code->width;
	unsigned int squareWidth = width() / ( codeWidth + 2 );

	QPainter painter( this );
    for(int i = code->width -1; i >=0; i--)
    {
        for(int j = 0; j < code->width; j++)
        {
            unsigned char data = code->data[ ( code->width * i ) + j ];
            QRect rect = QRect( ( j + 1 ) * squareWidth, ( i + 1 ) * squareWidth, squareWidth, squareWidth );
            if( data & 00000001 ) painter.fillRect( rect, Qt::black ); else painter.fillRect( rect, Qt::white );
		}
    }
}

void QRWidget::RenderConnectionCode( QString& connectionCode )
{
	mConnectionCode = connectionCode;
	repaint();
}
