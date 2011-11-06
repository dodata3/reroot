// $Id$
// Description: Widget which renders a QR Code from a connection code
// (C) Cyberpad Technologies 2011
#include <QtGui>
#include "QRWidget.h"
#include <string.h>
#include "QREncode.h"
#include <QPainter>
using namespace std;

QRWidget::QRWidget()
{
    resize( 200, 200 );
}

void QRWidget::paintEvent( QPaintEvent* paintEvent )
{
	const QColor white(255, 255, 255);
    const QColor black(0,0,0);
    const char* address;
    


	string tempAddress = mConnectionCode.toStdString();
	address = tempAddress.c_str();


	QRcode* code = QRcode_encodeString(address, 0, QR_ECLEVEL_L, QR_MODE_8, 1);

	unsigned int width = code->width;
	//unsigned char** qrData;
	unsigned char* current = code->data;
	unsigned int height = code->width;

	//painter.begin(this);
    for(int i = 0; i < code->width; i++)
    {
        for(int j = 0; j < code->width; j++)
        {
            unsigned char mask = 00000001; // 00000001
            unsigned char data = code->data[(code->width*j)+i];
            if( data & mask )
            {
                // Draw Black
				QBrush b;
				b.setColor(black);
				QPen p;
				p.setWidth(1);
				p.setColor(black);
				QPainter painter(this);
				painter.setPen(p);
				painter.setBrush(b);
                const QRect rect(j*5, i*5,5,5);
				painter.fillRect(rect, QBrush(black));
				painter.drawRect(rect);   
            }
            else
            {
				QBrush b;
				b.setColor(white);
				QPen p;
				p.setWidth(1);
				p.setColor(white);
				QPainter painter(this);
				painter.setPen(p);
				painter.setBrush(b);
                const QRect rect(j*5, i*5,5,5);
				painter.fillRect(rect, QBrush(white));
				painter.drawRect(rect);
                
                
            }
		}
    }
	//painter.end();
}

void QRWidget::RenderConnectionCode( QString& connectionCode )
{
	mConnectionCode = connectionCode;
	repaint();
	

    

}
