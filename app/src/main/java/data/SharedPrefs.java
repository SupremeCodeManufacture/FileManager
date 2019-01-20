package data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static SharedPreferences preferences = App.getAppCtx().getSharedPreferences(GenericConstants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);


    public static void setSharedPrefsString(String key, String string) {
        preferences.edit().putString(key, string).apply();
    }

    public static String getSharedPrefsString(String key) {
        return preferences.getString(key, null);
    }

    public static String getSharedPrefsString(String key, String defVal) {
        return preferences.getString(key, defVal);
    }

    public static void deleteSharedPrefs(String key) {
        preferences.edit().remove(key).apply();
    }

    //-----------------------
    public static void setSharedPrefsInt(String key, int number) {
        preferences.edit().putInt(key, number).apply();
    }

    public static int getSharedPrefsInt(String key) {
        return preferences.getInt(key, 0);
    }

    //-----------------------
    public static void setSharedPrefsLong(String key, long number) {
        preferences.edit().putLong(key, number).apply();
    }

    public static long getSharedPrefsLong(String key) {
        return preferences.getLong(key, 0);
    }

    //-----------------------
    public static void setSharedPrefsBool(String key, boolean bool) {
        preferences.edit().putBoolean(key, bool).apply();
    }

    public static boolean getSharedPrefsBool(String key) {
        return preferences.getBoolean(key, false);
    }

}