package com.example.axel.nbamuzei.DataAccess;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by axel on 08/05/17.
 */

public class SharedPreferencesService {
    private static final int START_ID_VALUE = 0;
    private static final String ID_TAG = "ID";
    private Context context;

    public SharedPreferencesService(Context context){
        this.context = context;
    }

    public String UpdateCurrentID(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        int CurrentID = ReadSharedPrefID();
        editor.putInt(ID_TAG, ++CurrentID);
        editor.apply();
        return String.valueOf(CurrentID);
    }

    public int ReestartID(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ID_TAG, START_ID_VALUE);
        editor.apply();
        return START_ID_VALUE;
    }

    private int ReadSharedPrefID(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int ID = prefs.getInt(ID_TAG, START_ID_VALUE);
        if(ID != START_ID_VALUE)
            return ID;
        else
            return START_ID_VALUE;
    }


}
