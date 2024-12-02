package com.example.flappybirdclone.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static PreferenceManager instance;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "game_prefs";

    private PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (instance == null) instance = new PreferenceManager(context.getApplicationContext());
        return instance;
    }

    public int getVolume() {
        return prefs.getInt("volume", 50);
    }

    public void setVolume(int volume) {
        prefs.edit().putInt("volume", volume).apply();
    }

    public boolean isVibrationEnabled() {
        return prefs.getBoolean("vibration", true);
    }

    public void setVibrationEnabled(boolean enabled) {
        prefs.edit().putBoolean("vibration", enabled).apply();
    }

    public String getBirdSkinPath() {
        return prefs.getString("skinPath",null);
    }
    public void setBirdSkinPath(String path) {
        prefs.edit().putString("skinPath",path).apply();
    }

}
