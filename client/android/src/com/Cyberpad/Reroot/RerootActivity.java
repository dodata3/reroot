package com.Cyberpad.Reroot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;



public class RerootActivity extends Activity {
	private EditText text;
	SharedPreferences preferences;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (EditText)findViewById(R.id.editText1);
        Button button = (Button)findViewById(R.id.button2);
        //initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v){
        		String ip_address = preferences.getString("ip_address", "n/a");
        		Toast.makeText(RerootActivity.this,
						"Your saved ip address is: " + ip_address, Toast.LENGTH_LONG).show();
        		
        	}
        	
        });
    }
    
    
    
    
    //this method is called at button click
    public void myClickHandler(View view){
    	switch(view.getId()){
    	case R.id.button1:
    		if(text.getText().length() == 0){
    			Toast.makeText(this, "Please enter a valid number", 
    					Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		float inputValue = Float.parseFloat(text.getText().toString());
    		Editor edit = preferences.edit();
    		String str = Float.toString(inputValue);
    		//String username = preferences.getString("ip_address", "n/a");
    		StringBuffer buffer = new StringBuffer();
    		for(int i= str.length()-1; i>= 0; i--){
    			buffer.append(str.charAt(i));
    		}
    		edit.putString("ip_address", str/*buffer.toString()*/);
    		edit.commit();
    		
    		Toast.makeText(this, "Message received", Toast.LENGTH_LONG).show();
    			
    	}
    }
   
}
