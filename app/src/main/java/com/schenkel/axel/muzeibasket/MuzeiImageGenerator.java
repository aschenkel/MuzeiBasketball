package com.schenkel.axel.muzeibasket;

/**
 * Created by axel on 04/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.schenkel.axel.muzeibasket.Application.MyApplication;
import com.schenkel.axel.muzeibasket.DataAccess.Interfaces.LocalDBService;
import com.schenkel.axel.muzeibasket.DataAccess.Interfaces.RemoteDBService;
import com.schenkel.axel.muzeibasket.ImageServices.BitmapUtils;
import com.schenkel.axel.muzeibasket.ImageServices.CacheImageService;
import com.schenkel.axel.muzeibasket.Permissions.AskForPermissionsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.schenkel.axel.muzeibasket.ImageServices.BitmapUtils.getPath;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {

    private static final int UPDATE_IMAGE_TIME_MILLIS =  10 * 1000; //every day
    private static final int NO_INTERNET_TIME_MILLIS =  5 * 60 * 1000; //every 5 minutes
    private static final int SAVE_TO_GALLERY_COMMAND_ID =  12345;
    private static final String NAME = "MuzeiBasketball";
    Subscription subscription;
    CacheImageService cacheImageService;
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
        commands.add(new UserCommand(SAVE_TO_GALLERY_COMMAND_ID, getString(R.string.save_to_gallery_button)));
        setUserCommands(commands);
    }

    private void InstanceServices(){
        cacheImageService = new CacheImageService(getApplicationContext());
        MyApplication.getInstance().injectLocalAndRemoteDB(this).inject(this);
    }


    @Override
    protected void onTryUpdate( int reason ) throws RetryException {
        if(isNetworkAvailable()) {
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

    public void OnCompleted(NBAImage image){
        if (image != null) {
            CacheImage(image);
            setMuzeiImage(image);
        } else {
            localDBService.RestartID();
            RemoteDBError();
        }
    }

    private void CacheImage(NBAImage image) {
        cacheImageService.execute(image.getUrl());                     //To be able to save it to Gallery later
    }

    private void setMuzeiImage(NBAImage img) {
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
            case SAVE_TO_GALLERY_COMMAND_ID:
                ShareItem();
                break;
        }

    }

    private void ShareItem(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, new BitmapUtils().getLocalBitmapUri(getApplicationContext()));
        Intent chooserIntent = Intent.createChooser(i, "Share images to..");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooserIntent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


