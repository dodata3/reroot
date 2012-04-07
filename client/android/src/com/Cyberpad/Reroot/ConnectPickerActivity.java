package com.Cyberpad.Reroot;

import com.Cyberpad.RerootService.RerootService;
import com.Cyberpad.RerootService.RerootBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class ConnectPickerActivity extends Activity {
	
	private RerootBinder mBinder;
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected( ComponentName className, IBinder binder) {
			mBinder = ( RerootBinder )binder;
			Toast.makeText( ConnectPickerActivity.this, "Connected to Service!", Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onServiceDisconnected( ComponentName className ) {
			Toast.makeText( ConnectPickerActivity.this, "Disconnected from Service!", Toast.LENGTH_SHORT ).show();
			
		}
	};
	
	@Override
	public void onCreate( Bundle icicle ) {
		super.onCreate( icicle );
		setContentView( R.layout.connect_picker_layout );
		
		// TODO: This may move around.
		bindService( new Intent( this, RerootService.class ), mConnection, Context.BIND_AUTO_CREATE );
		
		ImageButton qr_button = ( ImageButton )findViewById( R.id.qr_button );
		qr_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				try{
					Intent i = new Intent( ConnectPickerActivity.this, QRConnectActivity.class );
					ConnectPickerActivity.this.startActivity(i);
				}
				catch(Exception ex){
					Log.e( "Reroot", "Could not start the QRConnectActivity" );
				}
			}
		});
		
		ImageButton bluetooth_button = (ImageButton)findViewById( R.id.bluetooth_button);
		bluetooth_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Log.i( "Reroot", "Start the Bluetooth Activity!");
			}
		});
		
		ImageButton ip_button = (ImageButton)findViewById( R.id.ip_button);
		ip_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				Log.i( "Reroot", "Start the IP Activity!");
			}
		});
	}

}
