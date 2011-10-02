package com.Cyberpad.Reroot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class RerootActivity extends Activity {
	private EditText text;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (EditText)findViewById(R.id.editText1);
        //TextView tv = new TextView(this);
        //tv.setText("Hello, Android!");
        //setContentView(tv);
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
    		
    		Toast.makeText(this, "Message received", Toast.LENGTH_LONG).show();
    			
    	}
    }
   
}
