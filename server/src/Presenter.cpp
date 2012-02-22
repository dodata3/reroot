#include "Presenter.h"

#include <QApplication>

PresenterPoint::PresenterPoint(QWidget* parent, Qt::WindowFlags f) : QLabel(parent, f)
{

}

bool PresenterPoint::event(QEvent* ev)
{
	/*
	switch (ev->type())
	{
	case QEvent::KeyPress:
	case QEvent::KeyRelease:
	case QEvent::MouseButtonPress:
	case QEvent::MouseButtonRelease:
	case QEvent::MouseButtonDblClick:
		
		return false;
	default:
		ev->accept();
		//ev->ignore();
		return true;
	}
	return true;*/
	return QLabel::event(ev);
}

Presenter* Presenter::sInstance = NULL;

const char* Presenter::sPointFile = "images/presenter_dot.png";

QPixmap* Presenter::sPointImage = NULL;

const QSize Presenter::sPointSize(32, 32);

Presenter& Presenter::Get()
{
    if (sInstance == NULL)
    {
		sPointImage = new QPixmap(Presenter::sPointFile);
        sInstance = new Presenter();
    }
    return *sInstance;
}

void Presenter::Clear()
{
	delete sInstance;
	delete sPointImage;
	sInstance = NULL;
	sPointImage = NULL;
}

Presenter::Presenter() : mWindow(NULL, Qt::FramelessWindowHint | Qt::SplashScreen | Qt::WindowStaysOnTopHint)
{
    Init();
}

Presenter::~Presenter()
{
    Deinit();
}

void Presenter::Init()
{
	mActive = false;
	mWindow.setStyleSheet("background:transparent;");
	mWindow.setAttribute(Qt::WA_TranslucentBackground);
	mWindow.setAttribute(Qt::WA_TransparentForMouseEvents);
	mWindow.setFocusPolicy(Qt::NoFocus);
	mWindow.setPixmap(*sPointImage);
	Point(QPoint(640, 640));
	//mWindow.setWindowFlags(Qt::FramelessWindowHint);
}

void Presenter::Deinit()
{
	Stop();
}

void Presenter::Start()
{
	if (!mActive) {
		mActive = true;
		//QApplication::setOverrideCursor(mCursor);
		mWindow.show();
	}
}

void Presenter::Stop()
{
	if (mActive) {
		mActive = false;
		//QApplication::restoreOverrideCursor();
		mWindow.hide();
	}
}

void Presenter::Point(QPoint pos)
{
	mWindow.move(pos);
}