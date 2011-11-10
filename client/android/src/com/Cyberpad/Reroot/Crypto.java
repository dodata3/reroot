package com.Cyberpad.Reroot;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Crypto {
	
	// Remote public keys (Used for Encryption and Signing Verification)
	private RSAPublicKey mRemotePubEncKey;
	private RSAPublicKey mRemotePubSignKey;
	
	// Local Key Pairs (Used for Decryption and Signing Messages)
	private RSAPublicKey mLocalPubEncKey;
	private RSAPrivateKey mLocalPrivEncKey;
	private RSAPublicKey mLocalPubSignKey;
	private RSAPrivateKey mLocalPrivSignKey;
	
	public Crypto( SharedPreferences preferences )
	{
		// Generate keys if necessary    
		if( !preferences.contains( "keys" ) )
		{
	        try{
	            KeyPairGenerator kpg = KeyPairGenerator.getInstance( "RSA" );
	            kpg.initialize( 2048 );
	            KeyPair kpEnc = kpg.genKeyPair();
	            KeyPair kpSign = kpg.genKeyPair();
	
	            KeyFactory fact = KeyFactory.getInstance( "RSA" );
	            RSAPublicKeySpec pubEnc = fact.getKeySpec( kpEnc.getPublic(), RSAPublicKeySpec.class );
	            RSAPrivateKeySpec privEnc = fact.getKeySpec( kpEnc.getPrivate(), RSAPrivateKeySpec.class );
	            RSAPublicKeySpec pubSign = fact.getKeySpec( kpSign.getPublic(), RSAPublicKeySpec.class );
	            RSAPrivateKeySpec privSign = fact.getKeySpec( kpSign.getPrivate(), RSAPrivateKeySpec.class );
	            
	            Editor edit = preferences.edit();
	    		edit.putString( "enc_mod", pubEnc.getModulus().toString() );
	    		edit.putString( "enc_pub_exp", pubEnc.getPublicExponent().toString() );
	    		edit.putString( "enc_priv_exp", privEnc.getPrivateExponent().toString() );
	    		edit.putString( "sign_mod", pubSign.getModulus().toString() );
	    		edit.putString( "sign_pub_exp", pubSign.getPublicExponent().toString() );
	    		edit.putString( "sign_priv_exp", privSign.getPrivateExponent().toString() );
	    		edit.putInt( "keys", 1 );
	    		edit.commit();
	        } catch( Exception e ) {
	            System.out.println( e.getMessage() );
	        }
		}
		
		// Set the local keys
		
	}
	
	public boolean SetRemoteKeys( String encMod, String encExp, String signMod, String signExp )
	{
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			BigInteger encKeyMod = new BigInteger( encMod );
			BigInteger encKeyExp = new BigInteger( encExp );
			BigInteger signKeyMod = new BigInteger( signMod );
			BigInteger signKeyExp = new BigInteger( signExp );
			RSAPublicKeySpec encKeySpec = new RSAPublicKeySpec( encKeyMod, encKeyExp );
			RSAPublicKeySpec signKeySpec = new RSAPublicKeySpec( signKeyMod, signKeyExp );
			mRemotePubEncKey = ( RSAPublicKey )keyFactory.generatePublic( encKeySpec );
			mRemotePubSignKey = ( RSAPublicKey )keyFactory.generatePublic( signKeySpec ); 
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
			return false;
		}
		return true;
	}

	// Encrypt message, return signature
	/*
	public String Encrypt( String ioMessage )
	{
		
	}
	
	public boolean Decrypt( CryptoPP::RSA::PrivateKey inEncKey, CryptoPP::RSA::PublicKey inSignKey, QString& ioMessage, QString& inSignature )
	{
		
	}
	*/
}
