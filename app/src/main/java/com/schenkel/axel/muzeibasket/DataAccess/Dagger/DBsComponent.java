package com.schenkel.axel.muzeibasket.DataAccess.Dagger;

import android.content.Context;

import com.schenkel.axel.muzeibasket.MuzeiImageGenerator;


import dagger.Component;

/**
 * Created by axel on 20/05/17.
 */
@Component(modules = DBsModule.class)
public interface DBsComponent {
    void inject(MuzeiImageGenerator muzeiImageGenerator);

    final class Initializer {
        public static DBsComponent init(Context context) {
            return DaggerDBsComponent.builder()
                    .dBsModule(new DBsModule(context))
                    .build();
        }
    }
}
