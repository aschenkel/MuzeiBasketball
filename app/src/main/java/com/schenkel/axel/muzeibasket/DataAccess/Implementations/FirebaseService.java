package com.schenkel.axel.muzeibasket.DataAccess.Implementations;

import com.schenkel.axel.muzeibasket.DataAccess.Interfaces.RemoteDBService;
import com.schenkel.axel.muzeibasket.Image;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import rx.Observable;

/**
 * Created by axel on 11/05/17.
 */

public class FirebaseService implements RemoteDBService {

    @Override
    public Observable<Image> GetNextImage(String imageId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference.child(imageId), Image.class);
    }

}
