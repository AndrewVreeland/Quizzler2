package com.study.quizzler2.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtility {

    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private HandlerUtility() {
        // private constructor to prevent instantiation
    }

    /**
     * Run a task on the main UI thread.
     *
     * @param runnable The task to run.
     */
    public static void runOnMainThread(Runnable runnable) {
        MAIN_THREAD_HANDLER.post(runnable);
    }

    /**
     * Run a task on the main UI thread after a delay.
     *
     * @param runnable The task to run.
     * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
     */
    public static void runOnMainThreadDelayed(Runnable runnable, long delayMillis) {
        MAIN_THREAD_HANDLER.postDelayed(runnable, delayMillis);
    }

    /**
     * Removes callbacks and messages from the main thread handler.
     *
     * @param runnable The task to remove from the message queue.
     */
    public static void removeCallbacks(Runnable runnable) {
        MAIN_THREAD_HANDLER.removeCallbacks(runnable);
    }


}