#include "Keyboard.h"

#include <cstdio>

Keyboard* Keyboard::sInstance = NULL;

Keyboard& Keyboard::get()
{
    if (sInstance == NULL)
    {
        sInstance = new Keyboard();
    }
    return *sInstance;
}

Keyboard::Keyboard()
{
    Init();
}

void Keyboard::Init()
{
    #ifdef OS_WINDOWS

    #endif
}

void Keyboard::down(Keycode key)
{
    #ifdef OS_WINDOWS
        INPUT input;
        input.type = INPUT_KEYBOARD;
        input.ki.wVk = 0;
        input.ki.wScan = key;
        input.ki.dwFlags = KEYEVENTF_UNICODE;
        input.ki.time = 0;
        input.ki.dwExtraInfo = (ULONG_PTR)NULL;

        if (SendInput(1, &input, sizeof(INPUT)) != 1)
        {
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif

    #ifdef OS_LINUX
        XEvent event;
        memset(&event, 0, sizeof(event));
        event.xkey.button = key;
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
        event.type = KeyPress;
        if (XSendEvent(mDisplay, PointerWindow, True, ButtonReleaseMask, &event))
        {
            // error
        }
        XFlush(mDisplay);
    #endif
}

void Keyboard::up(Keycode key)
{
    #ifdef OS_WINDOWS
        INPUT input;
        input.type = INPUT_KEYBOARD;
        input.ki.wVk = 0;
        input.ki.wScan = key;
        input.ki.dwFlags = KEYEVENTF_UNICODE | KEYEVENTF_KEYUP;
        input.ki.time = 0;
        input.ki.dwExtraInfo = (ULONG_PTR)NULL;

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
