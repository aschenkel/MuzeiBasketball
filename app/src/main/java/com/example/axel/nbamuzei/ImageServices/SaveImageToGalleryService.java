package com.example.axel.nbamuzei.ImageServices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

import static com.example.axel.nbamuzei.ImageServices.CacheImageService.FILENAME;

/**
 * Created by axel on 11/05/17.
 */

public class SaveImageToGalleryService {
    public static void AddImageToGallery(Context context){
        Bitmap bitmap = GetBitmapFromPath();
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"NBA Muzei", "");
        OpenGallery(context);
    }

    private static Bitmap GetBitmapFromPath(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, FILENAME);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    }

    private static void OpenGallery(Context context){
        Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(galleryIntent);
    }
}
