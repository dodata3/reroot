package com.Cyberpad.Reroot;

public class Utility {
	
	// Helper function for converting string to hex
	// This method should be implemented Server side too!
	private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String ByteArrayToHexString( byte[] bytes )
	{
	    StringBuilder sb = new StringBuilder( bytes.length * 2 );
	    for( final byte b : bytes ) 
	    {
	        sb.append( hex[ ( b & 0xF0 ) >> 4 ] );
	        sb.append( hex[ b & 0x0F ] );
	    }
	    return sb.toString();
	}
	
	public static byte[] HexStringToByteArray( String s ) 
	{
		if( s.length() % 2 == 1 )
			s = "0".concat( s );
	    int len = s.length();
	    byte[] data = new byte[ len / 2 ];
	    for( int i = 0; i < len; i += 2 ) {
	        data[ i / 2 ] = ( byte )( ( Character.digit( s.charAt( i ), 16 ) << 4 )
	        		+ Character.digit( s.charAt( i + 1 ), 16 ) );
	    }
	    return data;
	}
	
	public static int HexStringToInteger( String string )
	{
		return Integer.parseInt( string, 16 );
	}

}
