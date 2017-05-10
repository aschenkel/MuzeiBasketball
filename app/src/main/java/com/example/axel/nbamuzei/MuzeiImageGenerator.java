package com.example.axel.nbamuzei;

/**
 * Created by axel on 04/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MuzeiImageGenerator extends RemoteMuzeiArtSource {


    private static final int UPDATE_IMAGE_TIME_MILLIS =  10 * 1000;
    private static final int NO_INTERNET_TIME_MILLIS =  10 * 1000;
    private static final int SAVE_TO_GALLERY_COMMAND_ID =  12345;
    private static final String NAME = "NBAMuzei";
    private DatabaseReference mDatabase;
    private String imageURL="";
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
            scheduleUpdateInMiliseconds(NO_INTERNET_TIME_MILLIS);
        }

    }

    private void scheduleUpdateInMiliseconds(long milliseconds) {
        scheduleUpdate(System.currentTimeMillis() + milliseconds);
    }



    private void setMuzeiImage(NBAImage img) {
        SharedPreferencesManager.UpdateCurrentURLForSaveToGallery(img.url,getApplicationContext());
        publishArtwork(new Artwork.Builder()
                .title(img.name)
                .byline(img.description)
                .imageUri(Uri.parse(img.url))
                .viewIntent( new Intent(Intent.ACTION_VIEW, Uri.parse( img.url ) ) )
                .build() );
    }




    @Override
    protected void onCustomCommand(int id) {
        super.onCustomCommand(id);
        switch (id) {
            case SAVE_TO_GALLERY_COMMAND_ID:
                //Save to gallery here
                    SaveImage();
        }

    }

    private void SaveImage() {
        String url= SharedPreferencesManager.ReadSharedPrefURL(getApplicationContext());
        SaveImageToGalleryService.SaveImage(url,getApplicationContext());
    }


    private void updateImage() {
       // FirebaseApp.initializeApp(getBaseContext());
        //Set next update
        scheduleUpdateInMiliseconds(UPDATE_IMAGE_TIME_MILLIS);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        GetNextIdFromFirebase(database);

    }

    private void GetNextIdFromFirebase(FirebaseDatabase database) {
        mDatabase = database.getReference();
        String imageId= String.valueOf(SharedPreferencesManager.UpdateCurrentID(getApplicationContext()));
        Log.i("Siguiente imagen: ", imageId);
        mDatabase.child(imageId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        NBAImage imagen = dataSnapshot.getValue(NBAImage.class);
                        //Si no hay más en la DB
                        if (imagen != null) {
                            setMuzeiImage(imagen);
                        }
                        else {
                            SharedPreferencesManager.ReestartID(getApplicationContext());
                            ReScheduleUpdate();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("", "getUser:onCancelled", databaseError.toException());
                        ReScheduleUpdate();

                    }

                    private void ReScheduleUpdate() {
                        unscheduleUpdate();
                        scheduleUpdateInMiliseconds(NO_INTERNET_TIME_MILLIS);
                    }


                });
    }

    private boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
    }


}


