#include "Keyboard.h"

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
        input.ki.wVk = 0;
        input.ki.wScan = key;
        input.ki.dwFlags = KEYEVENTF_SCANCODE;
        input.ki.time = 0;
        input.ki.dwExtraInfo = (ULONG_PTR)NULL;

        if (SendInput(1, &input, sizeof(INPUT)) != 0)
        {
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif
}

void Keyboard::up(Keycode key)
{
    #ifdef OS_WINDOWS
        INPUT input;
        input.ki.wVk = 0;
        input.ki.wScan = key;
        input.ki.dwFlags = KEYEVENTF_SCANCODE | KEYEVENTF_KEYUP;
        input.ki.time = 0;
        input.ki.dwExtraInfo = (ULONG_PTR)NULL;

        if (SendInput(1, &input, sizeof(INPUT)) != 0)
        {
            switch (GetLastError())
            {
                default:
                    break;
            }
        }
    #endif
}
