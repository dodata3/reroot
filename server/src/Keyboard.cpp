// $Id$
// Description: Emulates keyboard input in a cross platform manner
// (C) Cyberpad Technologies 2011
#include <cstddef>
#include <limits>
#include "Keyboard.h"

Keyboard* Keyboard::sInstance = NULL;

Keyboard& Keyboard::Get()
{
    if (sInstance == NULL)
    {
        sInstance = new Keyboard();
    }
    return *sInstance;
}

void Keyboard::Clear()
{
	delete sInstance;
	sInstance = NULL;
}

Keyboard::Keyboard()
{
    Init();
}

Keyboard::~Keyboard()
{
    Deinit();
}

void Keyboard::Init()
{
    #ifdef OS_WINDOWS

    #endif //OS_WINDOWS
}

void Keyboard::Deinit()
{
	qDebug() << "Killing keyboard\n";
	// Release all keys
	ModifierUp(ModCtrlL);
	ModifierUp(ModCtrlR);
	ModifierUp(ModCtrl);
	ModifierUp(ModAltL);
	ModifierUp(ModAltR);
	ModifierUp(ModAlt);
	ModifierUp(ModShiftL);
	ModifierUp(ModShiftR);
	ModifierUp(ModShift);
	ModifierUp(ModMetaL);
	ModifierUp(ModMetaR);
	ModifierUp(ModMeta);

	for (Keycode i = 0; i < std::numeric_limits<Keycode>::max(); ++i)
	{
		Up(i);
	}
	Up(std::numeric_limits<Keycode>::max());
}

#ifdef OS_WINDOWS
void Keyboard::WindowsModifierKey(ModifierKeycode key, bool up)
{
    WORD wkey;
    switch(key)
    {
    case ModCtrlL:
        wkey = VK_LCONTROL;
        break;
    case ModCtrlR:
        wkey = VK_RCONTROL;
        break;
    case ModCtrl:
        wkey = VK_CONTROL;
        break;
    case ModAltL:
        wkey = VK_LMENU;
        break;
    case ModAltR:
        wkey = VK_RMENU;
        break;
    case ModAlt:
        wkey = VK_MENU;
        break;
    case ModShiftL:
        wkey = VK_LSHIFT;
        break;
    case ModShiftR:
        wkey = VK_RSHIFT;
        break;
    case ModShift:
        wkey = VK_SHIFT;
        break;
    case ModMetaL:
        wkey = VK_LWIN; // This might not have any effect, Windows key may be read-only at user level
        break;
    case ModMetaR:
        wkey = VK_RWIN;
        break;
    case ModMeta:
        wkey = VK_LWIN; // Windows has no generic 'sideless' windows key, so just use left
        break;
    default:
        wkey = 0;
        break;
    }
    INPUT input;
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = wkey;
    input.ki.wScan = 0;
    input.ki.dwFlags = up ? KEYEVENTF_KEYUP : 0;
    input.ki.time = 0;
    input.ki.dwExtraInfo = (ULONG_PTR)NULL;

    if (SendInput(1, &input, sizeof(INPUT)) != 1)
    {
        WindowsError("SendInput");
    }
}

void Keyboard::WindowsKey(Keycode key, bool up)
{
    INPUT input;
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = 0;
    input.ki.wScan = key;
    input.ki.dwFlags = KEYEVENTF_UNICODE | (up ? KEYEVENTF_KEYUP : 0);
    input.ki.time = 0;
    input.ki.dwExtraInfo = (ULONG_PTR)NULL;

    if (SendInput(1, &input, sizeof(INPUT)) != 1)
    {
        WindowsError("SendInput");
    }
}
#endif //OS_WINDOWS

#ifdef OS_LINUX
void Keyboard::LinuxModifierKey(ModifierKeycode key, bool up)
{

}

void Keyboard::LinuxKey(Keycode key, bool up)
{
    // UP NOT YET IMPLEMENTED
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
}
#endif //OS_LINUX

void Keyboard::ModifierUp(ModifierKeycode key)
{
    #ifdef OS_WINDOWS
        WindowsModifierKey(key, true);
    #endif
}

void Keyboard::ModifierDown(ModifierKeycode key)
{
    #ifdef OS_WINDOWS
       WindowsModifierKey(key, false);
    #endif
}

void Keyboard::Down(Keycode key)
{
    #ifdef OS_WINDOWS
        WindowsKey(key, false);
    #endif

    #ifdef OS_LINUX
        LinuxKey(key, false);
    #endif
}

void Keyboard::Up(Keycode key)
{
    #ifdef OS_WINDOWS
        WindowsKey(key, true);
    #endif
	
	#ifdef OS_LINUX
		LinuxKey(key, true);
	#endif
}
