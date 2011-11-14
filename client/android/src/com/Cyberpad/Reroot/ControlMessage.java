package com.Cyberpad.Reroot;

public abstract class ControlMessage {
	
	protected int mDevice;
	protected int mControl;
	protected int mType;
	protected int mMeta1, mMeta2;
	
	abstract int Device();
	
	public String FormatMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append( Device() ).append( ',' );
		sb.append( mControl ).append( ',' );
		sb.append( mType ).append( ',' );
		sb.append( mMeta1 ).append( ',' );
		sb.append( mMeta2 );
		return sb.toString();
	}
}
