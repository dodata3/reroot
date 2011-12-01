// $Id$
// Description: Keyboard emulation handler
// (C) Cyberpad Technologies 2011
#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include "OS.h"

class Keyboard
{
    // static
private:
    static Keyboard* sInstance;

public:
    static Keyboard& Get();
	static void Clear();

    // Look into Qt's localization functionality
    // to see if it can translate letter to keycode
    // across different keyboard layouts, etc.
    typedef unsigned int Keycode;

    enum ModifierKeycode
    {
        ModNone     = 0x0000,

        ModCtrlL    = 0x0001,
        ModCtrlR    = 0x0002,
        ModCtrl     = 0x0001 + 0x0002,

        ModAltL     = 0x0004,
        ModAltR     = 0x0008,
        ModAlt      = 0x0004 + 0x0008,

        ModShiftL   = 0x0010,
        ModShiftR   = 0x0020,
        ModShift    = 0x0010 + 0x0020,

        ModMetaL    = 0x0040,
        ModMetaR    = 0x0080,
        ModMeta     = 0x0040 + 0x0080,

        ModAltGr    = 0x0100
    };

    // non-static
private:
    Keyboard();
    ~Keyboard();

    void Init();
    void Deinit();

    #ifdef OS_WINDOWS
	Keycode WindowsDIConvert(Keycode key);

    void WindowsModifierKey(ModifierKeycode key, bool up);
    void WindowsKey(Keycode key, bool up);
    #endif //OS_WINDOWS

    #ifdef OS_LINUX
    void LinuxModifierKey(ModifierKeycode key, bool up);
    void LinuxKey(Keycode key, bool up);
    #endif // OS_LINUX

public:
    void ModifierDown(ModifierKeycode key);
    void ModifierUp(ModifierKeycode key);

    void Down(Keycode key);
    void Up(Keycode key);


};

#endif // KEYBOARD_H_

