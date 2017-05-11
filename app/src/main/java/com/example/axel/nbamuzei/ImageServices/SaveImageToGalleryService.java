package com.example.axel.nbamuzei.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by axel on 11/05/17.
 */

public class SaveImageToGalleryService {
    public static void AddImageToGallery(Context context){
        //Get bitmap from path
        Bitmap bitmap = null;
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", "");
        Toast.makeText(context,
                "Image Saved", Toast.LENGTH_SHORT).show();
    }
}
