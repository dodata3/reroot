package com.Cyberpad.Reroot;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.illposed.osc.*;

public class Connector {
	private OSCPortOut mSender;
	private OSCPortIn mReceiver;
	private Crypto mCrypto;
	Context mContext;
	
	private static final int REROOT_SERVER_PORT = 57110;
	private static final int REROOT_CLIENT_PORT = 57220;
	
	public static final String AUTH_INTENT = "com.Cyberpad.Reroot.AUTHENTICATED";
	
	static private Connector mInstance;
	
	public static synchronized Connector getInstance( Context c )
	{
		if( mInstance == null )
			mInstance = new Connector( c );
		return mInstance;
	}
	
	// Constructor
	private Connector( Context c )
	{	
		// Upon initialization, we have not authenticated yet
		mContext = c;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( c );
		mCrypto = new Crypto( preferences );
		
		// Create a listener port which will listen for a server handshake
		try {
			mReceiver = new OSCPortIn( REROOT_CLIENT_PORT );
		} catch ( SocketException e ) {
			e.printStackTrace();
		}
		
		// Create a new HandshakeListener
		HandshakeListener listener = new HandshakeListener( this );
		mReceiver.addListener( "/handshake_server", listener );
		mReceiver.startListening();
	}
	
	public void ConnectToServer( InetAddress address, int connectionKey )
	{
		// Close the connection to any previous server and open a new server port
		if( mSender != null ) mSender.close();
		try {
			mSender = new OSCPortOut( address, REROOT_SERVER_PORT );
		} catch( SocketException e ) {
			Log.d( "Connector", "Unable to create outward OSC port. Exception: " + e.getMessage() );
		}
		
		// Send a handshake message to the server
		// Consider converting these to hex strings
		Object[] args = new Object[5];
		args[0] = Utility.ByteArrayToHexString( mCrypto.GetPublicEncryptionKey().getModulus().toByteArray() );
		args[1] = Utility.ByteArrayToHexString( mCrypto.GetPublicEncryptionKey().getPublicExponent().toByteArray() );
		args[2] = Utility.ByteArrayToHexString( mCrypto.GetPublicSigningKey().getModulus().toByteArray() );
		args[3] = Utility.ByteArrayToHexString( mCrypto.GetPublicSigningKey().getPublicExponent().toByteArray() );
		args[4] = connectionKey;
		OSCMessage msg = new OSCMessage( "/handshake_client", args );
		SendMessage( msg );
	}
	
	public void SendControlMessage( ControlMessage message )
	{
		// Encrypt and sign the control message
		EncryptedMessage emsg = mCrypto.Encrypt( message.FormatMessage() );
		
		if( emsg.mSignature != "" )
		{
			// Send the control message
			Object[] args = new Object[2];
			args[0] = emsg.mCipherText;
			args[1] = emsg.mSignature;
			Log.d( "Connector", "Cipher = " + emsg.mCipherText );
			Log.d( "Connector", "Signature = " + emsg.mSignature );
			OSCMessage msg = new OSCMessage( "/control", args );
			SendMessage( msg );
		}
		else
			Log.d( "Connector", "Failed to send control message." );
	}
	
	private void Authenticate( String publicEncKeyMod, String publicEncKeyExp, String publicSignKeyMod, String publicSignKeyExp )
	{
		// Set the remote public keys
		mCrypto.SetRemoteKeys( publicEncKeyMod, publicEncKeyExp, publicSignKeyMod, publicSignKeyExp );
		
		// Create a new "authenticated intent" which should be broadcast to the system
		Intent i = new Intent();
		i.setAction( AUTH_INTENT );
		mContext.sendBroadcast(i);
	}
	
	// Simple handshake listener class which shouldn't be needed outside of connector
	private class HandshakeListener implements OSCListener
	{
		Connector mConnector;
		HandshakeListener( Connector connector ) { mConnector = connector; }
		
		// Possible security risk.  Anyone can send a handshake message with their own public key.
		// Server would just not interpret these messages and the user would not see input feedback from the server.
		public void acceptMessage( Date time, OSCMessage message ) 
		{
			// Parse message, pull keys, call *authenticate* method
			Object[] args = message.getArguments();
			if( args.length == 4 )
			{
				String encMod = ( String )args[0];
				String encExp = ( String )args[1];
				String signMod = ( String )args[2];
				String signExp = ( String )args[3];
				mConnector.Authenticate( encMod, encExp, signMod, signExp );
				Log.d( "Connector", "Recieved server handshake, keys exchanged." );
			}
		}
	}
	
	// Send a message over the outgoing server port if possible.
	private void SendMessage( OSCMessage msg )
	{
		try { 
			if( mSender != null ) mSender.send( msg ); 
		}
		catch( Exception e ) {
			Log.d( "Connector", "Unable to send message to server. Exception: " + e.getMessage() ); 
		}
	}
}
