package com.schenkel.axel.muzeibasket.Application;

import android.app.Application;

import com.schenkel.axel.muzeibasket.DataAccess.Dagger.DBsComponent;
import com.schenkel.axel.muzeibasket.MuzeiImageGenerator;

/**
 * Created by axel on 20/05/17.
 */

public class MyApplication extends Application {

    public static DBsComponent injectLocalAndRemoteDB(MuzeiImageGenerator muzeiImageGenerator){
        return DBsComponent.Initializer.init(muzeiImageGenerator);
    }

}
