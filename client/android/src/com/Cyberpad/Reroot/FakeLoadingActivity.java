package com.Cyberpad.Reroot;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.app.PendingIntent;


public class FakeLoadingActivity extends Activity {
	
	private BroadcastReceiver mAuthReceiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	          // TODO Auto-generated method stub
	          Intent connectPickerIntent = new Intent( FakeLoadingActivity.this, ConnectPickerActivity.class);
	          startActivity( connectPickerIntent );
	          finish();
	      }
	};
	
	@Override
	public void onCreate( Bundle icicle ) {
		super.onCreate(icicle);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView( R.layout.loading);
		
		ProgressBar progress = (ProgressBar)findViewById( R.id.fake_progress);
		progress.setIndeterminate(true);
		
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		IntentFilter filter = new IntentFilter();
	    filter.addAction( Connector.AUTH_INTENT );
	    registerReceiver( mAuthReceiver, filter );
	}
}