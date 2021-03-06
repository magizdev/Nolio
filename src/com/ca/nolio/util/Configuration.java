package com.ca.nolio.util;

import java.util.HashMap;

import com.ca.nolio.ApplicationSelectListener;
import com.ca.nolio.OnApplicationChangedListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Configuration {
	static final String USER_NAME = "USER_NAME";
	static final String AUTHENTICATE = "AUTHENTICATE";
	static final String SERVER = "SERVER";
	static final String APPLICATION = "APPLICATION";
	static final String APPLICATION_ID = "APPLICATION_ID";

	SharedPreferences preferences;
	private OnApplicationChangedListener applicationChangedListener;
	private static HashMap<Context, Configuration> pool = new HashMap<Context, Configuration>();
	
	public static Configuration getConfiguration(Context context){
		if(!pool.containsKey(context)){
			pool.put(context, new Configuration(context));
		}
		
		return pool.get(context);
	}

	private Configuration(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void setApplicationSelectListener(OnApplicationChangedListener applicationChangedListener){
		this.applicationChangedListener = applicationChangedListener;
	}

	public String getUsername() {
		return preferences.getString(USER_NAME, "");
	}

	public void setUsername(String username) {
		saveStringValue(USER_NAME, username);
	}

	public String getAuthenticate() {
		return preferences.getString(AUTHENTICATE, "");
	}

	public void setAuthenticate(String authenticate) {
		saveStringValue(AUTHENTICATE, authenticate);
	}

	public String getServer() {
		return preferences.getString(SERVER, "");
	}

	public void setServer(String server) {
		saveStringValue(SERVER, server);
	}

	public String getApplicationName() {
		return preferences.getString(APPLICATION, "");
	}

	public Long getApplicationId() {
		return preferences.getLong(APPLICATION_ID, 0);
	}

	public void setApplication(String name, Long id) {
		saveStringValue(APPLICATION, name);
		saveLongValue(APPLICATION_ID, id);
		applicationChangedListener.ChangeTo(id);
	}

	private void saveStringValue(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void saveLongValue(String key, Long value) {
		Editor editor = preferences.edit();
		editor.putLong(APPLICATION_ID, value);
		editor.commit();
	}
}
