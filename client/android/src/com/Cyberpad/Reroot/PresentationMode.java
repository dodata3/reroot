package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
public class PresentationMode extends Activity implements SensorEventListener {
	
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
	 
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}

	public void onSensorChanged(SensorEvent event) {
		TextView tvX= (TextView)findViewById(R.id.x_axis);
		TextView tvY= (TextView)findViewById(R.id.y_axis);
		TextView tvZ= (TextView)findViewById(R.id.z_axis);
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvX.setText("0.0");
			tvY.setText("0.0");
			tvZ.setText("0.0");
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvX.setText(Float.toString(x));
			tvY.setText(Float.toString(y));
			tvZ.setText(Float.toString(z));
		}
	}
}
*/



public class PresentationMode extends Activity implements SensorEventListener {

private Handler handler = new Handler();
private static final String TAG = "Reroot";
private Connector mConnector;
//accelerometer stuff
private boolean mInitialized;
private float mLastX, mLastY, mLastZ;
private SensorManager mSensorManager;
private Sensor mAccelerometer;
private final float NOISE = (float) 2.0;
	
// Called when the activity is first created. 
@Override
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	//set display to our presentation layout
	setContentView(R.layout.presentation_layout);
	//accelerometer stuff
	mInitialized = false;
	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
	
	
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
	mSensorManager.unregisterListener(this);
}

protected void onPause(){
	super.onPause();
	mSensorManager.unregisterListener(this);
}
	
@Override
public void onAccuracyChanged(Sensor sensor, int accuracy){
	// IGNORRRRRRE ME!
}

@Override
public void onSensorChanged(SensorEvent event){
	//TextView txt = (TextView)findViewById(R.id.acctext);
	
	float x = event.values[0];
	float y = event.values[1];
	float z = event.values[2];
	
	if(!mInitialized){
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		
		//txt.setText("X: 0, Y: 0, Z: 0");
		
		mInitialized = true;
	}
	else{
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);
		
		if(deltaX < NOISE) deltaX = (float)0.0;
		if(deltaY < NOISE) deltaY = (float)0.0;
		if(deltaZ < NOISE) deltaZ = (float)0.0;
		
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		
		//txt.setText("X: " + Float.toString(deltaX) + ", Y:" + Float.toString(deltaY) + ", Z:" + Float.toString(deltaZ));
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
