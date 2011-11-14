package com.Cyberpad.Reroot;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Crypto {
	
	// Remote public keys (Used for Encryption and Signing Verification)
	private RSAPublicKey mRemotePubEncKey;
	private RSAPublicKey mRemotePubSignKey;
	private boolean mRemoteKeysSet = false;
	
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
		BigInteger encMod = new BigInteger( preferences.getString( "enc_mod", "0" ) );
		BigInteger encPubExp = new BigInteger( preferences.getString( "enc_pub_exp", "0" ) );
		BigInteger encPrivExp = new BigInteger( preferences.getString( "enc_priv_exp", "0" ) );
		BigInteger signMod = new BigInteger( preferences.getString( "sign_mod", "0" ) );
		BigInteger signPubExp = new BigInteger( preferences.getString( "sign_pub_exp", "0" ) );
		BigInteger signPrivExp = new BigInteger( preferences.getString( "sign_priv_exp", "0" ) );
		RSAPublicKeySpec encPubKeySpec = new RSAPublicKeySpec( encMod, encPubExp );
		RSAPrivateKeySpec encPrivKeySpec = new RSAPrivateKeySpec( encMod, encPrivExp );
		RSAPublicKeySpec signPubKeySpec = new RSAPublicKeySpec( signMod, signPubExp );
		RSAPrivateKeySpec signPrivKeySpec = new RSAPrivateKeySpec( signMod, signPrivExp );
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			mLocalPubEncKey = ( RSAPublicKey )keyFactory.generatePublic( encPubKeySpec );
			mLocalPrivEncKey = ( RSAPrivateKey )keyFactory.generatePrivate( encPrivKeySpec );
			mLocalPubSignKey = ( RSAPublicKey )keyFactory.generatePublic( signPubKeySpec );
			mLocalPrivSignKey = ( RSAPrivateKey )keyFactory.generatePublic( signPrivKeySpec );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to generate local keys. Exception: " + e.getMessage() );
		}
	}
	
	public void SetRemoteKeys( String encMod, String encExp, String signMod, String signExp )
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
			Log.d( "Crypto", "Failed to set remote keys. Exception: "+ e.getMessage() );
			mRemoteKeysSet = false;
		}
		mRemoteKeysSet = true;
	}
	
	public boolean RemoteKeysSet() { return mRemoteKeysSet; }

	// Encrypt message, return signature
	public String Encrypt( String ioMessage )
	{
		// If we havn't set any remote keys yet, we have no way to encrypt the message.
		if( !mRemoteKeysSet )
			return "";
		
		// Sign the message
		String signature = "";
		try {
			Signature instance = Signature.getInstance( "SHA1withRSA" );
			instance.initSign( mLocalPrivSignKey );
			instance.update( ioMessage.getBytes() );
			signature = ByteArrayToHexString( instance.sign() );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to sign message. Exception: " + e.getMessage() );
		}
		
		// Encrypt the message
		try {
			Cipher cipher = Cipher.getInstance( "RSA" );
			cipher.init( Cipher.ENCRYPT_MODE, mRemotePubEncKey );
			ioMessage = ByteArrayToHexString( cipher.doFinal( ioMessage.getBytes() ) );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to encrypt message. Exception: " + e.getMessage() );
		}
		
		// Return the signature
		return signature;
	}
	
	// Decrypt message if possible
	public boolean Decrypt( String ioMessage, String inSignature )
	{	
		// Decrypt message
		try {
			Cipher cipher = Cipher.getInstance( "RSA" );
			cipher.init( Cipher.DECRYPT_MODE, mLocalPrivEncKey );
			ioMessage = ByteArrayToHexString( cipher.doFinal( ioMessage.getBytes() ) );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to decrypt message. Exception: " + e.getMessage() );
		}
		
		// If we don't have any remote keys, we can't verify the message
		if( !mRemoteKeysSet )
			return false;
		
		// Verify signature
		boolean verified = false;
		try {
			Signature instance = Signature.getInstance( "SHA1withRSA" );
			instance.initVerify( mRemotePubSignKey );
			instance.update( ioMessage.getBytes() );
			verified = instance.verify( inSignature.getBytes() );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to verify signature on message. Exception: " + e.getMessage() );
		}
		return verified;
	}
	
	public RSAPublicKey GetPublicEncryptionKey() { return mLocalPubEncKey; }
	public RSAPublicKey GetPublicSigningKey() { return mLocalPubSignKey; }
	
	// Helper function for converting string to hex
	// This method should be implemented Server side too!
	private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static String ByteArrayToHexString(byte[] bytes) {
	    StringBuilder sb = new StringBuilder(bytes.length * 2);
	    for (final byte b : bytes) {
	        sb.append(hex[(b & 0xF0) >> 4]);
	        sb.append(hex[b & 0x0F]);
	    }
	    return sb.toString();
	}
}
