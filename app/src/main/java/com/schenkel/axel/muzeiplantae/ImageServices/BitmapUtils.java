package com.schenkel.axel.muzeiplantae.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by axel on 15/05/17.
 */

public class BitmapUtils {

    final String FILENAME = "BASKET_MUZEI.png";

    public boolean cacheBitmapToUri(Bitmap bmp, Context context) {
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Uri getBitmapFromUri(Context context){
        File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
        return Uri.fromFile(file);
    }
}
