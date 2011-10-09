package com.Cyberpad.Reroot;

import java.net.InetAddress;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PadActivity extends Activity {
	private OSCPortOut sender;
	SharedPreferences preferences;
	EditText test_msg;
	private static final String TAG = "Reroot";
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.test_layout);
	    test_msg = (EditText)findViewById(R.id.edit_test);
	    //get preferences
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    Button submit_btn = (Button)findViewById(R.id.submit_btn);
	    submit_btn.setOnClickListener( new OnClickListener() {
        	public void onClick(View v){
        		String ip_address = preferences.getString("ip_address", "n/a");
        		String message = test_msg.getText().toString();
				Object[] args = new Object[3];
				args[0] = 1; /* key down */
				args[1] = 96;// (int)c;
				args[2] = new Character('a');
				
				OSCMessage msg = new OSCMessage("/control", args);
				try {
					Log.d(TAG, "Sending...");
					sender.send(msg);
				} catch (Exception ex) {
					Log.d(TAG, "Failed to send...");
					Log.d(TAG, ex.toString());
				}
        		
        	}
        	
        });
	    
	    
	    try{
	    	Log.d(TAG, "Trying to create the OSCPort: Address - " + preferences.getString("ip_address", "n/a"));
	    	this.sender = new OSCPortOut(InetAddress.getByName(preferences.getString("ip_address", "n/a")),
	    			OSCPort
	    			.defaultSCOSCPort());
	    	
	    }
	    catch(Exception ex){
	    	Log.d(TAG, "Could not create the OSCPort");
	    	Log.d(TAG, ex.toString());
	    }
	    
	
	    // TODO Auto-generated method stub
	}

}

