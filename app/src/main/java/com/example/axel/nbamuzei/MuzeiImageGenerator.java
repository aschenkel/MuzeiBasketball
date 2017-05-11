package com.example.axel.nbamuzei;

/**
 * Created by axel on 04/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

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
import static com.example.axel.nbamuzei.ImageServices.CacheImageService.CacheImageFromURL;
import static com.example.axel.nbamuzei.ImageServices.SaveImageToGalleryService.AddImageToGallery;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {


    private static final int UPDATE_IMAGE_TIME_MILLIS =  20 * 1000;
    private static final int NO_INTERNET_TIME_MILLIS =  15 * 1000;
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
        commands.add(new UserCommand(SAVE_TO_GALLERY_COMMAND_ID, getString(R.string.save_to_gallery_button)));
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
        if (image != null) {
            setMuzeiImage(image);
            CacheImageFromURL(image.getUrl(),getApplicationContext());                     //To be able to save it to Gallery later
        } else {
            SharedPreferencesService.ReestartID(getBaseContext());
            FirebaseError();
        }
    }
    private void setMuzeiImage(NBAImage img) {
        publishArtwork(new Artwork.Builder()
                .title(img.getName())
                .byline(img.getDescription())
                .imageUri(Uri.parse(img.getUrl()))
                .viewIntent( new Intent(Intent.ACTION_VIEW, Uri.parse( img.getUrl() ) ) )
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
                ShowMessage(getString(R.string.saving_to_gallery_message));
                AddImageToGallery(getBaseContext());
        }

    }

    private void ShowMessage(String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(MuzeiImageGenerator.this.getApplicationContext(),message,Toast.LENGTH_LONG)
                .show());


    }

    private boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
    }





}


