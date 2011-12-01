// $Id$
// Description: Mouse emulation handler
// (C) Cyberpad Technologies 2011
#ifndef MOUSE_H_
#define MOUSE_H_

#include <QCursor>

#include "OS.h"

// OS-specific includes
#ifdef OS_WINDOWS
    #include <Windows.h>
    #include <Winuser.h>
#endif
#ifdef OS_LINUX
    #include <X11/Xlib.h>
#endif

class Mouse
{
    // static
private:
    static Mouse* sInstance;

    #ifdef OS_LINUX

    #endif

public:
    static Mouse& Get();
	static void Clear();

    static const unsigned int sLeft = 0;
    static const unsigned int sRight = 1;
    static const unsigned int sMiddle = 2;
    static const unsigned int sMouse4 = 3;
    static const unsigned int sMouse5 = 4;

    // non-static
private:
    #ifdef OS_LINUX
    Display* mDisplay;
    #endif

    Mouse();
    ~Mouse();
    void Init();
    void Deinit();

    #ifdef OS_WINDOWS
    void WindowsButton(unsigned int button, bool up);

    #endif //OSWINDOWS

    #ifdef OS_LINUX
    void LinuxButton(unsigned int button, bool up);
    #endif //OS_LINUX

public:
    // Position
    QPoint Position() const;
    Mouse& SetPosition(QPoint position);
    Mouse& MovePosition(QPoint delta);

    // Clicks
    void Down(unsigned int button);
    void Up(unsigned int button);

    // Scrolls(TM)
    void Scroll(signed int horizontal, signed int vertical);
};

#endif // MOUSE_H_
