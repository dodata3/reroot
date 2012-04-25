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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ConnectPickerActivity extends Activity {
	
	@Override
	public void onCreate( Bundle icicle ) {
		super.onCreate( icicle );
		setContentView( R.layout.connect_picker_layout );
		
		Button qr_button = ( Button )findViewById( R.id.qr_button );
		qr_button.setOnClickListener(new OnClickListener() {
			public void onClick( View v ){
				try{
					Intent i = new Intent( ConnectPickerActivity.this, QRConnectActivity.class );
					ConnectPickerActivity.this.startActivity(i);
				}
				catch( Exception ex ){
					Log.e( "Reroot", "Could not start the QRConnector" );
				}
			}
		} );
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
