package com.schenkel.axel.muzeiplantae.ImageServices;

import android.content.Context;
import android.content.Intent;

/**
 * Created by axel on 11/05/17.
 */

public class ShareImageService {
    public void ShareItem(Context context){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, new BitmapUtils().getBitmapFromUri(context));
        Intent chooserIntent = Intent.createChooser(i, "Share images to..");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }
}
