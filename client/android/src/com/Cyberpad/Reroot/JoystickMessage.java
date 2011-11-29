package com.Cyberpad.Reroot;

public class JoystickMessage extends ControlMessage {

	// Define the available controls on this device
	public static final int LEFT_STICK = 0;
	public static final int RIGHT_STICK = 1;
	
	// Define the device ID
	@Override
	int Device() { return 2; }
	
	// Boiler plate
	public JoystickMessage( int control, int action, int meta1, int meta2 ) {
		super(control, action, meta1, meta2);
	}
}
