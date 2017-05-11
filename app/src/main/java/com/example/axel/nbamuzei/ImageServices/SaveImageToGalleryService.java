package com.example.axel.nbamuzei.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static com.example.axel.nbamuzei.Utils.FILENAME;

/**
 * Created by axel on 11/05/17.
 */

public class SaveImageToGalleryService {
    public static void AddImageToGallery(Context context){
        Bitmap bitmap = GetBitmapFromPath();
        Log.e("Bitmap",bitmap.toString());
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", "");
        Toast.makeText(context,
                "Image Saved", Toast.LENGTH_SHORT).show();
    }

    private static Bitmap GetBitmapFromPath(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, FILENAME);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    }
}
