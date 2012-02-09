package com.Cyberpad.Reroot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class PresentationMode extends Activity {
	
private Handler handler = new Handler();
private static final String TAG = "Reroot";
private Connector mConnector;
	
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	//set display to our presentation layout
	setContentView(R.layout.presentation_layout);
	
	mConnector = Connector.getInstance(this);
	
	//initialize buttons
	try{
		this.initClick();
		this.initLaser();
		this.initLeft();
		this.initRight();
	}
	catch(Exception ex){
		Log.d(TAG, "Could not create the OSCPort");
    	Log.d(TAG, ex.toString());
	}
	
}
	
private void initClick(){
	RelativeLayout clickBtn = (RelativeLayout)this.findViewById(R.id.presen_click);
	
	clickBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			buttonClicked("click");
		}
	});
}

private void initLeft(){
	RelativeLayout leftBtn = (RelativeLayout)this.findViewById(R.id.presen_left);
	
	leftBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			buttonClicked("left");
		}
	});
}

private void initRight(){
	RelativeLayout rightBtn = (RelativeLayout)this.findViewById(R.id.presen_right);
	
	rightBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			buttonClicked("right");
		}
	});
	
}

private void initLaser(){
	RelativeLayout laserBtn = (RelativeLayout)this.findViewById(R.id.presen_laser);
	
	laserBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			buttonClicked("laser");
		}
	});
}


//button implementations
void buttonClicked(String id){
	if(id=="click")
		mConnector.SendControlMessage(
				new MouseMessage(
						MouseMessage.LEFT_BUTTON,
						ControlMessage.CONTROL_DOWN,
						0, 0)
				);
	else if(id=="right")
		;
	else if(id=="left")
		;
	else if(id=="laser")
		;
	
}
	

}

/*



//mouse zone

private boolean onMouseMove(MotionEvent ev) {
	int type = -1;
	float xMove = 0f;
	float yMove = 0f;
	
	int pointerCount = 1;
	if (multiEnabled)
		pointerCount = WrappedMotionEvent.getPointerCount(ev);
	
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
				
				int new_type = -1;
				
				if(elapsed <= 200){
					//Toast.makeText(PadActivity.this,
					//		"Pointer count is " + pointerCount, Toast.LENGTH_SHORT).show();

					//register the tap and send a click
					if(lastPointerCount  == 1)
						new_type = 0;
					else if(lastPointerCount == 2){
						new_type = 3;	
					}
				}
				else{
					//too much time passed to be a tap
					this.last_tap = 0;
				}
				this.tapstate = "no_tap";
				
				if(new_type >= 0){
					this.sendMouseEvent(new_type, xMove, yMove);
				}
				
			}
			
			
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
					int multi_type = -1;
					//check for pinch out
					if(Math.sqrt(Math.pow(x1pos-x2pos,2) + Math.pow(y1pos-y2pos, 2)) > 
						Math.sqrt(Math.pow(x1History - x2History, 2) + Math.pow(y1History - y2History, 2))+50){
						//send pinch out command
						multi_type = 1;
					}
					//check for pinch in
					else if(Math.sqrt(Math.pow((double)(x1pos-x2pos),2) + Math.pow(y1pos-y2pos, 2)) + 50< 
							Math.sqrt(Math.pow(x1History - x2History, 2) + Math.pow(y1History - y2History, 2))){
						//send pinch in command
						multi_type = 0;
					}
					//check for vertical scroll down
					else if(y1pos > y1History + 20 && y2pos > y2History + 20){
						//send vertical scroll down command
						multi_type = 3;
					}
					//check for vertical scroll up
					else if(y1pos < y1History + 20 && y2pos < y2History + 20){
						//send vertical scroll up command
						multi_type = 2;
					}
					//check for horizontal scroll right
					else if(x1pos > x1History + 20 && x2pos > x2History + 20){
						//send horizontal scroll right
						multi_type = 5;
					}
					//check for horizontal scroll left
					else if(x1pos < x1History + 20 && x2pos < x2History + 20){
						//send horizontal scroll left command
						multi_type = 4;
					}
					
					if(multi_type >=0)
						this.sendMultitouchEvent(multi_type);
				}
				
				
			}
			break;
	}
	
	lastPointerCount = pointerCount;
	
	//0 is a left click, 1 is a release, 2 is a move, 3 is a right click
	if(type >= 0 ){
		this.sendMouseEvent(type, xMove, yMove);
	}
	return true;
	
}

}*/