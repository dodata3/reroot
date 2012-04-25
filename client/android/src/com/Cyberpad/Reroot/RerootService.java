package com.Cyberpad.Reroot;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RerootService extends Service implements RerootServiceConstants
{
	private final IBinder mBinder = new RerootBinder( this );
	private boolean mConnected = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d( TAG, "Created RerootService" );
		if( !Connect() ) {
			Log.d( TAG, "Failed to start RerootService; Disconnecting..." );
			stopSelf();
		}
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
	
	private boolean Connect()
	{
		// TODO: Attempt to connect via ADB
		Log.d( TAG, "Attempting to connect!" );
		
		// Launch the connector activity to connect using another method
		Intent dialogIntent = new Intent( getBaseContext(), ConnectPickerActivity.class );
		dialogIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		getApplication().startActivity( dialogIntent );
		
		return false;
	}
}
// TODO: Call the ConnectPickerActivity from this service if not connected
// TODO: Attempt to bind the RerootService in the Reroot Application (on MainMenuActivity)
// TODO: Move into Cyberpad.RerootService (Perhaps into an "Android library project" (RerootLib)?)
//	Connector.java
//	ControlMessage.java
//	Crypto.java
//	EncryptedMessage.java
//	JoystickMessage.java
//	KeyboardMessage.java
//	MouseMessage.java
//	MultitouchMessage.java
//	Utility.java
// TODO: Link the RerootLib with the Reroot project and define the service in the AndroidManifest
// TODO: Create a RerootControllerPlugin class within the RerootLib which can be extended to create RCPs
// TODO: Convert all current controllers to RCPs
// TODO: Get Rid of all com.google.zxing
// TODO: Get Rid of :
//	QRConnectActivity.java
//	QRConnectActivityHandler.java
//	RerootViewfinderView.java
//	ViewfinderResulyPointCallback.java
//	DecoderThread.java
//	DecoderHandler.java
// TODO: Replace all QR Stuff with the intent to call BarcodeScanner
// TODO: Get Rid of all Doxyfile nonsence
