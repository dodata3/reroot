// $Id$
// Description: Map control keys to Unicode Private use characters
// (C) Cyberpad Technologies 2012
#ifndef UNICODE_H_
#define UNICODE_H_

static const unsigned int ukey_ascii_min_		= 0x0000;
static const unsigned int ukey_ascii_max_		= 0x007F;

static const unsigned int ukey_private_min_		= 0xE000;
static const unsigned int ukey_private_max_		= 0xF8FF;

enum Ukey
{
	ukey_ascii_min				= ukey_ascii_min_,
	ukey_ascii_max				= ukey_ascii_max_,

	ukey_private_min			= ukey_private_min_,

	ukey_cancel					= ukey_private_min_, // ctrl+c signal

	ukey_backspace,
	ukey_tab,
	ukey_clear,
	ukey_return,

	ukey_shift_l,
	ukey_shift_r,

	ukey_ctrl_l,
	ukey_ctrl_r,
	
	ukey_alt_l,
	ukey_alt_r,

	ukey_pause,

	ukey_capslock,
	ukey_numlock,
	ukey_scrolllock,

	ukey_escape,

	ukey_pgup,
	ukey_pgdn,

	ukey_home,
	ukey_end,

	ukey_up,
	ukey_down,
	ukey_left,
	ukey_right,

	ukey_select, // ???
	ukey_print, // ctrl+p?
	ukey_execute, // ???
	
	ukey_printscreen,

	ukey_insert,
	ukey_delete,

	ukey_help,
	
	ukey_meta_l,
	ukey_meta_r,

	ukey_apps, // context menu? (button between altr and ctrlr)

	ukey_power,
	ukey_sleep,
	ukey_wake,

	ukey_f1,
	ukey_f2,
	ukey_f3,
	ukey_f4,
	ukey_f5,
	ukey_f6,
	ukey_f7,
	ukey_f8,
	ukey_f9,
	ukey_f10,
	ukey_f11,
	ukey_f12,
	ukey_f13,
	ukey_f14,
	ukey_f15,

	ukey_num0,
	ukey_num1,
	ukey_num2,
	ukey_num3,
	ukey_num4,
	ukey_num5,
	ukey_num6,
	ukey_num7,
	ukey_num8,
	ukey_num9,

	
	//ukey_
	
	ukey_unknown,

	ukey_private_max				= ukey_private_max_
};

#endif // UNICODE_H_
