package com.example.axel.nbamuzei;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.axel.nbamuzei.ImageServices.BitmapUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowEnvironment;

import static junit.framework.Assert.assertTrue;

/**
 * Created by axel on 15/05/17.
 */
@Config(packageName="com.example.axel.nbamuzei")
@RunWith(RobolectricTestRunner.class)
public class BitmapUtilsTest {
    @Mock
    Context context;
    BitmapUtils bitmapUtils;
    Bitmap bitmap1;
    String mockPath;

    @Before
    public void before() throws Exception {
        bitmapUtils = new BitmapUtils();
        InitMocks();
        mockPath = ShadowEnvironment.getExternalStorageDirectory().toString();
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.common_full_open_on_phone);
    }

    private void InitMocks() {
        this.context = Mockito.mock(Context.class);
    }

    @Test
    public void SaveBitmapToPathTest() throws Exception {
        boolean result= bitmapUtils.SaveBitmapToPath(bitmap1, mockPath);
        assertTrue(result);
    }

    @Test
    public void GetBitmapFromPathTest() throws Exception {
        boolean result= bitmapUtils.SaveBitmapToPath(bitmap1, mockPath);
        if(result) {
            Bitmap bitmap = bitmapUtils.GetBitmapFromPath(mockPath);
            assertTrue(bitmap!=null);
        }
    }


}