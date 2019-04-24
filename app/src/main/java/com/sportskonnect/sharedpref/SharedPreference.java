package com.sportskonnect.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sportskonnect.modal.TourDatum;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

public class SharedPreference {

    private final static String PREF_FILE = "rackonnect";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor ;




    public SharedPreference(Context context)
    {

        sharedPreferences = context.getSharedPreferences(PREF_FILE, 0);
        sharedPreferencesEditor = sharedPreferences.edit();


    }
    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }



    public static void saveArrayList(Context context,List<TourDatum> list, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static List<TourDatum> getArrayList(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        Gson gson = new Gson();
        String json = settings.getString(key, null);
        Type type = new TypeToken<List<TourDatum>>() {}.getType();
        return gson.fromJson(json, type);
    }


    /**
     * Set a integer shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }







    /**
     * Get a string shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }





    /**
     * Get a integer shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getSharedPreferenceInt(Context context, String key, int defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * Get a boolean shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

    public static void removeFromPrefs(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
        editor.clear();
    }


    public static void clearallPrefs(Context context){



        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
