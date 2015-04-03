package com.ca.nolio.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Configuration {
	static final String USER_NAME = "USER_NAME";
	static final String AUTHENTICATE = "AUTHENTICATE";
	static final String SERVER = "SERVER";

	SharedPreferences preferences;

	public Configuration(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
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

	private void saveStringValue(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
