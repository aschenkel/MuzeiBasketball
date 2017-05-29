package com.schenkel.axel.muzeiplantae;

import com.google.firebase.database.DatabaseError;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;
import com.schenkel.axel.muzeiplantae.DataAccess.Interfaces.RemoteDBService;

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
    RemoteDBService remoteDBService;
    Image nextImage;


    @Before
    public void before() throws Exception {
        InitMocks();
        nextImage = new Image("url","name","descr");

    }

    private void InitMocks() {
        this.remoteDBService = Mockito.mock(RemoteDBService.class);
    }


    @Test
    public void GetNextImageFromFirebaseSuccess() throws Exception {
        when(remoteDBService.GetNextImage(imageIdTest)).thenReturn(Observable.just(nextImage));
        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        remoteDBService.GetNextImage(imageIdTest).subscribe(testSubscriber);
        testSubscriber.assertValue(nextImage);
    }

    @Test
    public void GetNextImageFromFirebaseError() throws Exception {
        RxFirebaseDataException ex = new RxFirebaseDataException(DatabaseError.fromException(new Exception()));
        when(remoteDBService.GetNextImage(imageIdTest)).thenReturn(Observable.error(ex));
        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        remoteDBService.GetNextImage(imageIdTest).subscribe(testSubscriber);
        testSubscriber.assertError(ex);
    }

    /*@Test
    public void CheckOnCompletedGetsCallOnSuccess() throws Exception {
        when(remoteDBService.GetNextImage(imageIdTest)).thenReturn(Observable.just(nextImage));
        doNothing().when(imageGenerator).OnCompleted(nextImage);
        imageGenerator.GetNextImageFromFirebase(imageIdTest,this.remoteDBService);
        verify(imageGenerator).OnCompleted(nextImage);
    }*/


}

