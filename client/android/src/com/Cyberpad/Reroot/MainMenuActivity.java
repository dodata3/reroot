package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

//the main menu class
public class MainMenuActivity extends Activity{
	private static final String TAG = "MainMenu";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		Button hybrid_mode = (Button)findViewById(R.id.hybrid_mode);
		Button dualstick_mode = (Button)findViewById(R.id.dualstick_mode);
		Button acc_test = (Button)findViewById(R.id.acc_test);
		Button generic = (Button)findViewById(R.id.generic);
	
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
		 
		 acc_test.setOnClickListener(new OnClickListener(){
			 public void onClick(View v){
				 onAccClick();
			 }
		 });
		 
		 generic.setOnClickListener(new OnClickListener(){
			 public void onClick(View v){
				 onGenClick();
			 }
		 });
	}
	
	public void onGenClick(){
		try{
			Intent i = new Intent(this, generic_controller.class);
			this.startActivity(i);
		}
		catch(Exception ex){
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
		}		
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