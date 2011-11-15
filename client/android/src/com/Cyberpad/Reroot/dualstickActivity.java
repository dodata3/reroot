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
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.KeyEvent;

public class dualstickActivity extends Activity {
	
	private OSCPortOut sender;
	//private Handler handler = new Handler();
	
	//private FrameLayout left_carriage;
	//private FrameLayout right_carriage;
	
	SharedPreferences preferences;
	private static final String TAG = "Reroot";
	
	//remember the past or condemn yourself
	private float left_xHistory;
	private float left_yHistory;
	private float right_xHistory;
	private float right_yHistory;
	private int head_y;
	private int l_head_x;
	private int r_head_x;
	
	private boolean multiEnabled;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//set display on our new layout
		setContentView(R.layout.dualstick_layout);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//force the screen to always be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		multiEnabled = WrappedMotionEvent.isMultitouchCapable();
		
		//assume that we will have access to OSC connection and initialize buttons	
		initLeftCarriage();
		initRightCarriage();
		
	}
	
	//initializations
	private void initLeftCarriage(){
		FrameLayout left_carriage = (FrameLayout)this.findViewById(R.id.left_carriage);
		
		left_carriage.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onLeftMove( ev );
        	}
        });
	}
	
	private void initRightCarriage(){
		FrameLayout right_carriage = (FrameLayout)this.findViewById(R.id.right_carriage);
		
		right_carriage.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onRightMove( ev );
			}
		});
	}
	
	//handles movement in the left carriage
	private boolean onLeftMove( MotionEvent ev ){
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		ImageView left = (ImageView)this.findViewById(R.id.left_head);
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = 0;
				yMove = 0;
				type = 0;
				this.left_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				this.l_head_x = left.getLeft();
				this.head_y = left.getTop();
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				this.left_xHistory = this.left_yHistory = 0;
				left.scrollTo(this.l_head_x, this.head_y);
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - this.left_xHistory;
				yMove = ev.getY() - this.left_yHistory;
				
				this.left_xHistory = ev.getX();
				this.left_yHistory = ev.getY();
				
				//move the dual stick head to reflect movement
				left.scrollBy((int)xMove, (int)yMove);
				
				break;	
		}
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		
		
		return true;
	}
	
	//handles movement in the right carriage
	private boolean onRightMove( MotionEvent ev ){
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		ImageView right = (ImageView)findViewById(R.id.right_head);
		
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = 0;
				yMove = 0;
				type = 0;
				this.right_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				this.r_head_x = right.getLeft();
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				this.right_xHistory = this.right_yHistory = 0;
				right.scrollTo(r_head_x, head_y);
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - this.right_xHistory;
				yMove = ev.getY() - this.right_yHistory;
				
				this.right_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				
				right.scrollBy((int)xMove, (int)yMove);
				
				break;	
		}
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		
		
		return true;
				
	}
}
	
	/*
	
//mouse zone
	private boolean onMouseMove(MotionEvent ev) {
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		
		//0 is a left click, 1 is a right click, 2 is a move
		if(type >= 0 ){
			this.sendMouseEvent(type, xMove, yMove);
		}
		return true;	
	}
*/