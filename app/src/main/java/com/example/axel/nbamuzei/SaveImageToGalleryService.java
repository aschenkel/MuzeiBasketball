package com.example.axel.nbamuzei;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

/**
 * Created by axel on 09/05/17.
 */

public class SaveImageToGalleryService {

    private static Context context;
    public static void SaveImage(String URL,Context contextParam){
        //ask if there is an internet connection
        context = contextParam;
        Toast.makeText(context,
                "Saving Image", Toast.LENGTH_SHORT).show();
        new saveImageToGallery().execute(URL);

    }

    private static class saveImageToGallery extends AsyncTask<String, Void, Bitmap> {



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlAddress = params[0];
            return ConvertURLtoBitmap(urlAddress);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", "");
            Toast.makeText(context,
                    "Image Saved", Toast.LENGTH_SHORT).show();
        }



        private Bitmap ConvertURLtoBitmap(String urlAddress)
        {
            try {
                URL url = new URL(urlAddress);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
                return null;
            }
        }

    }

}
