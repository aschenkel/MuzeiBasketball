package com.schenkel.axel.muzeiplantae.ImageServices;

import android.content.Context;
import android.content.Intent;

import com.schenkel.axel.muzeiplantae.R;

/**
 * Created by axel on 11/05/17.
 */

public class ShareImageService {
    public void ShareItem(Context context){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, new BitmapUtils().getBitmapUri(context));
        Intent chooserIntent = Intent.createChooser(i, context.getString(R.string.share_descrip));
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }
}
