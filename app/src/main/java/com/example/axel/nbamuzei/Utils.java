package com.example.axel.nbamuzei;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by axel on 08/05/17.
 */

public class Utils {
    public static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
