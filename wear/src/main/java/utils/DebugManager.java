package utils;

import android.util.Log;

import tarwinderjosan.com.drunkoclock.BuildConfig;

/**
 * Created by tarwinderjosan on 8/20/15.
 */
public class DebugManager {
    public static void print(String tag, String s) {
        if(BuildConfig.DEBUG) {
            Log.d(tag, s);

        }
    }
}
