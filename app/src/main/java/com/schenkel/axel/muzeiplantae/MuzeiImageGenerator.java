package com.schenkel.axel.muzeiplantae;

/**
 * Created by axel on 04/05/17.
 */

import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.schenkel.axel.muzeiplantae.Application.MyApplication;
import com.schenkel.axel.muzeiplantae.DataAccess.Interfaces.LocalDBService;
import com.schenkel.axel.muzeiplantae.DataAccess.Interfaces.RemoteDBService;
import com.schenkel.axel.muzeiplantae.ImageServices.CacheImageService;
import com.schenkel.axel.muzeiplantae.ImageServices.ShareImageService;
import com.schenkel.axel.muzeiplantae.NetworkUtils.Connectivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {

    private static final int UPDATE_IMAGE_TIME_MILLIS = 24* 60 * 60 * 1000; //every day
    private static final int NO_INTERNET_TIME_MILLIS =  5 * 60 * 1000; //every 5 minutes
    private static final int SHARE_COMMAND_ID =  12345;
    private static final String NAME = "MuzeiPlantae";
    Subscription subscription;
    CacheImageService cacheImageService;
    Connectivity connectivity;
    @Inject
    LocalDBService localDBService;
    @Inject
    RemoteDBService remoteDBService;


    public MuzeiImageGenerator() {
        super( NAME );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manageUserCommands();
        InstanceServices();
    }

    private void manageUserCommands() {
        List<UserCommand> commands = new ArrayList<UserCommand>();
        commands.add(new UserCommand(SHARE_COMMAND_ID, getString(R.string.share)));
        setUserCommands(commands);
    }

    private void InstanceServices(){
        connectivity = new Connectivity();
        cacheImageService = new CacheImageService(getApplicationContext());
        MyApplication.getInstance().injectLocalAndRemoteDB(this).inject(this);
    }


    @Override
    protected void onTryUpdate( int reason ) throws RetryException {
        if(connectivity.isConnected(getApplicationContext())) {
            updateImage();
        }
        else{
            ReScheduleUpdate(NO_INTERNET_TIME_MILLIS);
        }

    }


    private void updateImage() {
        ReScheduleUpdate(UPDATE_IMAGE_TIME_MILLIS);      //Done before updating to avoid some GooglePlayServices issues on some devices
        String nextId = localDBService.GetNextID();
        GetNextImageFromFirebase(nextId);
    }

    public void GetNextImageFromFirebase(String imageId) {
        subscription = remoteDBService.GetNextImage(imageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> RemoteDBError())
                .subscribe(this::OnCompleted);
    }

    public void OnCompleted(Image image){
        if (image != null) {
            CacheImage(image);
            setMuzeiImage(image);
        } else {
            localDBService.RestartID();
            RemoteDBError();
        }
    }

    private void CacheImage(Image image) {
        cacheImageService.execute(image.getUrl());                     //To be able to save it to Gallery later
    }

    private void setMuzeiImage(Image img) {
        publishArtwork(new Artwork.Builder()
                .title(img.getName())
                .byline(img.getDescription())
                .imageUri(Uri.parse(img.getUrl()))
                .build() );
    }

    private void RemoteDBError() {
        unscheduleUpdate();
        ReScheduleUpdate(NO_INTERNET_TIME_MILLIS);
    }

    private void ReScheduleUpdate(long milliseconds) {
        scheduleUpdate(System.currentTimeMillis() + milliseconds);
    }


    @Override
    protected void onCustomCommand(int id) {
        super.onCustomCommand(id);
        switch (id) {
            case SHARE_COMMAND_ID:
                new ShareImageService().ShareItem(getApplicationContext());
                break;
        }

    }
}


