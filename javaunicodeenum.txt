import java.util.*;
import java.lang.*;

public final class Ukey
{
	
	
	private static final int ukey_ascii_min		= 0x0000;
	private static final int ukey_ascii_max		= 0x007F;

	private static final int ukey_private_min	= 0xE000;
	private static final int ukey_private_max	= 0xF8FF;
	
	private static int ordinal = ukey_private_min;
	
	private int mValue;
	
	private Ukey()
	{
		this.mValue = ordinal++;
	}
	
	public Ukey(int val)
	{
		this.mValue = val;
	}
	
	public int value()
	{
		return this.mValue;
	}
	
	static final Ukey 
	Ukey_cancel			= new Ukey(), // ctrl+c signal

	Ukey_backspace		= new Ukey(),
	Ukey_tab			= new Ukey(),
	Ukey_clear			= new Ukey(),
	Ukey_return			= new Ukey(),

	Ukey_shift_l		= new Ukey(),
	Ukey_shift_r		= new Ukey(),

	Ukey_ctrl_l			= new Ukey(),
	Ukey_ctrl_r			= new Ukey(),
	
	Ukey_alt_l			= new Ukey(),
	Ukey_alt_r			= new Ukey(),

	Ukey_pause			= new Ukey(),

	Ukey_capslock		= new Ukey(),
	Ukey_numlock		= new Ukey(),
	Ukey_scrolllock		= new Ukey(),

	Ukey_escape			= new Ukey(),

	Ukey_pgup			= new Ukey(),
	Ukey_pgdn			= new Ukey(),

	Ukey_home			= new Ukey(),
	Ukey_end			= new Ukey(),

	Ukey_up				= new Ukey(),
	Ukey_down			= new Ukey(),
	Ukey_left			= new Ukey(),
	Ukey_right			= new Ukey(),

	Ukey_select			= new Ukey(), // ???
	Ukey_print			= new Ukey(), // ctrl+p?
	Ukey_execute		= new Ukey(), // ???
	
	Ukey_printscreen	= new Ukey(),

	Ukey_insert			= new Ukey(),
	Ukey_delete			= new Ukey(),

	Ukey_help			= new Ukey(),
	
	Ukey_meta_l			= new Ukey(),
	Ukey_meta_r			= new Ukey(),

	Ukey_apps			= new Ukey(), // context menu? (Button between altr and ctrlr)

	Ukey_power			= new Ukey(),
	Ukey_sleep			= new Ukey(),
	Ukey_wake			= new Ukey(),

	Ukey_f1				= new Ukey(),
	Ukey_f2				= new Ukey(),
	Ukey_f3				= new Ukey(),
	Ukey_f4				= new Ukey(),
	Ukey_f5				= new Ukey(),
	Ukey_f6				= new Ukey(),
	Ukey_f7				= new Ukey(),
	Ukey_f8				= new Ukey(),
	Ukey_f9				= new Ukey(),
	Ukey_f10			= new Ukey(),
	Ukey_f11			= new Ukey(),
	Ukey_f12			= new Ukey(),
	Ukey_f13			= new Ukey(),
	Ukey_f14			= new Ukey(),
	Ukey_f15			= new Ukey(),

	Ukey_num0			= new Ukey(),
	Ukey_num1			= new Ukey(),
	Ukey_num2			= new Ukey(),
	Ukey_num3			= new Ukey(),
	Ukey_num4			= new Ukey(),
	Ukey_num5			= new Ukey(),
	Ukey_num6			= new Ukey(),
	Ukey_num7			= new Ukey(),
	Ukey_num8			= new Ukey(),
	Ukey_num9			= new Ukey(),

	
	//Ukey_
	
	Ukey_unknown		= new Ukey();
}