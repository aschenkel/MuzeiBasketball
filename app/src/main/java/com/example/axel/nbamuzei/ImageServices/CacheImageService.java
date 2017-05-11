package com.example.axel.nbamuzei.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by axel on 09/05/17.
 */

public class CacheImageService {

    static final String FILENAME = "NBA_MUZEI_IMAGE";
    static Context context;
    public static void CacheImageFromURL(String URL,Context contextParam){
        context = contextParam;
        new CacheImage().execute(URL);

    }

    private static class CacheImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlAddress = params[0];
            return ConvertURLtoBitmap(urlAddress);

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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            SaveImageToStorage(bitmap);
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

        }

    }




}
