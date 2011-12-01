#include "Mouse.h"
#include <cstring>
#include <iostream>

Mouse* Mouse::sInstance = NULL;

Mouse& Mouse::Get()
{
    if (sInstance == NULL)
    {
        sInstance = new Mouse();
    }
    return *sInstance;
}

Mouse::Mouse()
{
    Init();
}

Mouse::~Mouse()
{
    #ifdef OS_LINUX
        XCloseDisplay(mDisplay);
    #endif
}

void Mouse::Init()
{
    #ifdef OS_WINDOWS

    #endif
    #ifdef OS_LINUX
        if ((mDisplay = XOpenDisplay(NULL)) == NULL) // Open Display according to environment variable
        {
            // error
        }
    #endif
}

QPoint Mouse::Position() const
{
    // Cross Platform
    return QCursor::pos();
}

Mouse& Mouse::SetPosition(QPoint position)
{
    // Cross Platform
    QCursor::setPos(position);

    return *this;
}

Mouse& Mouse::MovePosition(QPoint delta)
{
    // Cross Platform
    QCursor::setPos(QCursor::pos() + delta);

    return *this;
}

void Mouse::Down(unsigned int button)
{
    #ifdef OS_WINDOWS
        INPUT input;
        input.type = INPUT_MOUSE;
        input.mi.dx = 0;
        input.mi.dy = 0;
        input.mi.mouseData = 0;
        input.mi.dwFlags = 0;
        input.mi.time = 0;
        input.mi.dwExtraInfo = (ULONG_PTR)NULL;

		std::cout << "Testing button..." << button << "\n";

        switch(button)
        {
            case sLeft:
                input.mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
                break;
            case sRight:
                input.mi.dwFlags = MOUSEEVENTF_RIGHTDOWN;
                break;
            case sMiddle:
                input.mi.dwFlags = MOUSEEVENTF_MIDDLEDOWN;
                break;
            case sMouse4:
                input.mi.dwFlags = MOUSEEVENTF_XDOWN;
                input.mi.mouseData = XBUTTON1;
                break;
            case sMouse5:
                input.mi.dwFlags = MOUSEEVENTF_XDOWN;
                input.mi.mouseData = XBUTTON2;
                break;
            default:
                // Invalid button
                break;
        }
        if (SendInput(1, &input, sizeof(INPUT)) != 1)
        {
            WindowsError("SendInput");
        }
    #endif
    #ifdef OS_LINUX
        XEvent event;
        memset(&event, 0, sizeof(event));
        switch (button)
        {
            case 1:
                event.xbutton.button = Button1;
                break;
            case 2:
                event.xbutton.button = Button2;
                break;
            case 3:
                event.xbutton.button = Button3;
                break;
            case 4:
                event.xbutton.button = Button4;
                break;
            case 5:
                event.xbutton.button = Button5;
                break;
            default:
                // Invalid button
                break;
        }
        event.xbutton.same_screen = True;
        event.xbutton.subwindow = DefaultRootWindow(mDisplay);
        while (event.xbutton.subwindow) // Find window
        {
            event.xbutton.window = event.xbutton.subwindow;
            XQueryPointer(mDisplay,
                          event.xbutton.window,
                          &event.xbutton.root,
                          &event.xbutton.subwindow,
                          &event.xbutton.x_root,
                          &event.xbutton.y_root,
                          &event.xbutton.x,
                          &event.xbutton.y,
                          &event.xbutton.state);
        }
        event.type = ButtonPress;
        if (XSendEvent(mDisplay, PointerWindow, True, ButtonPressMask, &event))
        {
            // error
        }
        XFlush(mDisplay);

    #endif
}

void Mouse::Up(unsigned int button)
{
    #ifdef OS_WINDOWS
        INPUT input;
        input.type = INPUT_MOUSE;
        input.mi.dx = 0;
        input.mi.dy = 0;
        input.mi.mouseData = 0;
        input.mi.dwFlags = 0;
        input.mi.time = 0;
        input.mi.dwExtraInfo = (ULONG_PTR)NULL;

        switch(button)
        {
            case sLeft:
                input.mi.dwFlags = MOUSEEVENTF_LEFTUP;
                break;
            case sRight:
                input.mi.dwFlags = MOUSEEVENTF_RIGHTUP;
                break;
            case sMiddle:
                input.mi.dwFlags = MOUSEEVENTF_MIDDLEUP;
                break;
            case sMouse4:
                input.mi.dwFlags = MOUSEEVENTF_XUP;
                input.mi.mouseData = XBUTTON1;
                break;
            case sMouse5:
                input.mi.dwFlags = MOUSEEVENTF_XUP;
                input.mi.mouseData = XBUTTON2;
                break;
            default:
                // Invalid button
                break;
        }
        if (SendInput(1, &input, sizeof(INPUT)) != 1)
        {
            WindowsError("SendInput");
        }
    #endif

    #ifdef OS_LINUX
        XEvent event;
        memset(&event, 0, sizeof(event));
        switch (button)
        {
            case 1:
                event.xbutton.button = Button1;
                break;
            case 2:
                event.xbutton.button = Button2;
                break;
            case 3:
                event.xbutton.button = Button3;
                break;
            case 4:
                event.xbutton.button = Button4;
                break;
            case 5:
                event.xbutton.button = Button5;
                break;
            default:
                // Invalid button
                break;
        }
        event.xbutton.same_screen = True;
        event.xbutton.subwindow = DefaultRootWindow(mDisplay);
        while (event.xbutton.subwindow) // Find window
        {
            event.xbutton.window = event.xbutton.subwindow;
            XQueryPointer(mDisplay,
                          event.xbutton.window,
                          &event.xbutton.root,
                          &event.xbutton.subwindow,
                          &event.xbutton.x_root,
                          &event.xbutton.y_root,
                          &event.xbutton.x,
                          &event.xbutton.y,
                          &event.xbutton.state);
        }
        event.type = ButtonPress;
        if (XSendEvent(mDisplay, PointerWindow, True, ButtonReleaseMask, &event))
        {
            // error
        }
        XFlush(mDisplay);
    #endif
}

void Mouse::Scroll(signed int horizontal, signed int vertical)
{
    // Scroll wheel X clicks, positive is scrolling up/right
    #ifdef OS_WINDOWS
        INPUT input[2]; // [horizontal, vertical]
        input[0].type = INPUT_MOUSE;
        input[0].mi.dx = 0;
        input[0].mi.dy = 0;
        #if 0//_WIN32_WINNT >= 0x0600
            // Horizontal scrolling requires Vista+
            // MOUSEEVENTF_HWHEEL is not defined anywhere in my windows headers
            // for some reason
            input[0].mi.mouseData = horizontal * WHEEL_DELTA;
            input[0].mi.dwFlags = MOUSEEVENTF_HWHEEL;
        #else
            // Do nothing if not supported
            input[0].mi.mouseData = 0;
            input[0].mi.dwFlags = 0;
        #endif
        input[0].mi.time = 0;
        input[0].mi.dwExtraInfo = (ULONG_PTR)NULL;

        input[1].type = INPUT_MOUSE;
        input[1].mi.dx = 0;
        input[1].mi.dy = 0;
        input[1].mi.mouseData = vertical * WHEEL_DELTA;
        input[1].mi.dwFlags = MOUSEEVENTF_WHEEL;
        input[1].mi.time = 0;
        input[1].mi.dwExtraInfo = (ULONG_PTR)NULL;

        if (SendInput(2, input, sizeof(INPUT)) != 2)
        {
            WindowsError("SendInput");
        }
    #endif
}
