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
		// We want to save the keys in preferences because task may be interrupted by other apps
		// Bah, but the server public keys are lost anyway.  We will just have to reconnect.
		// Store these keys in the preferences if absolutely necessary    
        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance( "RSA" );
            kpg.initialize( 512 );
            KeyPair kpEnc = kpg.genKeyPair();
            KeyPair kpSign = kpg.genKeyPair();
            
            mLocalPubEncKey = ( RSAPublicKey ) kpEnc.getPublic();
            mLocalPrivEncKey = ( RSAPrivateKey ) kpEnc.getPrivate();
            mLocalPubSignKey = ( RSAPublicKey ) kpSign.getPublic();
            mLocalPrivSignKey = ( RSAPrivateKey ) kpSign.getPrivate();
        } catch( Exception e ) {
            System.out.println( e.getMessage() );
        }
	}
	
	public void SetRemoteKeys( String encMod, String encExp, String signMod, String signExp )
	{	
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			/*
			BigInteger encKeyMod = new BigInteger( Utility.HexStringToByteArray( encMod ) );
			BigInteger encKeyExp = new BigInteger( Utility.HexStringToByteArray( encExp ) );
			BigInteger signKeyMod = new BigInteger( Utility.HexStringToByteArray( signMod ) );
			BigInteger signKeyExp = new BigInteger( Utility.HexStringToByteArray( signExp ) );
			*/
			
			BigInteger encKeyMod = new BigInteger( encMod, 16 );
			BigInteger encKeyExp = new BigInteger( encExp, 16 );
			BigInteger signKeyMod = new BigInteger( signMod, 16 );
			BigInteger signKeyExp = new BigInteger( signExp, 16 );
			RSAPublicKeySpec encKeySpec = new RSAPublicKeySpec( encKeyMod, encKeyExp );
			RSAPublicKeySpec signKeySpec = new RSAPublicKeySpec( signKeyMod, signKeyExp );
			mRemotePubEncKey = ( RSAPublicKey )keyFactory.generatePublic( encKeySpec );
			mRemotePubSignKey = ( RSAPublicKey )keyFactory.generatePublic( signKeySpec ); 
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to set remote keys. Exception: "+ e.getMessage() );
			mRemoteKeysSet = false;
			return;
		}
		mRemoteKeysSet = true;
	}
	
	public boolean RemoteKeysSet() { return mRemoteKeysSet; }

	// Encrypt message, return signature
	public EncryptedMessage Encrypt( String ioMessage )
	{
		// If we havn't set any remote keys yet, we have no way to encrypt the message.
		EncryptedMessage ret = new EncryptedMessage();
		if( !mRemoteKeysSet )
			return ret;
		
		// Sign the message
		try {
			Signature instance = Signature.getInstance( "SHA1withRSA" );
			instance.initSign( mLocalPrivSignKey );
			instance.update( ioMessage.getBytes() );
			ret.mSignature = Utility.ByteArrayToHexString( instance.sign() );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to sign message. Exception: " + e.getMessage() );
		}
		
		// Encrypt the message
		try {
			Cipher cipher = Cipher.getInstance( "RSA/NONE/PKCS1Padding", "BC" );
			cipher.init( Cipher.ENCRYPT_MODE, mRemotePubEncKey );
			ret.mCipherText = Utility.ByteArrayToHexString( cipher.doFinal( ioMessage.getBytes() ) );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to encrypt message. Exception: " + e.getMessage() );
		}
		
		// Return the signature
		return ret;
	}
	
	// Decrypt message if possible
	public String Decrypt( EncryptedMessage inMessage )
	{	
		// If we don't have any remote keys, we can't verify the message
		if( !mRemoteKeysSet )
			return "";
		
		// Decrypt message
		String plainText = "";
		try {
			Cipher cipher = Cipher.getInstance( "RSA/NONE/PKCS1Padding", "BC" );
			cipher.init( Cipher.DECRYPT_MODE, mLocalPrivEncKey );
			plainText = Utility.ByteArrayToHexString( cipher.doFinal( inMessage.mCipherText.getBytes() ) );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to decrypt message. Exception: " + e.getMessage() );
		}
		
		// Verify signature
		boolean verified = false;
		try {
			Signature instance = Signature.getInstance( "SHA1withRSA", "BC" );
			instance.initVerify( mRemotePubSignKey );
			instance.update( plainText.getBytes() );
			verified = instance.verify( inMessage.mSignature.getBytes() );
		} catch( Exception e ) {
			Log.d( "Crypto", "Failed to verify signature on message. Exception: " + e.getMessage() );
		}
		if( verified )
			return plainText;
		else
			return "";
	}
	
	public RSAPublicKey GetPublicEncryptionKey() { return mLocalPubEncKey; }
	public RSAPublicKey GetPublicSigningKey() { return mLocalPubSignKey; }
}
