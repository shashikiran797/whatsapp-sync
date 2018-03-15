package com.boatman.sync;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shashikiranms on 14/03/18
 */

public class LocalStorageService {

    public static String getString(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("com.boatman.sync", Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "https://hookb.in/ZrRRzr1o");
    }


    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("com.boatman.sync", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
