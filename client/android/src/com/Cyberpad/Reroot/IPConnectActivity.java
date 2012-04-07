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
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class IPConnectActivity extends Activity {

	@Override
	public void onCreate( Bundle icicle ) {
		super.onCreate( icicle );
		setContentView( R.layout.connect_ip_layout );
		
		Button connect = (Button)findViewById( R.id.connect_button );
		connect.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				String ip = getIP();
				String security = getSecurity();
				Log.i("Reroot", ip);
				Log.i("Reroot", security);
				Toast.makeText(IPConnectActivity.this, "IP: " + ip + "\nSecurity: " + security, Toast.LENGTH_LONG).show();
			}
		});
		
		Button back = (Button)findViewById( R.id.ip_back_button );
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				IPConnectActivity.this.finish();
			}
		});
	}
	
	private String getIP() {
		EditText ip_edit_1 = (EditText)findViewById( R.id.ip_edit_1 );
		EditText ip_edit_2 = (EditText)findViewById( R.id.ip_edit_2 );
		EditText ip_edit_3 = (EditText)findViewById( R.id.ip_edit_3 );
		EditText ip_edit_4 = (EditText)findViewById( R.id.ip_edit_4 );
		
		String address = "";
		if (ip_edit_1.getText().toString().equals(""))
			address = address.concat(ip_edit_1.getHint().toString().trim());
		else
			address = address.concat(ip_edit_1.getText().toString());
		address = address.concat(".");
		if (ip_edit_2.getText().toString().equals(""))
			address = address.concat(ip_edit_2.getHint().toString().trim());
		else
			address = address.concat(ip_edit_2.getText().toString());
		address = address.concat(".");
		if (ip_edit_3.getText().toString().equals(""))
			address = address.concat(ip_edit_3.getHint().toString().trim());
		else
			address = address.concat(ip_edit_3.getText().toString());
		address = address.concat(".");
		if (ip_edit_4.getText().toString().equals(""))
			address = address.concat(ip_edit_4.getHint().toString().trim());
		else
			address = address.concat(ip_edit_4.getText().toString());
		
		return address;
	}
	
	private String getSecurity() {
		EditText ip_security = (EditText)findViewById( R.id.security_edit );
		if (ip_security.getText().toString().equals(""))
			return ip_security.getHint().toString();
		return ip_security.getText().toString();
	}
}
