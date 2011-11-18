package com.Cyberpad.Reroot;

import java.net.InetAddress;
//import java.util.Timer;
//import java.util.TimerTask;
import java.lang.Math;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;
import android.view.KeyEvent;


public class dualstickActivity extends Activity{
	private OSCPortOut sender;
	//private Handler handler = new Handler();
	
	//private FrameLayout left_carriage;
	//private FrameLayout right_carriage;
	
	SharedPreferences preferences;
	private static final String TAG = "dualstickActivity";
	
	
	private boolean multiEnabled;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duallayout2);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//force the screen to always be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		multiEnabled = WrappedMotionEvent.isMultitouchCapable();
		
		//assume that we will have access to OSC connection and initialize buttons	
		initBackground();
		//initLeftCarriage();
		//initRightCarriage();
		//initCosmetic();
		
	}
	
	private void initBackground(){
		LinearLayout background = (LinearLayout)this.findViewById(R.id.background);
		
		background.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return backgroundTouch(ev);
			}
		});
		
	}
	
	private boolean backgroundTouch(MotionEvent ev){
		int pointerCount = 1;
		@SuppressWarnings("deprecation")
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage_padded);
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage_padded);
		
		if (multiEnabled){
			pointerCount = WrappedMotionEvent.getPointerCount(ev);
		}
		
		
		//loop through registered touches
		for(int i=0; i<pointerCount; i++){
			
			float touchx = WrappedMotionEvent.getX(ev, i);
			float touchy = WrappedMotionEvent.getY(ev, i);
			
			//rects don't seem to be working
			Rect left_rect = new Rect();
			left_carriage.getHitRect(left_rect);
			Rect right_rect = new Rect();
			right_carriage.getHitRect(right_rect);
			//Toast.makeText(dualstickActivity.this,
			//		"rect"+left_rect.left + " " + left_rect.bottom, Toast.LENGTH_SHORT).show();
			
			
			
			
			//check to see if we're registering a touch in the left zone and handle it
			if(left_rect.contains((int)touchx, (int)touchy)){
				handle_event(ev, touchx, touchy, left_carriage);			
			}
			//check to see if we're registering a touch in the right zone and handle it
			if(right_rect.contains((int)touchx, (int)touchy)){
				handle_event(ev, touchx-right_rect.left, touchy, right_carriage);
			}		
		}

	
		return true;
		
	}
	
	@SuppressWarnings("deprecation")
	private void handle_event(MotionEvent ev, float ev_x, float ev_y, AbsoluteLayout carriage){
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		int cosX = 0;
		int cosY = 0;
		
		
		carriage.removeAllViews();
		
		//load the bitmap for the head
		Bitmap head_res = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		BitmapDrawable bmd = new BitmapDrawable(head_res);
		ImageView head = new ImageView(this);
		head.setImageDrawable(bmd);
		
		//calculations for centering the head
		int head_width = head.getDrawable().getIntrinsicWidth();
		int head_height = head.getDrawable().getIntrinsicHeight();
		int centerx = carriage.getWidth()/2;
		int centery = carriage.getHeight()/2;
		
		int offsetx = 1*(carriage.getWidth() - head_width)/3;
		int offsety = 1*(carriage.getHeight() - head_height)/3;
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = ev_x - centerx;
				yMove = ev_y - centery;
				type = 0;
				
				break;
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				
				break;
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev_x - centerx;
				yMove = ev_y - centery;
				
				break;
		}
		
		//cosmetic values
		cosX = (int)xMove + offsetx;
		cosY = (int)yMove + offsety;
		
		//make sure that we are within bounds
		double offset = Math.sqrt(Math.pow(xMove,2) + Math.pow(yMove, 2));
		//bound cosmetic joy stick
		if(offset > centerx - head_width/2){
			double angle = Math.atan2(yMove, xMove);
			double cos_drawback = (centerx - (3*head_width/4))/offset;
			cosX = (int)(Math.cos(angle)*offset*cos_drawback) + offsetx;
			cosY = (int)(Math.sin(angle)*offset*cos_drawback) + offsety;
			//now do xMove and yMove if we need to
			if(offset > centerx){
				double drawback = centerx/offset;
				int new_x = (int)(Math.cos(angle)*offset*drawback);
				int new_y = (int)(Math.sin(angle)*offset*drawback);
				xMove = new_x;
				yMove = new_y;
			}
		}
		
		carriage.addView(head,
			new AbsoluteLayout.LayoutParams(head_res.getWidth(), head_res.getHeight(), 
				cosX, cosY));
				
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here	
		 
		
	}
	
}
/*

		
		cosX = (int)xMove + offsetx;
		cosY = (int)yMove + offsety;
		
		//make sure that we are within bounds
		double offset = Math.sqrt(Math.pow(xMove,2)+Math.pow(yMove,2));
		//bound cosmetic joy stick
		if(offset > centerx - head_width/2){
			double angle = Math.atan2(yMove, xMove);
			double cos_drawback = (centerx - (3*head_width/4))/offset;
			cosX = (int)(Math.cos(angle)*offset*cos_drawback) + offsetx;
			cosY = (int)(Math.sin(angle)*offset*cos_drawback) + offsety;
			//now do xMove and yMove if we need to
			if(offset > centerx){
				double drawback = centerx/offset;
				int new_x = (int)(Math.cos(angle)*offset*drawback);
				int new_y = (int)(Math.sin(angle)*offset*drawback);
				xMove = new_x;
				yMove = new_y;
			}
		}
		
		left_carriage.addView(left,
				new AbsoluteLayout.LayoutParams(left_head.getWidth(), left_head.getHeight(), 
						cosX, cosY));
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		
		return true;
	}*/


/*public class dualstickActivity extends Activity {
	
	private OSCPortOut sender;
	//private Handler handler = new Handler();
	
	//private FrameLayout left_carriage;
	//private FrameLayout right_carriage;
	
	SharedPreferences preferences;
	private static final String TAG = "Reroot";
	
	
	private boolean multiEnabled;
	
	//let's do animations
	
	
	/** Called when the activity is first created. */
	/*@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dualstick_layout);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//force the screen to always be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		multiEnabled = WrappedMotionEvent.isMultitouchCapable();
		
		//assume that we will have access to OSC connection and initialize buttons	
		initLeftCarriage();
		initRightCarriage();
		initCosmetic();
		
	}
	
	//initializations
	//we will also be initializing our variables in this for use in later functions
	@SuppressWarnings("deprecation")
	private void initLeftCarriage(){
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage);
		
		left_carriage.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onLeftMove( ev );
        	}
        });
	}
	
	@SuppressWarnings("deprecation")
	private void initRightCarriage(){
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage);
		
		right_carriage.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onRightMove( ev );
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void initCosmetic(){

		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage);
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage);	
		left_carriage.removeAllViews();
		
		//load the bitmap
		Bitmap left_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		Bitmap right_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		
		
		//make a drawble from the bitmap
		BitmapDrawable left_bmd = new BitmapDrawable(left_head);
		BitmapDrawable right_bmd = new BitmapDrawable(right_head);
		ImageView left = new ImageView(this);
		ImageView right = new ImageView(this);
		left.setImageDrawable(left_bmd);
		right.setImageDrawable(right_bmd);
		
		//do calculation for the center of the carriage 'n' such
		int head_width = left.getDrawable().getIntrinsicWidth();
		int head_height = left.getDrawable().getIntrinsicHeight();
		int centerx = left_carriage.getWidth()/2;
		int centery = left_carriage.getHeight()/2;
		
		int offsetx = (centerx - head_width)/4;
		int offsety = (centery - head_height)/4;
		
		left_carriage.addView(left,
				new AbsoluteLayout.LayoutParams(left_head.getWidth(), left_head.getHeight(), 
						offsetx, offsety));
		right_carriage.addView(right,
				new AbsoluteLayout.LayoutParams(right_head.getWidth(), right_head.getHeight(), 
						offsetx, offsety));
	
	}
	
	//handles movement in the left carriage
	@SuppressWarnings("deprecation")
	private boolean onLeftMove( MotionEvent ev ){
		//Toast.makeText(dualstickActivity.this,
		//		"Left stick", Toast.LENGTH_SHORT).show();
		
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		int cosX = 0;
		int cosY = 0;
		//ImageView left = (ImageView)this.findViewById(R.id.left_head);
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage);
		left_carriage.removeAllViews();
			
		//load the bitmap
		Bitmap left_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		
		//make a drawble from the bitmap
		BitmapDrawable bmd = new BitmapDrawable(left_head);
		ImageView left = new ImageView(this);
		left.setImageDrawable(bmd);
		
		//do calculation for the center of the carriage 'n' such
		int head_width = left.getDrawable().getIntrinsicWidth();
		int head_height = left.getDrawable().getIntrinsicHeight();
		int centerx = left_carriage.getWidth()/2;
		int centery = left_carriage.getHeight()/2;
		
		int offsetx = (left_carriage.getWidth() - head_width)/4;
		int offsety = (left_carriage.getHeight() - head_height)/4;
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = ev.getX()-centerx;
				yMove = ev.getY()-centery;
				type = 0;
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;	
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - centerx;
				yMove = ev.getY() - centery;
				
				break;	
		}
		
		cosX = (int)xMove + offsetx;
		cosY = (int)yMove + offsety;
		
		//make sure that we are within bounds
		double offset = Math.sqrt(Math.pow(xMove,2)+Math.pow(yMove,2));
		//bound cosmetic joy stick
		if(offset > centerx - head_width/2){
			double angle = Math.atan2(yMove, xMove);
			double cos_drawback = (centerx - (3*head_width/4))/offset;
			cosX = (int)(Math.cos(angle)*offset*cos_drawback) + offsetx;
			cosY = (int)(Math.sin(angle)*offset*cos_drawback) + offsety;
			//now do xMove and yMove if we need to
			if(offset > centerx){
				double drawback = centerx/offset;
				int new_x = (int)(Math.cos(angle)*offset*drawback);
				int new_y = (int)(Math.sin(angle)*offset*drawback);
				xMove = new_x;
				yMove = new_y;
			}
		}
		
		left_carriage.addView(left,
				new AbsoluteLayout.LayoutParams(left_head.getWidth(), left_head.getHeight(), 
						cosX, cosY));
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		
		return true;
	}
	
	//handles movement in the right carriage
	@SuppressWarnings("deprecation")
	private boolean onRightMove( MotionEvent ev ){
		//Toast.makeText(dualstickActivity.this,
		//		"Right stick", Toast.LENGTH_SHORT).show();
		
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		int cosX = 0;
		int cosY = 0;
		//ImageView left = (ImageView)this.findViewById(R.id.left_head);
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage);
		right_carriage.removeAllViews();
			
		//load the bitmap
		Bitmap right_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		
		//make a drawble from the bitmap
		BitmapDrawable bmd = new BitmapDrawable(right_head);
		ImageView right = new ImageView(this);
		right.setImageDrawable(bmd);
		
		//do calculation for the center of the carriage 'n' such
		int head_width = right.getDrawable().getIntrinsicWidth();
		int head_height = right.getDrawable().getIntrinsicHeight();
		int centerx = right_carriage.getWidth()/2;
		int centery = right_carriage.getHeight()/2;
		
		int offsetx = (right_carriage.getWidth() - head_width)/4;
		int offsety = (right_carriage.getHeight() - head_height)/4;
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = ev.getX()-centerx;
				yMove = ev.getY()-centery;
				type = 0;
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;	
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - centerx;
				yMove = ev.getY() - centery;
				
				break;	
		}
		
		cosX = (int)xMove + offsetx;
		cosY = (int)yMove + offsety;
		
		//make sure that we are within bounds
		double offset = Math.sqrt(Math.pow(xMove,2)+Math.pow(yMove,2));
		//bound cosmetic joy stick
		if(offset > centerx - head_width/2){
			double angle = Math.atan2(yMove, xMove);
			double cos_drawback = (centerx - (3*head_width/4))/offset;
			cosX = (int)(Math.cos(angle)*offset*cos_drawback) + offsetx;
			cosY = (int)(Math.sin(angle)*offset*cos_drawback) + offsety;
			//now do xMove and yMove if we need to
			if(offset > centerx){
				double drawback = centerx/offset;
				int new_x = (int)(Math.cos(angle)*offset*drawback);
				int new_y = (int)(Math.sin(angle)*offset*drawback);
				xMove = new_x;
				yMove = new_y;
			}
		}
		
		right_carriage.addView(right,
				new AbsoluteLayout.LayoutParams(right_head.getWidth(), right_head.getHeight(), 
						cosX, cosY));
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		
		return true;
				
	}
}*/