package com.schenkel.axel.muzeibasket.Permissions;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.schenkel.axel.muzeibasket.ImageServices.BitmapUtils;
import com.schenkel.axel.muzeibasket.ImageServices.SaveImageToGalleryService;
import com.schenkel.axel.muzeibasket.R;

import static com.schenkel.axel.muzeibasket.ImageServices.BitmapUtils.getPath;

/**
 * Created by axel on 16/05/17.
 */

public class AskForPermissionsActivity extends Activity {

    static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareItem();
        finish();
    }

    private void ShareItem(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, new BitmapUtils().GetBitmapFromPath(getPath(getApplicationContext())));
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

}
