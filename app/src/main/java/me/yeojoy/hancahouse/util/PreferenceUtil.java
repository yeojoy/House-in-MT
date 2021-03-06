package me.yeojoy.hancahouse.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    private static final String TAG = PreferenceUtil.class.getSimpleName();

    private static final String PREFERENCES_NAME = "hanca___";

    private static PreferenceUtil sInstance;

    private Context mContext;

    public static PreferenceUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferenceUtil.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceUtil(context);
                }
            }
        }
        return sInstance;
    }

    private PreferenceUtil(Context context) {
        mContext = context;
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public void clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences;
    }
}
