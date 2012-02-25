package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;



public class generic_controller extends Activity{
	private Connector mConnector;
	private boolean multiEnabled;
	private static final String TAG = "Reroot";
	
	//buttons
	private AbsoluteLayout carriage;
	private Button start_btn, select_btn;
	private RelativeLayout y_btn, x_btn, a_btn, b_btn, t_btn;
	//carriage, start, select, y, x, a, b, t
	private Rect rects[];
	
	private boolean keypress[];
	
	//orientation stuff (red button)
	private float cur_or[] = {0, 0, 0};

	private SensorManager mSensorManager;
	private final SensorEventListener mSensorListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent se){
			if(se.sensor.getType() == Sensor.TYPE_ORIENTATION)
				send_orientation(se.values[2], se.values[1]);
		}
		//don't need to do anything for this one
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};

	private Sensor mOrientation;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic);
		
		//force screen into landscape mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		multiEnabled = WrappedMotionEvent.isMultitouchCapable();
		mConnector = Connector.getInstance(this);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mConnector = Connector.getInstance(this);
		mSensorManager.registerListener(mSensorListener, mOrientation, SensorManager.SENSOR_DELAY_GAME);
		
		//initialize carriage array
		keypress = new boolean[4];
		for(int i=0; i<4; i++)
			keypress[i] = false;
		
		//initialize the background listener
		try{
			this.init_btns();
			this.init_background();
		}
		catch(Exception e){
			Log.d(TAG, "Could not create the OSCPort");
	    	Log.d(TAG, e.toString());
		}
		
	}
	
	protected void onResume(){
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mOrientation, SensorManager.SENSOR_DELAY_GAME);
	}

	protected void onPause(){
		super.onPause();
		mSensorManager.unregisterListener(mSensorListener);
	}
	
	private void init_btns(){
		//locate all of those buttons!
		carriage = (AbsoluteLayout)this.findViewById(R.id.analog_stick);
		start_btn = (Button) this.findViewById(R.id.start_btn);
		select_btn = (Button) this.findViewById(R.id.select_btn);
		y_btn = (RelativeLayout)this.findViewById(R.id.y_btn);
		x_btn = (RelativeLayout)this.findViewById(R.id.x_btn);
		a_btn = (RelativeLayout)this.findViewById(R.id.a_btn);
		b_btn = (RelativeLayout)this.findViewById(R.id.b_btn);
		t_btn = (RelativeLayout)this.findViewById(R.id.t_btn);
		
		rects = new Rect[8];
		//init rects for all touchable objects
		for(int i=0; i<8; i++)
			rects[i] = new Rect();
		carriage.getHitRect(rects[0]);
		start_btn.getHitRect(rects[1]);
		select_btn.getHitRect(rects[2]);
		y_btn.getHitRect(rects[3]);
		x_btn.getHitRect(rects[4]);
		a_btn.getHitRect(rects[5]);
		b_btn.getHitRect(rects[6]);
		t_btn.getHitRect(rects[7]);
		
	}
	
	//catch volume control events
	@Override
	public boolean dispatchKeyEvent(KeyEvent ev){
		//if volume button got pressed down, send right trigger (mouse down for time being)
		if(ev.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP && ev.getAction() == KeyEvent.ACTION_DOWN){
			mConnector.SendControlMessage(
					new MouseMessage(
							MouseMessage.LEFT_BUTTON,
							ControlMessage.CONTROL_DOWN,
							0, 0));
			return true;
		}
		//volume button release, send key-up
		else if(ev.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP && ev.getAction() == KeyEvent.ACTION_UP){
			mConnector.SendControlMessage(
					new MouseMessage(
							MouseMessage.LEFT_BUTTON,
							ControlMessage.CONTROL_UP,
							0, 0));
			return true;
		}
		//process all other key events normally
		else
			return super.dispatchKeyEvent(ev);
		
	}
	
	//orientation
	void send_orientation(float yaw, float pitch){
		yaw = -yaw/8;
		pitch = pitch/8;
		
		yaw = yaw * 65000;
		pitch = pitch * 65000;
		
		mConnector.SendControlMessage(
				new MouseMessage(
						MouseMessage.TOUCH_1,
						ControlMessage.CONTROL_MOVE,
						(int) yaw, (int) pitch
				));
		
	}

	//have to do it this way due to multitouch
	@SuppressWarnings("deprecation")
	private void init_background(){
		RelativeLayout background = (RelativeLayout)this.findViewById(R.id.background);
		
		background.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return backgroundTouch(event);
			}
		});
		
	}
	
	private boolean backgroundTouch(MotionEvent ev){
		int pointerCount = 1;
		
		if(multiEnabled)
			pointerCount = WrappedMotionEvent.getPointerCount(ev);
		
		//loop through registered touches
		for(int i=0; i<pointerCount; i++){
			
			float touchx = WrappedMotionEvent.getX(ev, i);
			float touchy = WrappedMotionEvent.getY(ev, i);
			
			//figure out which button this touch is on, if any
			for(int k=0; k<8; k++)
				if(rects[k].contains((int)touchx, (int)touchy))
					handle_event(ev, touchx, touchy, k);				
			
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void carriage_event(MotionEvent ev, float ev_x, float ev_y){
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		int cosX = 0;
		int cosY = 0;
		
		carriage.removeAllViews();
		//load bitmap for the head
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
		
		//w
		if(yMove < -carriage.getHeight()/4 +head_height/2){
			if(keypress[0]==false){
				this.dualSendKey((int)'w', 0);
			}
			keypress[0] = true;

		}
		else{
			if(keypress[0] == true){
				this.dualSendKey((int)'w', 1);
			}
			keypress[0] = false;
		}
		//a
		if(xMove < -carriage.getWidth()/4 + head_width/2){
			if(keypress[1] == false)
				this.dualSendKey((int)'a', 0);
			keypress[1] = true;
		}
		else{
			if(keypress[1] == true)
				this.dualSendKey((int)'a', 1);
			keypress[1] = false;
		}
		//s
		if(yMove > carriage.getHeight()/4 - head_height/2){
			if(keypress[2] == false)
				this.dualSendKey((int)'s', 0);
			keypress[2] = true;
		}
		else{
			if(keypress[2])
				this.dualSendKey((int)'s', 1);
			keypress[2] = false;
		}
		//d
		if(xMove > carriage.getWidth()/4 - head_width/2){
			if(!keypress[3])
				this.dualSendKey((int)'d', 0);
			keypress[3] = true;
		}
		else{
			if(keypress[3])
				this.dualSendKey((int)'d', 1);
			keypress[3] = false;
		}
	
		
		
		
	}
	
	private void dualSendKey(int keycode, int type){
			
		if(type == 0){
			mConnector.SendControlMessage(
				new KeyboardMessage(
						keycode,
						ControlMessage.CONTROL_DOWN,
						keycode, 0) );
		}
		else if(type == 1){
			mConnector.SendControlMessage(
					new KeyboardMessage(
						keycode,
						ControlMessage.CONTROL_UP,
						keycode, 0) );
			
		}

	}
	
	//TODO: figure out what messages to send for this
	private void button_event(MotionEvent ev, int which){
		//carriage, start, select, y, x, a, b, t
		switch(which){
		//start
		case 1:
			//send escape key (happens to be ASCII code 37)
			mConnector.SendControlMessage(
					new KeyboardMessage(
							37,
							ControlMessage.CONTROL_DOWN,
							37, 0));
			mConnector.SendControlMessage(
					new KeyboardMessage(
							37,
							ControlMessage.CONTROL_UP,
							37, 0));
			break;
		//select
		case 2:
			//press i (for minecraft inventory) {for now}
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'i',
							ControlMessage.CONTROL_DOWN,
							(int)'i', 0));
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'i',
							ControlMessage.CONTROL_UP,
							(int)'i', 0));
			break;
		//y
		case 3:
			//switch weapon (mouse scroll wheel?)
			mConnector.SendControlMessage(
					new MouseMessage(
							MouseMessage.MIDDLE_BUTTON,
							ControlMessage.CONTROL_MOVE,
							1, 0));
			break;
		//x
		case 4:
			//send use (e)
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'e',
							ControlMessage.CONTROL_DOWN,
							(int)'e', 0));
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'e',
							ControlMessage.CONTROL_UP,
							(int)'e', 0));
			break;
		//a
		case 5:
			//for now, send spacebar (jump)
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)' ',
							ControlMessage.CONTROL_DOWN,
							(int)' ', 0));
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)' ',
							ControlMessage.CONTROL_UP,
							(int)' ', 0));
			break;
		//b
		case 6:
			//send reload (r)
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'r',
							ControlMessage.CONTROL_DOWN,
							(int)'r', 0));
			mConnector.SendControlMessage(
					new KeyboardMessage(
							(int)'r',
							ControlMessage.CONTROL_UP,
							(int)'r', 0));
			break;
		//t
		case 7:
			if(ev.getAction() == MotionEvent.ACTION_DOWN)
				mConnector.SendControlMessage(
						new MouseMessage(
								MouseMessage.RIGHT_BUTTON,
								ControlMessage.CONTROL_DOWN,
								0, 0));
			else if(ev.getAction() == MotionEvent.ACTION_UP)
				mConnector.SendControlMessage(
						new MouseMessage(
								MouseMessage.RIGHT_BUTTON,
								ControlMessage.CONTROL_UP,
								0, 0));
			break;
		}
		
	}
	
	//interpret our touch
	private void handle_event(MotionEvent ev, float touchx, float touchy, int touched){
		//carriage, start, select, y, x, a, b, t
		if(touched==0)
			carriage_event(ev, touchx, touchy);
		else
			button_event(ev, touched);
		
		
	}
	
	
	
}

