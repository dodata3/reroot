package com.Cyberpad.Reroot;

import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyCharacterMap;

public class Settings{
	public static String ip;
	public static KeyCharacterMap charmap;
	
	//saved preferences
	private static SharedPreferences prefs;
	private static final String PREFS_IPKEY = "remoteip";
	private static final String PREFS_TAPTOCLICK = "tapclick";
	private static final String PREFS_TAPTIME = "taptime";
	private static final String PREFS_SENSITIVITY = "sensitivity";
	private static final String PREFS_RECENT_IP_PREFIX = "recenthost";
	private static final String PREFS_SCROLL_SENSITIVITY = "scrollSensitivity";
	private static final String PREFS_SCROLL_INVERTED = "scrollInverted";
	private static final String PREFS_FILENAME = "Reroot";
	
	public static LinkedList<String> savedHosts;
	
	public static void init(Context con){
		if (prefs == null){
			prefs = con.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
			charmap = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
			savedHosts = new LinkedList<String>();
			
			
		}
		
	}
	
}

