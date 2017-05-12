package com.example.axel.nbamuzei.DataAccess;

import android.content.Context;

import com.example.axel.nbamuzei.NBAImage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import rx.Observable;

/**
 * Created by axel on 11/05/17.
 */

public class FirebaseService {

    public static Observable<NBAImage> GetNextImageFromFirebase (Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        String imageId = String.valueOf(SharedPreferencesService.UpdateCurrentID(context));
        return RxFirebaseDatabase.observeSingleValueEvent(mDatabase.child(imageId), NBAImage.class);
    }

}
