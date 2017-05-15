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

import java.util.Arrays;

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

    @Before
    public void before() throws Exception {
        bitmapUtils = new BitmapUtils();
        InitMocks();
    }

    private void InitMocks() {
        this.context = Mockito.mock(Context.class);
    }

    @Test
    public void BitmapUtilsTest() throws Exception {
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.common_ic_googleplayservices);
        String fakePath = ShadowEnvironment.getExternalStorageDirectory().toString();
      //  bitmapUtils.SaveBitmapToPath(bitmap1, fakePath);
        int[] array1 = bitmapToIntArray(bitmap1);
        int[] array2 = bitmapToIntArray(bitmap2);
        assertTrue(Arrays.equals(array1,array2));

    }

    private int[] bitmapToIntArray(Bitmap bitmap) {
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        int[] intArray = new int[x * y];
        bitmap.getPixels(intArray, 0, x, 0, 0, x, y);
        return intArray;
    }


}