package com.schenkel.axel.muzeibasket;

import com.schenkel.axel.muzeibasket.DataAccess.FirebaseService;
import com.google.firebase.database.DatabaseError;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

/**
 * Created by axel on 13/05/17.
 */

public class UpdateImageTest {
    String imageIdTest="3";
    @Mock
    FirebaseService firebaseService;
    NBAImage nextImage;


    @Before
    public void before() throws Exception {
        InitMocks();
        nextImage = new NBAImage("url","name","descr");

    }

    private void InitMocks() {
        this.firebaseService = Mockito.mock(FirebaseService.class);
    }


    @Test
    public void GetNextImageFromFirebaseSuccess() throws Exception {
        when(firebaseService.GetNextImage(imageIdTest)).thenReturn(Observable.just(nextImage));
        TestSubscriber<NBAImage> testSubscriber = new TestSubscriber<>();
        firebaseService.GetNextImage(imageIdTest).subscribe(testSubscriber);
        testSubscriber.assertValue(nextImage);
    }

    @Test
    public void GetNextImageFromFirebaseError() throws Exception {
        RxFirebaseDataException ex = new RxFirebaseDataException(DatabaseError.fromException(new Exception()));
        when(firebaseService.GetNextImage(imageIdTest)).thenReturn(Observable.error(ex));
        TestSubscriber<NBAImage> testSubscriber = new TestSubscriber<>();
        firebaseService.GetNextImage(imageIdTest).subscribe(testSubscriber);
        testSubscriber.assertError(ex);
    }

    /*@Test
    public void CheckOnCompletedGetsCallOnSuccess() throws Exception {
        when(firebaseService.GetNextImage(imageIdTest)).thenReturn(Observable.just(nextImage));
        doNothing().when(imageGenerator).OnCompleted(nextImage);
        imageGenerator.GetNextImageFromFirebase(imageIdTest,this.firebaseService);
        verify(imageGenerator).OnCompleted(nextImage);
    }*/


}

