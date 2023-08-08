package com.study.quizzler2.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String USER_PREFS = "USER_PREFS";
    private static final String LOGGED_IN_PREF = "LOGGED_IN_STATUS";
    private static final String USERNAME_PREF = "LOGGED_IN_USERNAME";

    private final SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN_PREF, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(LOGGED_IN_PREF, loggedIn).apply();
    }

    // New method to save the username of the logged-in user
    public void saveUsername(String username) {
        sharedPreferences.edit().putString(USERNAME_PREF, username).apply();
    }

    // New method to retrieve the username of the logged-in user
    public String getUsername() {
        return sharedPreferences.getString(USERNAME_PREF, "");
    }
}