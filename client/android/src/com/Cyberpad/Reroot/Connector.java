package com.Cyberpad.Reroot;

import java.net.SocketException;
import java.util.Date;

import android.content.SharedPreferences;

import com.illposed.osc.*;

public class Connector {
	private OSCPortOut mSender;
	private OSCPortIn mReceiver;
	private Crypto mCrypto;
	
	// Constructor
	Connector( SharedPreferences preferences )
	{	
		// Upon initialization, we have not authenticated yet
		mCrypto = new Crypto( preferences );
		
		// Create a listener port which will listen for a server handshake
		try {
			mReceiver = new OSCPortIn( OSCPort.defaultSCOSCPort() );
		} catch ( SocketException e ) {
			e.printStackTrace();
		}
		
		// Create a new HandshakeListener
		HandshakeListener listener = new HandshakeListener( this );
		mReceiver.addListener( "/handshake_server", listener );
		mReceiver.startListening();
	}
	
	public void Authenticate( String publicEncKeyMod, String publicEncKeyExp, String publicSignKeyMod, String publicSignKeyExp )
	{
		mCrypto.SetRemoteKeys( publicEncKeyMod, publicEncKeyExp, publicSignKeyMod, publicSignKeyExp );
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
			String[] args = ( String[] ) message.getArguments();
			mConnector.Authenticate( args[0], args[1], args[2], args[3] );
		}
	}
}
