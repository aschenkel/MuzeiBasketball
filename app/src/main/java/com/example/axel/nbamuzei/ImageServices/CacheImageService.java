package com.example.axel.nbamuzei.ImageServices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static rx.subscriptions.Subscriptions.empty;

/**
 * Created by axel on 09/05/17.
 */

public class CacheImageService {

    static final String FILENAME = "NBA_MUZEI_IMAGE";
    static Subscription subscription;
    public static void CacheImage(String URL){
        subscription = empty();
        CacheImageFromURL(URL);
    }

    private static void CacheImageFromURL(String URL){
        subscription = ConvertURLtoBitmap(URL)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext((bitmap) ->SaveImageToStorage(bitmap)) //THEN
                .doOnError(throwable -> LogError())
                .subscribe();
    }

    private static Observable<Bitmap> ConvertURLtoBitmap (String URL){
        return Observable.just(ConvertToBitmap(URL));
    }

    private static Bitmap ConvertToBitmap(String urlAddress)
    {
        try {
            URL url = new URL(urlAddress);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private static void SaveImageToStorage(Bitmap bitmap)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FILENAME);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FinishSubscription();
    }

    private static void LogError()
    {
        //Log error to Firebase CrashReport
        FinishSubscription();
    }

    private static void FinishSubscription() {
        subscription.unsubscribe();
    }




}
