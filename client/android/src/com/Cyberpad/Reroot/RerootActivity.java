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
	//private EditText text[];
	EditText [] text = new EditText[4];
	SharedPreferences preferences;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text[0] = (EditText)findViewById(R.id.editText1);
        text[1] = (EditText)findViewById(R.id.editText2);
        text[2] = (EditText)findViewById(R.id.editText3);
        text[3] = (EditText)findViewById(R.id.editText4);
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
    		if(text[0].getText().length() == 0 || text[1].getText().length() == 0
    				|| text[2].getText().length() == 0 || text[3].getText().length() == 0){
    			Toast.makeText(this, "Please enter a valid number", 
    					Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		String ip_full = "";
    		
    		for(int i=0; i<4; i++){
    			//int inputValue = text[i].getText().toString();
    			String str = text[i].getText().toString();//Integer.toString(inputValue);
    			ip_full = ip_full + str;
    			//ip_full.concat(str);
    			if(i<3){
    				//ip_full.concat(".");    
    				ip_full = ip_full + ".";
    			}
    		}
    		
    		Editor edit = preferences.edit();
    		edit.putString("ip_address", ip_full/*buffer.toString()*/);
    		edit.commit();
    		
    		
    		//Toast.makeText(this, ip_full, Toast.LENGTH_LONG).show();
    			
    	}
    }
   
}
