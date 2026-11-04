package af.alireza.appsaz.Utilities;

import android.content.*;
import android.preference.*;

public class Prefs
{
	public static void putString(Context context, String key,String value){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
	}
	public static void putInt(Context context, String key,int value){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
	}
	public static void putBoolean(Context context, String key,boolean value){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
	}
	public static String getString(Context context, String key,String defaultValue){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefs.getString(key, defaultValue);
	}
	public static int getInt(Context context, String key,int defaultValue){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefs.getInt(key, defaultValue);
	}
	public static boolean getBoolean(Context context, String key,boolean defaultValue){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefs.getBoolean(key, defaultValue);
	}
}

