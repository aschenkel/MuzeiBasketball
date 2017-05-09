package com.example.axel.nbamuzei;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by axel on 08/05/17.
 */

public class SharedPreferencesManager {
    private static final int START_ID_VALUE = 0;
    private static final String START_URL_VALUE = "";
    private static final String ID_TAG = "ID";
    private static final String URL_TAG = "URL";
    public static int UpdateCurrentID(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        int CurrentID = ReadSharedPrefID(context);
        editor.putInt(ID_TAG, ++CurrentID);

        editor.apply();
        return CurrentID;
    }

    public static void UpdateCurrentURLForSaveToGallery(String url,Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(URL_TAG, url);
        editor.apply();

    }

    public static int ReestartID(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ID_TAG, START_ID_VALUE);
        editor.putString(URL_TAG, "");
        editor.apply();
        return START_ID_VALUE;
    }

    private static int ReadSharedPrefID(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int ID = prefs.getInt(ID_TAG, START_ID_VALUE);
        if(ID != START_ID_VALUE)
            return ID;
        else
            return START_ID_VALUE;
    }

    public static String ReadSharedPrefURL(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String url = prefs.getString(URL_TAG, START_URL_VALUE);
        if(!url.contentEquals(START_URL_VALUE))
            return url;
        else
            return START_URL_VALUE;
    }

}
