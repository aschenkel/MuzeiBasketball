package com.schenkel.axel.muzeiplantae.DataAccess.Interfaces;

import com.schenkel.axel.muzeiplantae.Image;

import rx.Observable;

/**
 * Created by axel on 20/05/17.
 */

public interface RemoteDBService {
    Observable<Image> GetNextImage(String imageId);
}
