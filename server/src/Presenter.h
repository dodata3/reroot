// $Id$
// Description: Keyboard emulation handler
// (C) Cyberpad Technologies 2011
#ifndef PRESENTER_H_
#define PRESENTER_H_

#include <QCursor>
#include <QPixmap>
#include <QLabel>

class PresenterPoint : public QLabel
{
public:
	PresenterPoint(QWidget* parent = 0, Qt::WindowFlags f = 0);
	bool event(QEvent* ev);
};

class Presenter
{
	//static
private:
	static Presenter* sInstance;

	static const char* sPointFile;

	static QPixmap* sPointImage;

	static const QSize sPointSize;

public:
    static Presenter& Get();
	static void Clear();

    // non-static
private:
	
	//QCursor mCursor;
	PresenterPoint mWindow;
	bool mActive;

    Presenter();
    ~Presenter();

    void Init();
    void Deinit();

public:
	void Start();
	void Stop();

	void Point(QPoint pos);
};

#endif // PRESENTER_H_
