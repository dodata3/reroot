package com.Cyberpad.Reroot;

public class KeyboardMessage extends ControlMessage {
	
	
	// Define the available controls on this device
	
	
	// Define the device ID
	@Override
	int Device() { return 1; }
	
	// Boiler plate
	public KeyboardMessage( int control, int action, int meta1, int meta2 ) {
		super(control, action, meta1, meta2);
	}
}
