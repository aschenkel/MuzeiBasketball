package com.example.axel.nbamuzei;

/**
 * Created by axel on 04/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.axel.nbamuzei.DataAccess.FirebaseService;
import com.example.axel.nbamuzei.DataAccess.SharedPreferencesService;
import com.example.axel.nbamuzei.ImageServices.CacheImageService;
import com.example.axel.nbamuzei.Permissions.AskForPermissionsActivity;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {


    private static final int UPDATE_IMAGE_TIME_MILLIS =  24 * 60 * 60 * 1000; //every day
    private static final int NO_INTERNET_TIME_MILLIS =  5 * 60 * 1000; //every 5 minutes
    private static final int SAVE_TO_GALLERY_COMMAND_ID =  12345;
    private static final String NAME = "NBAMuzei";
    Subscription subscription;
    CacheImageService cacheImageService;
    SharedPreferencesService sharedPreferencesService;
    FirebaseService firebaseService;

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
        sharedPreferencesService = new SharedPreferencesService(getApplicationContext());
        firebaseService = new FirebaseService();
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
        String nextId = sharedPreferencesService.GetNextID();
        GetNextImageFromFirebase(nextId);
    }

    public void GetNextImageFromFirebase(String imageId) {
        subscription = firebaseService.GetNextImage(imageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> FirebaseError())
                .subscribe(this::OnCompleted);
    }

    public void OnCompleted(NBAImage image){
        if (image != null) {
            setMuzeiImage(image);
            CacheImage(image);
        } else {
            sharedPreferencesService.RestartID();
            FirebaseError();
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

    private void FirebaseError() {
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
                LaunchPermissionActivity();
                break;
        }

    }

    private void LaunchPermissionActivity() {
        Intent permissionIntent = new Intent(this, AskForPermissionsActivity.class);
        permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(permissionIntent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


