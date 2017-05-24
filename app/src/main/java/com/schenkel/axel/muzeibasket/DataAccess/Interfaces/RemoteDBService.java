package com.schenkel.axel.muzeibasket.DataAccess.Interfaces;

import com.schenkel.axel.muzeibasket.Image;

import rx.Observable;

/**
 * Created by axel on 20/05/17.
 */

public interface RemoteDBService {
    Observable<Image> GetNextImage(String imageId);
}
