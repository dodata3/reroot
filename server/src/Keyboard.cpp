// $Id$
// Description: Emulates keyboard input in a cross platform manner
// (C) Cyberpad Technologies 2011
#include <cstddef>
#include <limits>
#include "Keyboard.h"
#include "DirectInput.h"
#include <QDebug>

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

	// Release all modifier keys
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

	/*
	for (Keycode i = 0; i < std::numeric_limits<Keycode>::max(); ++i)
	{
		Up(i);
	}
	Up(std::numeric_limits<Keycode>::max());
	*/
	// Just unpress ASCII printing characters
	for (Keycode i = 32; i < 127; ++i)
	{
		Up(i);
	}

}

#ifdef OS_WINDOWS
Keyboard::Keycode Keyboard::WindowsDIConvert(Keycode key)
{
	// Convert to DirectInput compatible keycodes
	switch (key)
	{
	
		return DIKEYBOARD_ESCAPE;
	case '1':
		return DIKEYBOARD_1;
	case '2':
		return DIKEYBOARD_2;
	case '3':
		return DIKEYBOARD_3;
	case '4':
		return DIKEYBOARD_4;
	case '5':
		return DIKEYBOARD_5;
	case '6':
		return DIKEYBOARD_6;
	case '7':
		return DIKEYBOARD_7;
	case '8':
		return DIKEYBOARD_8;
	case '9':
		return DIKEYBOARD_9;
	case '0':
		return DIKEYBOARD_0;
	case '-':
		return DIKEYBOARD_MINUS;
	case '=':
		return DIKEYBOARD_EQUALS;
	
		return DIKEYBOARD_BACK;
	
		return DIKEYBOARD_TAB;
	case 'q':
		return DIKEYBOARD_Q;
	case 'w':
		return DIKEYBOARD_W;
	case 'e':
		return DIKEYBOARD_E;
	case 'r':
		return DIKEYBOARD_R;
	case 't':
		return DIKEYBOARD_T;
	case 'y':
		return DIKEYBOARD_Y;
	case 'u':
		return DIKEYBOARD_U;
	case 'i':
		return DIKEYBOARD_I;
	case 'o':
		return DIKEYBOARD_O;
	case 'p':
		return DIKEYBOARD_P;
	
		return DIKEYBOARD_LBRACKET;
	
		return DIKEYBOARD_RBRACKET;
	
		return DIKEYBOARD_RETURN;
	
		return DIKEYBOARD_LCONTROL;
	case 'a':
		return DIKEYBOARD_A;
	case 's':
		return DIKEYBOARD_S;
	case 'd':
		return DIKEYBOARD_D;
	case 'f':
		return DIKEYBOARD_F;
	case 'g':
		return DIKEYBOARD_G;
	case 'h':
		return DIKEYBOARD_H;
	case 'j':
		return DIKEYBOARD_J;
	case 'k':
		return DIKEYBOARD_K;
	case 'l':
		return DIKEYBOARD_L;
	case ';':
		return DIKEYBOARD_SEMICOLON;
	case '\'':
		return DIKEYBOARD_APOSTROPHE;
	case '`':
		return DIKEYBOARD_GRAVE;
	
		return DIKEYBOARD_LSHIFT;
	case '\\':
		return DIKEYBOARD_BACKSLASH;
	case 'z':
		return DIKEYBOARD_Z;
	case 'x':
		return DIKEYBOARD_X;
	case 'c':
		return DIKEYBOARD_C;
	case 'v':
		return DIKEYBOARD_V;
	case 'b':
		return DIKEYBOARD_B;
	case 'n':
		return DIKEYBOARD_N;
	case 'm':
		return DIKEYBOARD_M;
	case ',':
		return DIKEYBOARD_COMMA;
	case '.':
		return DIKEYBOARD_PERIOD;
	case '/':
		return DIKEYBOARD_SLASH;
	
		return DIKEYBOARD_RSHIFT;
	case '*':
		return DIKEYBOARD_MULTIPLY;
	
		return DIKEYBOARD_LMENU;
	case ' ':
		return DIKEYBOARD_SPACE;
	
		return DIKEYBOARD_CAPITAL;
	
		return DIKEYBOARD_F1;
		return DIKEYBOARD_F2;
		return DIKEYBOARD_F3;
		return DIKEYBOARD_F4;
		return DIKEYBOARD_F5;
		return DIKEYBOARD_F6;
		return DIKEYBOARD_F7;
		return DIKEYBOARD_F8;
		return DIKEYBOARD_F9;
		return DIKEYBOARD_F10;
		return DIKEYBOARD_NUMLOCK;
		return DIKEYBOARD_SCROLL;
		return DIKEYBOARD_NUMPAD7;
		return DIKEYBOARD_NUMPAD8;
		return DIKEYBOARD_NUMPAD9;
		return DIKEYBOARD_SUBTRACT;
		return DIKEYBOARD_NUMPAD4;
		return DIKEYBOARD_NUMPAD5;
		return DIKEYBOARD_NUMPAD6;
	case '+':
		return DIKEYBOARD_ADD;
		return DIKEYBOARD_NUMPAD1;
		return DIKEYBOARD_NUMPAD2;
		return DIKEYBOARD_NUMPAD3;
		return DIKEYBOARD_NUMPAD0;
		return DIKEYBOARD_DECIMAL;
		return DIKEYBOARD_OEM_102;
		return DIKEYBOARD_F11;
		return DIKEYBOARD_F12;
		return DIKEYBOARD_F13;
		return DIKEYBOARD_F14;
		return DIKEYBOARD_F15;
		return DIKEYBOARD_KANA;
		return DIKEYBOARD_ABNT_C1;
		return DIKEYBOARD_CONVERT;
		return DIKEYBOARD_NOCONVERT;
		return DIKEYBOARD_YEN;
		return DIKEYBOARD_ABNT_C2;
		return DIKEYBOARD_NUMPADEQUALS;
		return DIKEYBOARD_PREVTRACK;
	case '@':
		return DIKEYBOARD_AT;
	case ':':
		return DIKEYBOARD_COLON;
	case '_':
		return DIKEYBOARD_UNDERLINE;
		return DIKEYBOARD_KANJI;
		return DIKEYBOARD_STOP;
		return DIKEYBOARD_AX;
		return DIKEYBOARD_UNLABELED;
		return DIKEYBOARD_NEXTTRACK;
		return DIKEYBOARD_NUMPADENTER;
		return DIKEYBOARD_RCONTROL;
		return DIKEYBOARD_MUTE;
		return DIKEYBOARD_CALCULATOR;
		return DIKEYBOARD_PLAYPAUSE;
		return DIKEYBOARD_MEDIASTOP;
		return DIKEYBOARD_VOLUMEDOWN;
		return DIKEYBOARD_VOLUMEUP;
		return DIKEYBOARD_WEBHOME;
		return DIKEYBOARD_NUMPADCOMMA;
		return DIKEYBOARD_DIVIDE;
		return DIKEYBOARD_SYSRQ;
		return DIKEYBOARD_RMENU;
		return DIKEYBOARD_PAUSE;
		return DIKEYBOARD_HOME;
		return DIKEYBOARD_UP;
		return DIKEYBOARD_PRIOR;
		return DIKEYBOARD_LEFT;
		return DIKEYBOARD_RIGHT;
		return DIKEYBOARD_END;
		return DIKEYBOARD_DOWN;
		return DIKEYBOARD_NEXT;
		return DIKEYBOARD_INSERT;
		return DIKEYBOARD_DELETE;
		return DIKEYBOARD_LWIN;
		return DIKEYBOARD_RWIN;
		return DIKEYBOARD_APPS;
		return DIKEYBOARD_POWER;
		return DIKEYBOARD_SLEEP;
		return DIKEYBOARD_WAKE;
		return DIKEYBOARD_WEBSEARCH;
		return DIKEYBOARD_WEBFAVORITES;
		return DIKEYBOARD_WEBREFRESH;
		return DIKEYBOARD_WEBSTOP;
		return DIKEYBOARD_WEBFORWARD;
		return DIKEYBOARD_WEBBACK;
		return DIKEYBOARD_MYCOMPUTER;
		return DIKEYBOARD_MAIL;
		return DIKEYBOARD_MEDIASELECT;
	default:
		qDebug() << "Unknown DI keycode " << key << "!\n";
		return 0;
	}
}

void Keyboard::WindowsModifierKey(ModifierKeycode key, bool up)
{
	// Convert modifier keycode to Windows
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
	//Keycode convertedkey = WindowsDIConvert(key);
	//qDebug() << "Converted keycode " << key << " to DI keycode " << convertedkey << ".\n";
	Keycode convertedkey = key;
    INPUT input;
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = 0;
    //input.ki.wScan = key;
	input.ki.wScan = convertedkey;
    input.ki.dwFlags = KEYEVENTF_UNICODE | (up ? KEYEVENTF_KEYUP : 0);
	//input.ki.dwFlags = (up ? KEYEVENTF_KEYUP : 0);
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
