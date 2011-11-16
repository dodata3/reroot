package com.Cyberpad.Reroot;

public abstract class ControlMessage {
	
	protected int mDevice;
	protected int mControl;
	protected int mAction;
	protected int mMeta1, mMeta2;
	
	abstract int Device();
	
	public static final int CONTROL_DOWN = 0;
	public static final int CONTROL_UP = 1;
	public static final int CONTROL_MOVE = 2;
	
	public ControlMessage( int control, int action, int meta1, int meta2 )
	{
		mDevice = Device();
		mControl = control;
		mAction = action;
		mMeta1 = meta1;
		mMeta2 = meta2;
	}
	
	public String FormatMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append( Device() ).append( ',' );
		sb.append( mControl ).append( ',' );
		sb.append( mAction ).append( ',' );
		sb.append( mMeta1 ).append( ',' );
		sb.append( mMeta2 );
		return sb.toString();
	}
}
