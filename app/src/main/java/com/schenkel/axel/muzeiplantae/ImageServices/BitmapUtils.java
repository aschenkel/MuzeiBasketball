package com.schenkel.axel.muzeiplantae.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by axel on 15/05/17.
 */

public class BitmapUtils {

    final String FILENAME = "PLANTAE_MUZEI.png";

    public boolean cacheBitmap(Bitmap bmp, Context context) {
        try {
            FileOutputStream out = new FileOutputStream(getFile(context));
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Uri getBitmapUri(Context context){
        return Uri.fromFile(getFile(context));
    }

    @NonNull
    private File getFile(Context context) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
    }
}
