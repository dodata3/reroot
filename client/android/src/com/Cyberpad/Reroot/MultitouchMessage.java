package com.Cyberpad.Reroot;

public class MultitouchMessage extends ControlMessage {

	// Define the available controls on this device
	public static final int PINCH_IN = 0;
	public static final int PINCH_OUT = 1;
	public static final int VERT_SCROLL_UP = 2;
	public static final int VERT_SCROLL_DOWN = 3;
	public static final int HORI_SCROLL_LEFT = 4;
	public static final int HORI_SCOLL_RIGHT = 5;
	
	// Define the device ID
	@Override
	int Device() { return 0; }
	
	// Boiler plate
	public MultitouchMessage( int control, int action, int meta1, int meta2 ) {
		super(control, action, meta1, meta2);
	}
}
