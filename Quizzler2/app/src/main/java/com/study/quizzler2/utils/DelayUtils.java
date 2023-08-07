package com.study.quizzler2.utils;

import android.os.Handler;

public class DelayUtils {
    // Method to execute a delayed action
    public static void delayAction(Runnable action, long delayMillis) {
        new Handler().postDelayed(action, delayMillis);
    }
}