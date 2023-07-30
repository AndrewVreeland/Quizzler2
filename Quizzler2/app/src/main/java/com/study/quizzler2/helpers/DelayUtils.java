package com.study.quizzler2.helpers;

import android.os.Handler;

public class DelayUtils {
    // Method to execute a delayed action
    public static void delayAction(Runnable action, long delayMillis) {
        new Handler().postDelayed(action, delayMillis);
    }
}