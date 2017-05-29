package com.schenkel.axel.muzeiplantae.Application;

import android.app.Application;

import com.schenkel.axel.muzeiplantae.DataAccess.Dagger.DBsComponent;
import com.schenkel.axel.muzeiplantae.MuzeiImageGenerator;

/**
 * Created by axel on 20/05/17.
 */

public class MyApplication extends Application {


    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public DBsComponent injectLocalAndRemoteDB(MuzeiImageGenerator muzeiImageGenerator){
        return DBsComponent.Initializer.init(muzeiImageGenerator);
    }

}
