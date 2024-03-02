package com.example.hw1.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hw1.Models.LeaderBoardList;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private static volatile SharedPreferencesManager instance = null;
    private static final String DB_FILE = "DB_FILE";
    private final SharedPreferences sharedPref;

    private SharedPreferencesManager(Context context) {
        this.sharedPref = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        synchronized (SharedPreferencesManager.class) {
            if (instance == null) {
                instance = new SharedPreferencesManager(context);
            }
        }
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {

        return sharedPref.getInt(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }
    public void putLeaderboardList(String key, LeaderBoardList leaderboardList) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, new Gson().toJson(leaderboardList));
        editor.apply();
    }

    public LeaderBoardList getLeaderboardList(String key) {
        String leaderboardJson = sharedPref.getString(key, "LEADERBOARDLIST");
        if (!leaderboardJson.isEmpty()) {
            return new Gson().fromJson(leaderboardJson, LeaderBoardList.class);
        }
        return new LeaderBoardList();
    }

}
