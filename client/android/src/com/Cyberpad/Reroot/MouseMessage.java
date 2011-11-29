package com.Cyberpad.Reroot;

public class MouseMessage extends ControlMessage {

	// Define the available controls on this device
	public static final int LEFT_BUTTON = 0;
	public static final int RIGHT_BUTTON = 1;
	public static final int MIDDLE_BUTTON = 2;
	public static final int BUTTON_4 = 3;
	public static final int BUTTON_5 = 4;
	public static final int TOUCH_1 = 5;
	public static final int TOUCH_2 = 6;
	public static final int TOUCH_3 = 7;
	
	// Define the device ID
	@Override
	int Device() { return 0; }
	
	// Boiler plate
	public MouseMessage( int control, int action, int meta1, int meta2 ) {
		super(control, action, meta1, meta2);
	}
}
