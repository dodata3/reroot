package com.Cyberpad.Reroot;

import java.net.InetAddress;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;


public class PadActivity extends Activity {
	private OSCPortOut sender;
	//keyboard stuff
	private FrameLayout soft_keys;
	private Handler handler = new Handler();
	//private boolean softShown = false;
	private Runnable rMidDown;
	private Runnable rMidUp;
	
	SharedPreferences preferences;
	private static final String TAG = "Reroot";
	private float xHistory;
	private float yHistory;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.new_layout);
	    //get preferences
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    FrameLayout touchpad = (FrameLayout)findViewById(R.id.TouchPad);
	    FrameLayout keys = (FrameLayout)findViewById(R.id.keyboard_btn);
	    
	    keys.setOnTouchListener(new View.OnTouchListener(){
	    	public boolean onTouch(View v, MotionEvent ev){
	    		return onKeyTouch(ev);
	    	}
	    });
	    soft_keys = keys;
	    
	    
	    
		touchpad.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onMouseMove( ev );
        	}
        });
	    
		//initialize keyboard stuff
		this.rMidDown = new Runnable() {
			public void run() {
				drawSoftOn();
			}
		};
		this.rMidUp = new Runnable() {
			public void run() {
				drawSoftOff();
			}
		};
		
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
	
	//mouse zone
	
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
		args[1] = (int) ( (Math.pow(Math.abs(x), 1)) * xDir );
		args[2] = (int) ( (Math.pow(Math.abs(y), 1)) * yDir );
		// Log.d(TAG, String.valueOf(Settings.getSensitivity()));
		//
		OSCMessage msg = new OSCMessage("/mouse", args);
		try {
			this.sender.send(msg);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
		}
	}
	
	//keyboard zone
	
	private boolean onKeyTouch(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//
			this.handler.post(this.rMidDown);
			break;
		case MotionEvent.ACTION_UP:
			//
			this.midButtonDown();
			this.handler.post(this.rMidUp);
			break;
		}
		//this.softShown = true;
		//
		return true;
	}

	private void midButtonDown() {
		InputMethodManager man = (InputMethodManager) this.getApplicationContext()
				.getSystemService(INPUT_METHOD_SERVICE);
		man.toggleSoftInputFromWindow(this.soft_keys.getWindowToken(),
				InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	private void drawSoftOn() {
		this.soft_keys.setBackgroundResource(R.drawable.keyboard_on);
	}

	private void drawSoftOff() {
		this.soft_keys.setBackgroundResource(R.drawable.keyboard_off);
	}

}





