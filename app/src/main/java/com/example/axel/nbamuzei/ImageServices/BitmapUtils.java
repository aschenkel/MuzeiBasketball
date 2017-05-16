package com.example.axel.nbamuzei.ImageServices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.axel.nbamuzei.ImageServices.CacheImageService.FILENAME;

/**
 * Created by axel on 15/05/17.
 */

public class BitmapUtils {

    public static String getPath(){
        return Environment.getExternalStorageDirectory().toString();
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

    public Bitmap GetBitmapFromPath(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File file = new File(path, FILENAME);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    }
}
