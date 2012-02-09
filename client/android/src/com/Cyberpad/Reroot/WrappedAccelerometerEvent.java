package com.Cyberpad.Reroot;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Wraps the accelerometer methods available to Android
 * Credit should be given http://www.akeric.com/blog/?p=1313 for implementation method
 * 
 * @author Beth Werbaneth
 *
 */

public class WrappedAccelerometerEvent {
	SensorManager mSensorManager;
	accSensorListener acc_listener;
	Sensor acc_sensor;
	
	float [] acc_values;
	
	
	public void WrapedAccelerometerEvent(Context c){
		resume(c);
	};
	
	//need to call this method when you are leaving the event (on pause)
	void Pause(){
		mSensorManager.unregisterListener(acc_listener);
	}
	
	//call this when returning from a pause
	void resume(Context c){
		//this may work? (fingers crossed)
		mSensorManager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
		acc_listener = new accSensorListener();
		acc_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(acc_listener, acc_sensor, SensorManager.SENSOR_DELAY_GAME);					
	}
	
	//make a new listener class to interpret the data
	class accSensorListener implements SensorEventListener{
		public void onSensorChanged(SensorEvent event){
			int eventType = event.sensor.getType();
			if(eventType == Sensor.TYPE_ACCELEROMETER){
				acc_values = event.values;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//don't need to do anything here
		}
	}

}

/*
public class WrappedMotionEvent {

	/**
	 * Cached instance of empty Object array.
	 *//*
	private static Object[] mEmptyObjectArray = new Object[] {};

	static {
		initCompatibility();
	};

	private static void initCompatibility() {
		try {
			mMotionEvent_GetPointerCount = MotionEvent.class.getMethod("getPointerCount",
					new Class[] {});
			mMotionEvent_GetPointerId = MotionEvent.class.getMethod("getPointerId",
					new Class[] { int.class });
			mMotionEvent_GetX = MotionEvent.class.getMethod("getX", new Class[] { int.class });
			mMotionEvent_GetY = MotionEvent.class.getMethod("getY", new Class[] { int.class });
			/* success, this is a newer device *//*
			mIsMultitouchCapable = true;
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device *//*
			mIsMultitouchCapable = false;
		}
	}

	public static boolean isMultitouchCapable() {
		return mIsMultitouchCapable;
	}

	/**
	 * Reflected method call.
	 * 
	 * @param event
	 * @see android.view.MotionEvent#getPointerCount()
	 * @return
	 *//*
	public static int getPointerCount(MotionEvent event) {
		try {
			int pointerCount = (Integer) mMotionEvent_GetPointerCount.invoke(event,
					mEmptyObjectArray);
			return pointerCount;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		}
	}

	/**
	 * Reflected method call.
	 * 
	 * @param event
	 * @param pointerIndex
	 * @see android.view.MotionEvent#getPointerId(java.lang.Integer)
	 * @return
	 *//*
	public static int getPointerId(MotionEvent event, int pointerIndex) {
		try {
			int pointerCount = (Integer) mMotionEvent_GetPointerId.invoke(event,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		}
	}

	/**
	 * Reflected method call.
	 * 
	 * @param event
	 * @param pointerIndex
	 * @see android.view.MotionEvent#getX(java.lang.Integer)
	 * @return
	 *//*
	public static float getX(MotionEvent event, int pointerIndex) {
		try {
			float pointerCount = (Float) mMotionEvent_GetX.invoke(event,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		}
	}

	/**
	 * Reflected method call.
	 * 
	 * @param event
	 * @param pointerIndex
	 * @see android.view.MotionEvent#getY(java.lang.Integer)
	 * @return
	 *//*
	public static float getY(MotionEvent event, int pointerIndex) {
		try {
			float pointerCount = (Float) mMotionEvent_GetY.invoke(event,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Reflected multitouch method failed!", e);
		}
	}

}
*/