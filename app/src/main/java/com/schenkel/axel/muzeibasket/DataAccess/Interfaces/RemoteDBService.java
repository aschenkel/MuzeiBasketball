package com.schenkel.axel.muzeibasket.DataAccess.Interfaces;

import com.schenkel.axel.muzeibasket.NBAImage;

import rx.Observable;

/**
 * Created by axel on 20/05/17.
 */

public interface RemoteDBService {
    Observable<NBAImage> GetNextImage(String imageId);
}
