// $Id$
// Description: Keyboard emulation handler
// (C) Cyberpad Technologies 2011
#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include "Global.h"

// OS-specific includes
#ifdef OS_WINDOWS
    #include <windows.h>
#endif

class Keyboard
{
    // static
private:
    static Keyboard* sInstance;

public:
    static Keyboard& get();

    // Look into Qt's localization functionality
    // to see if it can translate letter to keycode
    // across different keyboard layouts, etc.
    typedef unsigned int Keycode;

    // non-static
private:
    Keyboard();
    void Init();

public:
    void down(Keycode key);
    void up(Keycode key);
};

#endif // KEYBOARD_H_

