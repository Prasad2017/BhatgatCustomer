package com.bachatgatapp.Extra;

import android.content.Context;
import android.content.SharedPreferences;



public class Common {
   // public static final String SHARED_PREF = "userData";
    public static final String SHARED_PREF = "session";

    public static void saveUserData(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSavedUserData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getString(key, "");

    }
}
