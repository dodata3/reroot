package com.Cyberpad.Reroot;

import android.os.Binder;

public class RerootBinder extends Binder {
	
	private RerootService mService;
	
	RerootBinder( RerootService pService )
	{
		mService = pService;
	}

}
