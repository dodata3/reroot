// $Id$
// Description: Emulates keyboard input in a cross platform manner
// (C) Cyberpad Technologies 2011
#include <cstddef>
#include <limits>
#include "Keyboard.h"
#include "DirectInput.h"
#include "Unicode.h"
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
	// Release all keys
	/*
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
	ModifierUp(ModMeta);*/

	/*
	for (Keycode i = 0; i < std::numeric_limits<Keycode>::max(); ++i)
	{
		Up(i);
	}
	Up(std::numeric_limits<Keycode>::max());
	*/
	// Just unpress ASCII printing characters
	/*
	for (Keycode i = 32; i < 127; ++i)
	{
		Up(i);
	}
	*/

}

#ifdef OS_WINDOWS

DIkey Keyboard::WindowsDIConvert(Ukey key)
{
	// Convert to DirectInput compatible keycodes
	//if ((key < ukey_ascii_min || key > ukey_ascii_max) && (key < ukey_private_min || key > ukey_private_max))
	//{
	//	return dikey_na;
	//}

	switch (key)
	{
	case ukey_escape:
		return dikey_escape;
	case '1':
		return dikey_1;
	case '2':
		return dikey_2;
	case '3':
		return dikey_3;
	case '4':
		return dikey_4;
	case '5':
		return dikey_5;
	case '6':
		return dikey_6;
	case '7':
		return dikey_7;
	case '8':
		return dikey_8;
	case '9':
		return dikey_9;
	case '0':
		return dikey_0;
	case '-':
		return dikey_minus;
	case '=':
		return dikey_equals;
	case ukey_backspace:
		return dikey_backspace;
	case '\t':
	case ukey_tab:
		return dikey_tab;
	case 'q':
		return dikey_q;
	case 'w':
		return dikey_w;
	case 'e':
		return dikey_e;
	case 'r':
		return dikey_r;
	case 't':
		return dikey_t;
	case 'y':
		return dikey_y;
	case 'u':
		return dikey_u;
	case 'i':
		return dikey_i;
	case 'o':
		return dikey_o;
	case 'p':
		return dikey_p;
	case '[':
		return dikey_lbracket;
	case ']':
		return dikey_rbracket;
	case '\n':
	case ukey_return:
		return dikey_return;
	case ukey_ctrl_l:
		return dikey_ctrl_l;
	case 'a':
		return dikey_a;
	case 's':
		return dikey_s;
	case 'd':
		return dikey_d;
	case 'f':
		return dikey_f;
	case 'g':
		return dikey_g;
	case 'h':
		return dikey_h;
	case 'j':
		return dikey_j;
	case 'k':
		return dikey_k;
	case 'l':
		return dikey_l;
	case ';':
		return dikey_semicolon;
	case '\'':
		return dikey_apostrophe;
	case '`':
		return dikey_grave;
	case ukey_shift_l:
		return dikey_shift_l;
	case '\\':
		return dikey_oem_102; // ?
	case 'z':
		return dikey_z;
	case 'x':
		return dikey_x;
	case 'c':
		return dikey_c;
	case 'v':
		return dikey_v;
	case 'b':
		return dikey_b;
	case 'n':
		return dikey_n;
	case 'm':
		return dikey_m;
	case ',':
		return dikey_comma;
	case '.':
		return dikey_period;
	case '/':
		return dikey_slash;
	case ukey_shift_r:
		return dikey_shift_r;
	case '*':
		return dikey_multiply;
	case ukey_alt_l:
		return dikey_alt_l;
	case ' ':
		return dikey_space;
	case ukey_capslock:
		return dikey_capslock;
	case ukey_f1:
		return dikey_f1;
	case ukey_f2:
		return dikey_f2;
	case ukey_f3:
		return dikey_f3;
	case ukey_f4:
		return dikey_f4;
	case ukey_f5:
		return dikey_f5;
	case ukey_f6:
		return dikey_f6;
	case ukey_f7:
		return dikey_f7;
	case ukey_f8:
		return dikey_f8;
	case ukey_f9:
		return dikey_f9;
	case ukey_f10:
		return dikey_f10;
	case ukey_numlock:
		return dikey_numlock;
	case ukey_scrolllock:
		return dikey_scrolllock;
	case ukey_num7:
		return dikey_num7;
	case ukey_num8:
		return dikey_num8;
	case ukey_num9:
		return dikey_num9;
//	case '-':
//		return dikey_SUBTRACT;
	case ukey_num4:
		return dikey_num4;
	case ukey_num5:
		return dikey_num5;
	case ukey_num6:
		return dikey_num6;
//	case '+':
//		return dikey_ADD;
	case ukey_num1:
		return dikey_num1;
	case ukey_num2:
		return dikey_num2;
	case ukey_num3:
		return dikey_num3;
	case ukey_num0:
		return dikey_num0;
//		return dikey_DECIMAL;
//	case '\\':
//		return dikey_oem_102;
	case ukey_f11:
		return dikey_f11;
	case ukey_f12:
		return dikey_f12;
	case ukey_f13:
		return dikey_f13;
	case ukey_f14:
		return dikey_f14;
	case ukey_f15:
		return dikey_f15;
	/*
		return dikey_KANA;
		return dikey_ABNT_C1;
		return dikey_CONVERT;
		return dikey_NOCONVERT;
		return dikey_YEN;
		return dikey_ABNT_C2;
		return dikey_NUMPADEQUALS;
		return dikey_PREVTRACK;
	//case '@':
		return dikey_AT;
	//case ':':
		return dikey_COLON;
	//case '_':
		return dikey_UNDERLINE;
		return dikey_KANJI;
		return dikey_STOP;
		return dikey_AX;
		return dikey_UNLABELED;
		return dikey_NEXTTRACK;
		return dikey_NUMPADENTER;
		return dikey_RCONTROL;
		return dikey_MUTE;
		return dikey_CALCULATOR;
		return dikey_PLAYPAUSE;
		return dikey_MEDIASTOP;
		return dikey_VOLUMEDOWN;
		return dikey_VOLUMEUP;
		return dikey_WEBHOME;
		return dikey_NUMPADCOMMA;
		return dikey_DIVIDE;
		return dikey_SYSRQ;*/
	case ukey_alt_r:
		return dikey_alt_r;
	case ukey_pause:
		return dikey_pause;
	case ukey_home:
		return dikey_home;
	case ukey_up:
		return dikey_up;
	case ukey_pgup:
		return dikey_pgup;
	case ukey_left:
		return dikey_left;
	case ukey_right:
		return dikey_right;
	case ukey_end:
		return dikey_end;
	case ukey_down:
		return dikey_down;
	case ukey_pgdn:
		return dikey_pgdn;
	case ukey_insert:
		return dikey_insert;
	case ukey_delete:
		return dikey_delete;
	case ukey_meta_l:
		return dikey_meta_l;
	case ukey_meta_r:
		return dikey_meta_r;
	case ukey_apps:
		return dikey_apps;
	case ukey_power:
		return dikey_power;
	case ukey_sleep:
		return dikey_sleep;
	case ukey_wake:
		return dikey_wake;
		/*
		return dikey_WEBSEARCH;
		return dikey_WEBFAVORITES;
		return dikey_WEBREFRESH;
		return dikey_WEBSTOP;
		return dikey_WEBFORWARD;
		return dikey_WEBBACK;
		return dikey_MYCOMPUTER;
		return dikey_MAIL;
		return dikey_MEDIASELECT;*/
	default:
		//qDebug() << "Unknown DI keycode " << key << "!\n";
		//return 0;
		return dikey_na;
	}
}

void Keyboard::WindowsKey(Ukey key, bool up)
{
    INPUT input;
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = 0;
    input.ki.wScan = WindowsDIConvert( key );
	input.ki.dwFlags = ( up ? KEYEVENTF_KEYUP : 0 );
	if (input.ki.wScan == dikey_na) {
		input.ki.wScan = key;
		input.ki.dwFlags |= KEYEVENTF_UNICODE;
	} else {
		input.ki.dwFlags |= KEYEVENTF_SCANCODE;
	}

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

void Keyboard::Down(Ukey key)
{
    #ifdef OS_WINDOWS
        WindowsKey(key, false);
    #endif

    #ifdef OS_LINUX
        LinuxKey(key, false);
    #endif
}

void Keyboard::Up(Ukey key)
{
    #ifdef OS_WINDOWS
        WindowsKey(key, true);
    #endif

	#ifdef OS_LINUX
		LinuxKey(key, true);
	#endif
}
