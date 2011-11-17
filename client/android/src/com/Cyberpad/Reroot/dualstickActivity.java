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
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;
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
	private SurfaceView test;
	
	//let's do animations
	
	
	/** Called when the activity is first created. */
	@Override
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
		//initRightCarriage();
		
	}
	
	/*//custom view class
	class dualstickView extends SurfaceView implements SurfaceHolder.Callback {
		private dualstickThread _thread;
		private int _x = 20;
		private int _y = 20;
		
		public dualstickView(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new dualstickThread(getHolder(), this);
			setFocusable(true);
			
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event){
			_x = (int) event.getX();
			_y = (int) event.getY();
			return true;
		}

		@Override
		public void onDraw(Canvas canvas){
			Bitmap l_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
			canvas.drawColor(Color.BLACK);
			canvas.drawBitmap(l_head,  _x, _y, null);
			
		}
		
		//@Override
	 	//public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		        // TODO Auto-generated method stub
		//}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder){
			_thread = new dualstickThread(getHolder(), this);
			_thread.setRunning(true);
			_thread.start();
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder){
			boolean retry = true;
			_thread.setRunning(false);
			while(retry){
				try{
					_thread.join();
				}
				catch(InterruptedException e){
					//keep trying...
				}
			}
		}
			
	}
	
	class dualstickThread extends Thread{
		private SurfaceHolder _surfaceHolder;
		private dualstickView _view;
		private boolean _run = false;
		
		public dualstickThread(SurfaceHolder surfaceHolder, dualstickView dview){
			_surfaceHolder = surfaceHolder;
			_view = dview;
		}
		
		public void setRunning(boolean run){
			_run = run;
		}
		
		@Override
		public void run(){
			Canvas c;
			while(_run){
				c = null;
				try{
					c = _surfaceHolder.lockCanvas(null);
					synchronized(_surfaceHolder){
						_view.onDraw(c);
					}
				}
				finally{
					if(c != null){
						_surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
		
	}*/
	//initializations
	private void initLeftCarriage(){
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage);
		
		left_carriage.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent ev){
				return onLeftMove( ev );
        	}
        });
	}
	
	private void initRightCarriage(){
		AbsoluteLayout right_carriage = (AbsoluteLayout)this.findViewById(R.id.right_carriage);
		
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
		//ImageView left = (ImageView)this.findViewById(R.id.left_head);
		AbsoluteLayout left_carriage = (AbsoluteLayout)this.findViewById(R.id.left_carriage);
		left_carriage.removeAllViews();
			
		//load the bitmap
		Bitmap left_head = BitmapFactory.decodeResource(getResources(), R.drawable.dualstick_head);
		
		//make a drawble from the bitmap
		BitmapDrawable bmd = new BitmapDrawable(left_head);
		ImageView left = new ImageView(this);
		left.setImageDrawable(bmd);
		
		
		
		//make a matrix
		/*Matrix matrix = left.getImageMatrix();
		if(matrix == null){
			Toast.makeText(dualstickActivity.this,
					"Movement detected..."+ xMove + "," +yMove, Toast.LENGTH_SHORT).show();
		}*/
		
		//testing with moving dualsticks around
		//float imageWidth = 12.6f;
		//float imageHeight = 12.6f;
		//RectF drawableRect = new RectF(0, 0, imageWidth, imageHeight);//new RectF(0, 0, 12.6, 12.6);
		//RectF viewRect = new RectF(0, 0, left.getWidth(), left.getHeight());
		//matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
		
		//left.setImageMatrix(matrix);
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = 0;
				yMove = 0;
				type = 0;
				this.left_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				//this.l_head_x = left.getLeft();
				//this.head_y = left.getTop();
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				this.left_xHistory = this.left_yHistory = 0;		
				
				//make it snap back to its start coordinates
				//matrix.postTranslate(this.l_head_x, this.head_y);
				
				//left.setImageMatrix(matrix);
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - this.left_xHistory;
				yMove = ev.getY() - this.left_yHistory;
				
				this.left_xHistory = ev.getX();
				this.left_yHistory = ev.getY();
				
				//move the joystick head
				//matrix.postTranslate(left.getLeft()+xMove, left.getTop()+yMove);
				
				//left.setImageMatrix(matrix);
				
				//move the dual stick head to reflect movement
				//left.scrollBy((int)xMove, (int)yMove);
				//Toast.makeText(dualstickActivity.this,
				//		"Movement detected..."+ xMove + "," +yMove, Toast.LENGTH_SHORT).show();
				
				break;	
		}
		
		//0 is a touch down, 1 is a release, 2 is a move
		//send message here
		
		left_carriage.addView(left,
				new AbsoluteLayout.LayoutParams(left_head.getWidth(), left_head.getHeight(), 
						95-(int)(left_head.getWidth()/2)+(int)xMove, 90-(int)(left_head.getHeight()/2)+(int)yMove));

		
		
		return true;
	}
	
	//handles movement in the right carriage
	private boolean onRightMove( MotionEvent ev ){
		int type = -1;
		float xMove = 0f;
		float yMove = 0f;
		//ImageView right = (ImageView)findViewById(R.id.right_head);
		
		
		switch(ev.getAction()){
			//finger down
			case MotionEvent.ACTION_DOWN:
				xMove = 0;
				yMove = 0;
				type = 0;
				this.right_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				//this.r_head_x = right.getLeft();
				
				break;
			//finger up
			case MotionEvent.ACTION_UP:
				type = 1;
				xMove = 0;
				yMove = 0;
				this.right_xHistory = this.right_yHistory = 0;
				//right.scrollTo(r_head_x, head_y);
				
				break;
			//finger moved
			case MotionEvent.ACTION_MOVE:
				type = 2;
				xMove = ev.getX() - this.right_xHistory;
				yMove = ev.getY() - this.right_yHistory;
				
				this.right_xHistory = ev.getX();
				this.right_yHistory = ev.getY();
				
				//right.scrollBy((int)xMove, (int)yMove);
				
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