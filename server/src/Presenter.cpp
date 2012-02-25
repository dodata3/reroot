#include "Presenter.h"

#include <QApplication>
#include <QEvent>

PresenterPoint::PresenterPoint(QWidget* parent, Qt::WindowFlags f) : QLabel(parent, f)
{

}

bool PresenterFilter::eventFilter(QObject* obj, QEvent* ev)
{
	//QKeyEvent* pKeyEvent = qobject_cast<QKeyEvent*>(ev);
    //QMouseEvent* pMouseEvent = qobject_cast<QMouseEvent*>(ev);
	//QKeyEvent* pKeyEvent = static_cast<QKeyEvent*>(ev);
	//QMouseEvent* pMouseEvent = static_cast<QMouseEvent*>(ev);

	switch (ev->type())
	{
	case QEvent::MouseButtonDblClick:
	case QEvent::MouseButtonPress:
	case QEvent::MouseButtonRelease:
		//QDebug() << "Filter happens\n";
		return true;
	default:
		return QObject::eventFilter(obj, ev);
	}
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
	mWindow.installEventFilter(&mFilter);
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