// $Id$
// Description: Mouse emulation handler
// (C) Cyberpad Technologies 2011
#ifndef MOUSE_H_
#define MOUSE_H_

#include <QCursor>

#include "Global.h"

// OS-specific includes
#ifdef OS_WINDOWS
    #include <Windows.h>
    #include <Winuser.h>
    #include <Winable.h>
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
    static Mouse& get();

    static const unsigned int sLeft = 1;
    static const unsigned int sRight = 2;
    static const unsigned int sMiddle = 3;
    static const unsigned int sMouse4 = 4;
    static const unsigned int sMouse5 = 5;

    // non-static
private:
    #ifdef OS_LINUX
        Display* mDisplay;
    #endif

    Mouse();
    ~Mouse();
    void Init();

public:
    // Position
    QPoint Position() const;
    Mouse& SetPosition(QPoint position);
    Mouse& MovePosition(QPoint delta);

    // Clicks
    void down(unsigned int button);
    void up(unsigned int button);

    // Scrolls(TM)
    void scroll(signed int horizontal, signed int vertical);
};

#endif // MOUSE_H_
