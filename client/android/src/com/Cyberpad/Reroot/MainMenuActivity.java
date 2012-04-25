package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

//the main menu class
public class MainMenuActivity extends Activity{
	private static final String TAG = "MainMenu";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_menu);
		Button hybrid_mode = (Button)findViewById(R.id.mouse_keyboard_btn);
		Button dualstick_mode = (Button)findViewById(R.id.game_controller_btn);
		//Button acc_test = (Button)findViewById(R.id.acc_test);
	
		 hybrid_mode.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v){
	        		onHybridClick();
	        	}
	        });
		 
		 dualstick_mode.setOnClickListener(new OnClickListener(){
			 public void onClick(View v){
				 onDualClick();
			 }
		 });
		 
		 /*
		 acc_test.setOnClickListener(new OnClickListener(){
			 public void onClick(View v){
				 onAccClick();
			 }
		 });
		 */
	}
	
	public void onAccClick(){
		try{
			Intent i = new Intent(this, PresentationMode.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
		}		
	}
	
	public void onHybridClick(){
		try{
			Intent i = new Intent(this, PadActivity.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show(); Log.d(TAG, ex.toString());
		}
	}
	
	public void onDualClick(){
		try{
			Intent i = new Intent(this, dualstickActivity.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show(); Log.d(TAG, ex.toString());
		}
	}
}