package com.Cyberpad.Reroot;

import java.net.InetAddress;
//import java.util.Timer;
//import java.util.TimerTask;
import java.lang.Math;

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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
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
	
	//touch stuff
	private float xHistory;
	private float yHistory;
	//multitouch stuff
	private float x1History, y1History, x2History, y2History;
	//private Timer tapTimer;
	private String tapstate = "no_tap";
	private long last_tap = 0;
	
	//multitouch stuff
	private int lastPointerCount = 0;
	private boolean multiEnabled;
	
	
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
	    charmap = KeyCharacterMap.load(KeyCharacterMap.ALPHA);
	    
	    multiEnabled = WrappedMotionEvent.isMultitouchCapable();
	    
	    
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
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		
		int pointerCount = 1;
		if (multiEnabled){
			pointerCount = WrappedMotionEvent.getPointerCount(ev);
			/*if(pointerCount ==2)
				Toast.makeText(PadActivity.this,
					"Pointer count is" + pointerCount, Toast.LENGTH_SHORT).show();*/
		}
		
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:			
				xMove = 0;
				yMove = 0;
				type = 0;
				this.xHistory = ev.getX();
				this.yHistory = ev.getY();
				
				//this.last_tap = System.currentTimeMillis();
				
				//handle tap-to-click
				if(this.tapstate == "no_tap" && (pointerCount == 1 || pointerCount == 2 )){
					//first tap
					
					if(pointerCount == 1){
						this.last_tap = System.currentTimeMillis();
						this.tapstate = "first_tap";
					}
					if(pointerCount == 2){ //&& System.currentTimeMillis() - this.last_tap < 200){
						
						this.last_tap = System.currentTimeMillis();
						this.tapstate = "double_tap";
						Toast.makeText(PadActivity.this,
								"Started double tap...", Toast.LENGTH_SHORT).show();
					}
					
					//return without sending anything
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				x1History = y1History = x2History = y2History = 0;
				
				//handle tap-to-click
				if(this.tapstate == "first_tap"){
					long now = System.currentTimeMillis();
					long elapsed = now - this.last_tap;
					
					if(elapsed <= 200){
						//Toast.makeText(PadActivity.this,
						//		"Pointer count is " + pointerCount, Toast.LENGTH_SHORT).show();
						
						//register the tap and send a click
						if(lastPointerCount  == 1)
							type = 0;
						else if(lastPointerCount == 2){
							Toast.makeText(PadActivity.this,
									"Double tap successful", Toast.LENGTH_SHORT).show();
							type = 3;	
						}
					}
					else{
						//too much time passed to be a tap
						this.last_tap = 0;
					}
					this.tapstate = "no_tap";
					
				}
				/*else if(this.tapstate == "double_tap"){
					long now = System.currentTimeMillis();
					long elapsed = now - this.last_tap;
					
					
					if(elapsed <= 200){
						Toast.makeText(PadActivity.this,
								"Double tap successful", Toast.LENGTH_SHORT).show();
						type = 3;	
					}
					else{
						//too much time passed to be a tap
						this.last_tap = 0;
					}
					this.tapstate = "no_tap";
				}*/
				
				
				break;
			case MotionEvent.ACTION_MOVE:
				if (pointerCount == 1){
					type = 2;
					if (lastPointerCount == 1){
						xMove = ev.getX() - this.xHistory;
						yMove = ev.getY() - this.yHistory;
					}
					this.xHistory = ev.getX();
					this.yHistory = ev.getY();
				}
				else if(pointerCount == 2){
					//MULTITOUCH ZONE
					float x1pos = WrappedMotionEvent.getX(ev, 1);
					float y1pos = WrappedMotionEvent.getY(ev, 1);
					float x2pos = WrappedMotionEvent.getX(ev, 2);
					float y2pos = WrappedMotionEvent.getY(ev, 2);
					
					//we're just starting the touch
					if(x1History ==0 && x2History == 0){
						//store current pos into the histories for later interpretation
						x1History = x1pos;
						y1History = y1pos;
						x2History = x2pos;
						y2History = y2pos;
					}
					//we can interpret the touch
					else{
						//check for pinch out
						if(Math.sqrt(Math.pow(x1pos-x2pos,2) + Math.pow(y1pos-y2pos, 2)) > 
							Math.sqrt(Math.pow(x1History - x2History, 2) + Math.pow(y1History - y2History, 2))+50){
							//send pinch out command
						}
						//check for pinch in
						else if(Math.sqrt(Math.pow((double)(x1pos-x2pos),2) + Math.pow(y1pos-y2pos, 2)) + 50< 
								Math.sqrt(Math.pow(x1History - x2History, 2) + Math.pow(y1History - y2History, 2))){
							//send pinch in command
						}
						//check for vertical scroll down
						else if(y1pos > y1History + 20 && y2pos > y2History + 20){
							//send vertical scroll down command
						}
						//check for vertical scroll up
						else if(y1pos < y1History + 20 && y2pos < y2History + 20){
							//send vertical scroll up command
						}
						//check for horizontal scroll right
						else if(x1pos > x1History + 20 && x2pos > x2History + 20){
							//send horizontal scroll right
						}
						//check for horizontal scroll left
						else if(x1pos < x1History + 20 && x2pos < x2History + 20){
							//send horizontal scroll left command
						}
						
					}
					
					
				}
				break;
		}
		
		lastPointerCount = pointerCount;
		
		//0 is a left click, 1 is a right click, 2 is a move
		if(type >= 0 ){
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
			char c_c = c.charAt(0);
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
					args[1] = key+68;
					args[2] = c_c;
					OSCMessage msg = new OSCMessage("/keyboard", args);
					
					this.sender.send(msg);
				}
				{
					Object[] args = new Object[3];
					args[0] = 1; /* key up */
					args[1] = key+68;// (int)c;
					args[2] = c_c;
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





