package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PresentationMode extends Activity {

private Handler handler = new Handler();
private static final String TAG = "Reroot";
private Connector mConnector;
//accelerometer stuff (--using orientation instead at the moment)
private float cur_acc[] = {0, 0, 0};
private float last_acc[] = {0, 0, 0};
//click button stuff
private long tap_time = 0;

//orientation stuff (red button)
private float last_or[] = {0, 0, 0};
private float cur_or[] = {0, 0, 0};

private SensorManager mSensorManager;
private final SensorEventListener mSensorListener = new SensorEventListener(){
	public void onSensorChanged(SensorEvent se){		
		if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			//record last values
			for(int i=0; i<3; i++)
				last_acc[i] = cur_acc[i];
			Log.i("Reroot", "old:" + last_acc[0] +", " + last_acc[1] +", " + last_acc[2]);
			//read current values
			for(int i=0; i<3; i++)
				cur_acc[i] = se.values[i];
			
			Log.i("Reroot", se.values[0] + ", " + se.values[1] + ", " + se.values[2]);
			//for now, we care about x and z
			send_offset(cur_acc[0] - last_acc[0], cur_acc[2] - last_acc[2]);
		}
		else if(se.sensor.getType() == Sensor.TYPE_ORIENTATION){
			//we care about pitch and yaw
			//for(int i=0; i<3; i++)
			//	last_or[i] = cur_or[i];
			for(int i=0; i<3; i++)
				cur_or[i] = se.values[i];
			
			send_or(cur_or[2], cur_or[1]);
			
		}
		
		
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy){}
};

private Sensor mAccelerometer;
private Sensor mOrientation;
private final float NOISE = (float) 2.0;
	
// Called when the activity is first created. 
@Override
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	//set display to our presentation layout
	setContentView(R.layout.presentation_layout);
	//accelerometer stuff
	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);	
	
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

protected void onResume(){
	super.onResume();
	//mSensorManager.unregisterListener(mSensorListener);
}

protected void onPause(){
	super.onPause();
	mSensorManager.unregisterListener(mSensorListener);
}
	

private void initClick(){
	RelativeLayout clickBtn = (RelativeLayout)this.findViewById(R.id.presen_click);
	
	clickBtn.setOnTouchListener(new View.OnTouchListener(){
		public boolean onTouch(View v, MotionEvent ev){
			return click(ev);
		}
	});
}

private void initLeft(){
	RelativeLayout leftBtn = (RelativeLayout)this.findViewById(R.id.presen_left);
	
	leftBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			left_or_right("left");
		}
	});
}

private void initRight(){
	RelativeLayout rightBtn = (RelativeLayout)this.findViewById(R.id.presen_right);
	
	rightBtn.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v){
			left_or_right("right");
		}
	});
	
}

private void initLaser(){
	RelativeLayout laserBtn = (RelativeLayout)this.findViewById(R.id.presen_laser);
	
	
	laserBtn.setOnTouchListener(new View.OnTouchListener(){
		public boolean onTouch(View v, MotionEvent ev){
			return laser(ev);
		}
	});
}


boolean click(MotionEvent ev){
	//record time that the finger went down, turn on orientator
	if(ev.getAction() == MotionEvent.ACTION_DOWN){
		this.tap_time = System.currentTimeMillis();
		mSensorManager.registerListener(mSensorListener, mOrientation, SensorManager.SENSOR_DELAY_GAME);
		Log.i("Reroot", "turning on orientator");
	}
	//turn off accelerometer and laser
	else if(ev.getAction() == MotionEvent.ACTION_UP){
		mSensorManager.unregisterListener(mSensorListener);
		Log.i("Reroot", "turning off orientator");
		//if they are within click threshold, send a click
		long now = System.currentTimeMillis();
		if(now - this.tap_time < 200){
				mConnector.SendControlMessage(
			new MouseMessage(
					MouseMessage.LEFT_BUTTON,
					ControlMessage.CONTROL_DOWN,
					0, 0)
			);
	mConnector.SendControlMessage(
			new MouseMessage(
					MouseMessage.LEFT_BUTTON,
					ControlMessage.CONTROL_UP,
					0, 0)
			);
			
		}
	}
	
	return true;
}

void left_or_right(String id){
	//send advance back
	if(id == "left")
		;
	//send advance forward
	else if(id == "right")
		;
	
}

//for accelerometer (testing)
void send_offset(float x, float z){
	//scale up for accuracy
	x = x*65000*5;
	z = z*65000*5;
	
	Log.i("Reroot", "Sending " + x + " and " + z);
	
	mConnector.SendControlMessage(
			new MouseMessage(
					MouseMessage.TOUCH_1,
					ControlMessage.CONTROL_MOVE,
					(int)x, (int)z)
			);
	
	
}

//for orientation(testing) - note - i'm using traditional terms for yaw and pitch,
//whilst android's dimensional scheme is a little different
//yaw is <- -> , pitch is up/down
void send_or(float yaw, float pitch){
	yaw = -yaw/8;
	pitch = pitch/8;
	
	yaw = yaw * 65000;
	pitch = pitch * 65000;
	
	Log.i("Reroot", "Sending " + yaw + " and " + pitch);
	mConnector.SendControlMessage(
			new MouseMessage(
					MouseMessage.TOUCH_1,
					ControlMessage.CONTROL_MOVE,
					(int) yaw, (int) pitch)
			);
	
}


boolean laser(MotionEvent ev){
	//turn on accelerometer, initiate laser
	if(ev.getAction() == MotionEvent.ACTION_DOWN){
		//mSensorManager.registerListener(mSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(mSensorListener, mOrientation, SensorManager.SENSOR_DELAY_GAME);
		//send laser on here
		Log.i("Reroot", "turning on accelerometer");
	}
	//turn off accelerometer and laser
	else if(ev.getAction() == MotionEvent.ACTION_UP){
		mSensorManager.unregisterListener(mSensorListener);
		//send laser off here
		Log.i("Reroot", "turning off accelerometer");
		
	}
	
	return true;
}
	

}
