#include "Mouse.h"

Mouse* Mouse::sInstance = NULL;

Mouse& Mouse::get()
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

void Mouse::Init()
{
    #ifdef OS_WINDOWS

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

void Mouse::down(unsigned int button)
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
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif
}

void Mouse::up(unsigned int button)
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
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif
}

void Mouse::scroll(signed int horizontal, signed int vertical)
{
    // Scroll wheel X clicks, positive is scrolling up/right
    #ifdef OS_WINDOWS
        INPUT input[2];
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
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif
}
