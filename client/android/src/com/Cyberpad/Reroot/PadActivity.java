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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class PadActivity extends Activity {
	private OSCPortOut sender;
	SharedPreferences preferences;
	EditText test_msg;
	private static final String TAG = "Reroot";
	private float xHistory;
	private float yHistory;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.test_layout);
	    test_msg = (EditText)findViewById(R.id.edit_test);
	    //get preferences
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    Button submit_btn = (Button)findViewById(R.id.submit_btn);
	    FrameLayout touchpad = (FrameLayout)findViewById(R.id.TouchPad);
	    submit_btn.setOnClickListener( new OnClickListener() {
        	public void onClick(View v){
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
	    
		touchpad.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				Object[] args = new Object[3];
				args[0] = 1; /* key down */
				args[1] = 96;// (int)c;
				args[2] = new Character('a');
				
				OSCMessage msg = new OSCMessage("/control", args);
				try {
					Log.d(TAG, "Sending...");
					sender.send(msg);
					return true;
				} catch (Exception ex) {
					Log.d(TAG, "Failed to send...");
					Log.d(TAG, ex.toString());
					return false;
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
	
	private boolean onMouseMove(MotionEvent ev) {
		int type = 0;
		float xMove = 0f;
		float yMove = 0f;
		
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				xMove = 0;
				yMove = 0;
				this.xHistory = ev.getX();
				this.yHistory = ev.getY();
				break;
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - this.xHistory;
				yMove = ev.getY() - this.yHistory;
				this.xHistory = ev.getX();
				this.yHistory = ev.getY();
				break;
		}
		
		if(type==2){
			this.sendMouseEvent(type, xMove, yMove);
		}
		return true;
		
	}
	
	private void sendMouseEvent(int type, float x, float y) {
		//
		float xDir = x == 0 ? 1 : x / Math.abs(x);
		float yDir = y == 0 ? 1 : y / Math.abs(y);
		//
		Object[] args = new Object[3];
		args[0] = type;
		args[1] = (float) (Math.pow(Math.abs(x), 1)) * xDir;
		args[2] = (float) (Math.pow(Math.abs(y), 1)) * yDir;
		// Log.d(TAG, String.valueOf(Settings.getSensitivity()));
		//
		OSCMessage msg = new OSCMessage("/mouse", args);
		try {
			this.sender.send(msg);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
		}
	}

}

