package com.Cyberpad.Reroot;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

//the main menu class
public class MainMenuActivity extends Activity{
	
	private static final String TAG = "MainMenu";
	private RerootBinder mBinder;
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected( ComponentName className, IBinder binder) {
			mBinder = ( RerootBinder )binder;
			Toast.makeText( MainMenuActivity.this, "Connected to Service!", Toast.LENGTH_LONG ).show();
		}

		@Override
		public void onServiceDisconnected( ComponentName className ) {
			Toast.makeText( MainMenuActivity.this, "Disconnected from Service!", Toast.LENGTH_SHORT ).show();
			
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Start the RerootService
		startService( new Intent( this, RerootService.class ) );
		
		setContentView(R.layout.main_menu);
		Button hybrid_mode = (Button)findViewById(R.id.hybrid_mode);
		Button dualstick_mode = (Button)findViewById(R.id.dualstick_mode);
		Button acc_test = (Button)findViewById(R.id.acc_test);
	
		hybrid_mode.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				onHybridClick();
			}
		});
		
		dualstick_mode.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				onDualClick();
			}
		});
		
		acc_test.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				onAccClick();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Attempt to bind to the RerootService
		//Log.d( TAG, "Attempting to Bind RerootService!" );
		//bindService( new Intent( this, RerootService.class ), mConnection, Context.BIND_AUTO_CREATE );
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		//// Unbind the RerootService
		//unbindService( mConnection );
	}
	
	@Override
	public void onDestroy() {
		// Stop the RerootService
		stopService( new Intent( this, RerootService.class ) );
		super.onDestroy();
	}
	
	public void onAccClick(){
		try{
			Intent i = new Intent(this, PresentationMode.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
		}		
	}
	
	public void onHybridClick(){
		try{
			Intent i = new Intent(this, PadActivity.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show(); Log.d(TAG, ex.toString());
		}
	}
	
	public void onDualClick(){
		try{
			Intent i = new Intent(this, dualstickActivity.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show(); Log.d(TAG, ex.toString());
		}
	}
}