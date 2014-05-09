package com.example.WordsLearner.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesManager {

    private final static String KEY_DB_TIMESTAMP = "db_timestamp";

    private static SharedPreferences preferences;

	public PreferencesManager(Context ctx) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /*************************************************************/

    public long getDbTimestamp() {
        return getLong(KEY_DB_TIMESTAMP, 0);
    }

    public void setDbTimestamp(long dbTimestemp) {
        setLong(KEY_DB_TIMESTAMP, dbTimestemp);
    }

    /*************************************************************/

    private String getString(String key, String defaultValue) {
		return preferences.getString(key, defaultValue);
	}

    private int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }


    private long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

	private void setString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

    private void setInt(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void setLong(String key, long value) {
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private void setBoolean(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
