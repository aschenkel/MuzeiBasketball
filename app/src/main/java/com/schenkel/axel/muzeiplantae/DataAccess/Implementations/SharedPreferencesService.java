package com.schenkel.axel.muzeiplantae.DataAccess.Implementations;

import android.content.Context;
import android.content.SharedPreferences;

import com.schenkel.axel.muzeiplantae.DataAccess.Interfaces.LocalDBService;

/**
 * Created by axel on 08/05/17.
 */

public class SharedPreferencesService implements LocalDBService {
    private static final int START_ID_VALUE = 0;
    private static final int MODE = 0;
    private static final String SHARE_PREFERENCES_NAME = "SharePref";
    private static final String ID_TAG = "ID";
    private Context context;

    public SharedPreferencesService(Context context){
        this.context = context;
    }

    @Override
    public String GetNextID(){
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCES_NAME,MODE);
        SharedPreferences.Editor editor = prefs.edit();
        int CurrentID = ReadSharedPrefID();
        editor.putInt(ID_TAG, ++CurrentID);
        editor.apply();
        return String.valueOf(CurrentID);
    }

    @Override
    public int RestartID(){
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCES_NAME,MODE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ID_TAG, START_ID_VALUE);
        editor.apply();
        return START_ID_VALUE;
    }

    @Override
    public int ReadSharedPrefID(){
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCES_NAME,MODE);
        int ID = prefs.getInt(ID_TAG, START_ID_VALUE);
        return ID;
    }


}
