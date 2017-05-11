package com.example.axel.nbamuzei;

/**
 * Created by axel on 04/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.example.axel.nbamuzei.DataAccess.SharedPreferencesService;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.axel.nbamuzei.DataAccess.FirebaseService.GetNextImageFromFirebase;
import static com.example.axel.nbamuzei.ImageServices.SaveImageToGalleryService.AddImageToGallery;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {


    private static final int UPDATE_IMAGE_TIME_MILLIS =  10 * 1000;
    private static final int NO_INTERNET_TIME_MILLIS =  20 * 1000;
    private static final int SAVE_TO_GALLERY_COMMAND_ID =  12345;
    private static final String NAME = "NBAMuzei";
    Subscription subscription;

    public MuzeiImageGenerator() {
        super( NAME );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manageUserCommands();
    }

    private void manageUserCommands() {
        List<UserCommand> commands = new ArrayList<UserCommand>();
        commands.add(new UserCommand(SAVE_TO_GALLERY_COMMAND_ID, "Save to Gallery"));
        setUserCommands(commands);
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
        QueryFirebase();

    }

    private void QueryFirebase() {
        subscription = GetNextImageFromFirebase(getBaseContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> FirebaseError())
                .subscribe(this::OnCompleted);
    }


    private void OnCompleted(NBAImage image){
        Log.i("AHORA:", "completado");
        if (image != null) {
            setMuzeiImage(image);
           // CacheImage(image.url);                     //To be able to save it to Gallery later
        } else {
            SharedPreferencesService.ReestartID(getBaseContext());
            FirebaseError();
        }
    }
    private void setMuzeiImage(NBAImage img) {
        publishArtwork(new Artwork.Builder()
                .title(img.name)
                .byline(img.description)
                .imageUri(Uri.parse(img.url))
                .viewIntent( new Intent(Intent.ACTION_VIEW, Uri.parse( img.url ) ) )
                .build() );
    }

    private void FirebaseError() {
        Log.i("AHORA:", "ERROR");
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
                AddImageToGallery(getBaseContext());
        }

    }

    private boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
    }





}


