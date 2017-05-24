package com.schenkel.axel.muzeibasket.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by axel on 15/05/17.
 */

public class BitmapUtils {

    final String FILENAME = "BASKET_MUZEI";

    public static String getPath(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
    }

    public boolean SaveBitmapToPath(Bitmap bitmap,String path)
    {
        FileOutputStream out = null;
        try {
            File file = new File(path, FILENAME);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean cacheBitmapFromUri(Bitmap bmp, Context context) {
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Uri getLocalBitmapUri(Context context){
        File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image.png");
        return Uri.fromFile(file);
    }


    public Bitmap GetBitmapFromPath(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File file = new File(path, FILENAME);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    }
}
