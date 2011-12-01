package com.Cyberpad.Reroot;


//import java.util.Timer;
//import java.util.TimerTask;
import java.lang.Math;



import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


@SuppressWarnings("deprecation")
public class dualstickActivity extends Activity{
	private Connector mConnector;
	//private Handler handler = new Handler();
	
	//private FrameLayout left_carriage;
	//private FrameLayout right_carriage;
	
	SharedPreferences preferences;
	//private static final String TAG = "dualstickActivity";
	
	
	private boolean multiEnabled;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duallayout2);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//force the screen to always be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		multiEnabled = WrappedMotionEvent.isMultitouchCapable();
		
		mConnector = Connector.getInstance(this);
		
		//assume that we will have access to OSC connection and initialize buttons	
		initBackground();
		//initLeftCarriage();
		//initRightCarriage();
		//initCosmetic();
		
	}
	
	private void sendJoystickMessage(int type, float x, float y, int which){
		mConnector.SendControlMessage(
				new JoystickMessage(
						which,
						type,
						(int)x, (int)y));
		
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
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage_padded);
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage_padded);
		
		if (multiEnabled){
			pointerCount = WrappedMotionEvent.getPointerCount(ev);
		}
		
		
		//loop through registered touches
		for(int i=0; i<pointerCount; i++){
			
			float touchx = WrappedMotionEvent.getX(ev, i);
			float touchy = WrappedMotionEvent.getY(ev, i);
			
			Rect left_rect = new Rect();
			left_carriage.getHitRect(left_rect);
			Rect right_rect = new Rect();
			right_carriage.getHitRect(right_rect);
			//Toast.makeText(dualstickActivity.this,
			//		"rect"+left_rect.left + " " + left_rect.bottom, Toast.LENGTH_SHORT).show();
			
			//check to see if we're registering a touch in the left zone and handle it
			if(left_rect.contains((int)touchx, (int)touchy)){
				handle_event(ev, touchx, touchy, left_carriage, 0);			
			}
			//check to see if we're registering a touch in the right zone and handle it
			if(right_rect.contains((int)touchx, (int)touchy)){
				handle_event(ev, touchx-right_rect.left, touchy, right_carriage, 1);
			}		
		}

	
		return true;
		
	}
	
	private void handle_event(MotionEvent ev, float ev_x, float ev_y, AbsoluteLayout carriage, int which){
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
		this.sendJoystickMessage(type, xMove, yMove, which);
		
	}
	
}
