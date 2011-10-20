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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.view.KeyEvent;


public class PadActivity extends Activity {
	private OSCPortOut sender;
	//keyboard stuff
	private FrameLayout soft_keys;
	private Handler handler = new Handler();
	//private boolean softShown = false;
	private Runnable rMidDown;
	private Runnable rMidUp;
	public static KeyCharacterMap charmap;
	
	private EditText AdvancedText;
	
	SharedPreferences preferences;
	private static final String TAG = "Reroot";
	private float xHistory;
	private float yHistory;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //set display to our new layout
	    setContentView(R.layout.new_layout);
	    /*requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
	    //get preferences
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    charmap = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
	    
	    
	    
	    
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
	    	//initialize the touch pad and keyboard button
	    	this.initTouchpad();
	    	this.initKeys();
	    	this.initAdvancedText();
	    	
	    }
	    catch(Exception ex){
	    	Log.d(TAG, "Could not create the OSCPort");
	    	Log.d(TAG, ex.toString());
	    }
	    
	
	    // TODO Auto-generated method stub
	}
	
	//initializations
	private void initTouchpad(){
		FrameLayout touchpad = (FrameLayout)this.findViewById(R.id.TouchPad);
		
		touchpad.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onMouseMove( ev );
        	}
        });
		
	}
	
	private void initKeys(){
	    FrameLayout keys = (FrameLayout)this.findViewById(R.id.keyboard_btn);
	    
	    keys.setOnTouchListener(new View.OnTouchListener(){
	    	public boolean onTouch(View v, MotionEvent ev){
	    		return onKeyTouch(ev);
	    	}
	    });
	    
	    this.soft_keys = keys;
		
	}
	
	String changed = "";
	private void initAdvancedText(){
		EditText et = (EditText)this.findViewById(R.id.AdvancedText);
		this.AdvancedText = et;
		
		//prevent keyboard from going fullscreen in landscape
		et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		
		changed = "a ";
		AdvancedText.setText(changed);
		
		//listener
		et.setOnKeyListener(new OnKeyListener(){
			
			//@Override
			public boolean onKey(View v, int keyCode, KeyEvent event){
				changed = "a ";
				AdvancedText.setText(changed);
				return false;
			}
			
		});
		et.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){
				if(s.toString().equals(changed)){
					AdvancedText.requestFocus();
					AdvancedText.setSelection(2);
					return;
				}
				changed = null;
				
				String change = s.toString().substring(start, start+count);
				
				if(count != 0){
					if(change.equals(" "))
						sendKey(62);
					else
						sendKeys(change);	
				}
				else
					sendKey(67);
				
				changed = "a ";
				AdvancedText.setText(changed);
			}
			
			public void afterTextChanged(Editable s){
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
				
			}
			
		});
		
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
		this.soft_keys.setBackgroundResource(R.drawable.keyboard);
	}
	
	private void sendKey(int keycode){
		try{
			//scoping brackets
			{
				Object[] args = new Object[3];
				args[0] = 0; //key down
				args[1] = keycode;
				args[2] = new Character(Character.toChars(PadActivity.charmap.get(keycode,  0))[0]).toString();
				OSCMessage msg = new OSCMessage("/keyboard", args);
				
				this.sender.send(msg);
			}
			{
				Object[] args = new Object[3];
				args[0] = 1; //key up
				args[1] = keycode;
				args[2] = new Character(Character.toChars(PadActivity.charmap.get(keycode,  0))[0]).toString();
				OSCMessage msg = new OSCMessage("/keyboard", args);
				
				this.sender.send(msg);
			}
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
		}
	}
	
	private void sendKeys(String keys){
		if(keys.equals("a ")) return;
		
		for(int i=0; i<keys.length();i++){
			String c = keys.substring(i, i+1);
			boolean isShift = false;
			boolean isCtrl = false;
			
			if(!c.toLowerCase().equals(c)){
				isShift = true;
				c = c.toLowerCase();
			}
			
			int key = 0;
			
			//conversion zone
			if(c.equals(" ")) key = 62;
			if(c.equals("\n")) key = 66;
			if(c.equals("\t")){key = 45; isCtrl = true;}
			if (c.equals("_")){key = 95; isShift = true;}
			if (c.equals("\"")){key = 75; isShift = true;}
			if (c.equals("^")){key = 94; isShift = true;}
			if (c.equals("~")){key = 126; isShift = true;}
			if (c.equals("`")){key = 68; isShift = true;}
			if (c.equals(":")){key = 74; isShift = true;}
			if (c.equals("=")) key = 70;
			if (c.equals("+")){key = 70; isShift=true;}
			if (c.equals("%")){key = 12; isShift=true;}
			if (c.equals("&")){key = 14; isShift=true;}
			if (c.equals("^")){key = 13; isShift=true;}
			if (c.equals("|")){key = 73; isShift=true;}
			if (c.equals("_")){key = 69; isShift=true;}
			if (c.equals("?")){key = 76; isShift=true;}
			if (c.equals("!")){key = 8; isShift=true;}
			if (c.equals("$")){key = 11; isShift=true;}
			if (c.equals("~")){key = 68; isShift=true;}
			if (c.equals("<")){key = 55; isShift=true;}
			if (c.equals(">")){key = 56; isShift=true;}
			if (c.equals("")){key = 56; isCtrl=true;}
			if (c.equals("(")){key = 16; isShift = true;}
			if (c.equals(")")){key = 7; isShift = true;}
			if (c.equals("{")){key = 71; isShift = true;}
			if (c.equals("}")){key = 72; isShift = true;}
			if (c.equals("["))key = 71;
			if (c.equals("]"))key = 72;
			
			if(key==0)
				for(int z =0; z <1024; z++)
					if(PadActivity.charmap.isPrintingKey(z))
						if(new Character(Character.toChars(PadActivity.charmap
								.get(z, 0))[0]).toString().equals(c)){
							key = z;
							break;
						}
			
			try{
				if(isCtrl){
					Object[] args = new Object[3];
					args[0] = 0; //key down
					args[1] = 57;
					args[2] = new Character ((char)0).toString();
					OSCMessage msg = new OSCMessage("/keyboard", args);
					
					this.sender.send(msg);
				}
				if(isShift){
					Object[] args = new Object[3];
					args[0] = 0; //key down
					args[1] = 59;
					args[2] = new Character ((char)0).toString();
					OSCMessage msg = new OSCMessage("/keyboard", args);
					
					this.sender.send(msg);
				}
				{
					Object[] args = new Object[3];
					args[0] = 0; //key down
					args[1] = key;
					args[2] = c;
					OSCMessage msg = new OSCMessage("/keyboard", args);
					
					this.sender.send(msg);
				}
				{
					Object[] args = new Object[3];
					args[0] = 1; /* key up */
					args[1] = key;// (int)c;
					args[2] = c;
					OSCMessage msg = new OSCMessage("/keyboard", args);
	
					this.sender.send(msg);
				}

				if(isShift){
					Object[] args = new Object[3];
					args[0] = 1; /* key up */
					args[1] = 59;// (int)c;
					args[2] = new Character((char)0).toString();
					OSCMessage msg = new OSCMessage("/keyboard", args);

					this.sender.send(msg);
				}
				
				if(isCtrl){
					Object[] args = new Object[3];
					args[0] = 1; /* key up */
					args[1] = 57;// (int)c;
					args[2] = new Character((char)0).toString();
					OSCMessage msg = new OSCMessage("/keyboard", args);

					this.sender.send(msg);
				}
			}
			catch(Exception ex){
				Log.d(TAG, ex.toString());
			}
		}
	}
	

}





