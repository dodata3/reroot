package com.Cyberpad.Reroot;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;



public class RerootActivity extends Activity {
	//private EditText text[];
	private static final String TAG = "Reroot";
	EditText [] ip_text = new EditText[4];
	EditText test_message;
	SharedPreferences preferences;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ip_text[0] = (EditText)findViewById(R.id.editText1);
        ip_text[1] = (EditText)findViewById(R.id.editText2);
        ip_text[2] = (EditText)findViewById(R.id.editText3);
        ip_text[3] = (EditText)findViewById(R.id.editText4);
        Button view_btn = (Button)findViewById(R.id.button2);
        test_message = (EditText)findViewById(R.id.editText5);
        Button transmit_btn = (Button)findViewById(R.id.button3);
        Button connect_btn = (Button)findViewById(R.id.connect_btn);
        
        //initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        if( !preferences.contains("key_pair") )
        	GenerateKeyPair();
        
        view_btn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v){
        		String ip_address = preferences.getString("ip_address", "n/a");
        		Toast.makeText(RerootActivity.this,
						"Your saved ip address is: " + ip_address, Toast.LENGTH_LONG).show();
        		
        	}
        	
        });
        transmit_btn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v){
        		if(test_message.getText().length() == 0){
        			Toast.makeText(RerootActivity.this, "Please enter a message", Toast.LENGTH_LONG).show();
        		}
        		else{
        			String message = test_message.getText().toString();
        			Toast.makeText(RerootActivity.this, message, Toast.LENGTH_LONG).show();
        		}
        	}
        });
        connect_btn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v){
        		onConnectButton();
        	}
        });
		/*Button but = (Button)this.findViewById(R.id.btnConnect);
		but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onConnectButton();
			}
		});*/
    }
    
    
    //this method is called at button click
    public void myClickHandler(View view){
    	switch(view.getId()){
    	case R.id.button1:
    		if(ip_text[0].getText().length() == 0 || ip_text[1].getText().length() == 0
    				|| ip_text[2].getText().length() == 0 || ip_text[3].getText().length() == 0){
    			Toast.makeText(this, "Please enter a valid number", 
    					Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		String ip_full = "";
    		
    		for(int i=0; i<4; i++){
    			String str = ip_text[i].getText().toString();//Integer.toString(inputValue);
    			ip_full = ip_full + str;
    			if(i<3){
    				ip_full = ip_full + ".";
    			}
    		}
    		
    		Editor edit = preferences.edit();
    		edit.putString("ip_address", ip_full);
    		edit.commit();
    		
    		//Toast.makeText(this, ip_full, Toast.LENGTH_LONG).show();
    	}
    	
    		
    }
    
    private void onConnectButton(){
    	String ip_address = preferences.getString("ip_address", "n/a");
    	if (ip_address.matches("^[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}$")){
    		try{
    			Intent i = new Intent(this, PadActivity.class);
    			this.startActivity(i);
    			this.finish();
    		}
    		catch(Exception ex){
    			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show(); Log.d(TAG, ex.toString());
    		}
    		
    	}
    	
    }
    
    public void GenerateKeyPair()
    {       
        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();

            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
              RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
              RSAPrivateKeySpec.class);
            
            Editor edit = preferences.edit();
    		edit.putString("public_key_mod", pub.getModulus().toString());
    		edit.putString("public_key_exp", pub.getPublicExponent().toString());
    		edit.putString("private_key_mod", priv.getModulus().toString());
    		edit.putString("private_key_exp", priv.getPrivateExponent().toString());
    		edit.putInt("key_pair", 1);
    		edit.commit();
    		
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
