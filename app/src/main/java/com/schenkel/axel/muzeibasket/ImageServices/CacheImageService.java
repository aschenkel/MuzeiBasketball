package com.schenkel.axel.muzeibasket.ImageServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import static com.schenkel.axel.muzeibasket.ImageServices.BitmapUtils.getPath;

/**
 * Created by axel on 09/05/17.
 */

 public class CacheImageService extends AsyncTask<String, Void, Bitmap> {

        public static final String FILENAME = "NBA_MUZEI_IMAGE";

        BitmapUtils bitmapUtils;
        Context context;

        public CacheImageService(Context context){
            this.context = context;
            this.bitmapUtils = new BitmapUtils();
        }

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
            bitmapUtils.SaveBitmapToPath(bitmap,getPath());
        }



    }





