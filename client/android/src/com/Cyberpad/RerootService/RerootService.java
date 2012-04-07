package com.Cyberpad.RerootService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RerootService extends Service implements RerootServiceConstants
{
	private final IBinder mBinder = new RerootBinder( this );
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d( TAG, "Created RerootService" );
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d( TAG, "Destroyed RerootService" );
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}