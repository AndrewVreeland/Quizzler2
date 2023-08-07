package com.study.quizzler2.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String USER_PREFS = "USER_PREFS";
    private static final String LOGGED_IN_PREF = "LOGGED_IN_STATUS";

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
}