package com.schenkel.axel.muzeibasket.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

/**
 * Created by axel on 24/05/17.
 */

public class Connectivity {
    public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
    public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
    public static final int NETWORK_TYPE_IDEN = 11; // Level 8
    public static final int NETWORK_TYPE_LTE = 13; // Level 11

    public boolean isConnected(Context context) {
        if(!isAirplaneModeOn(context))
        {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return isConnectionFast(info.getType(), info.getSubtype());
            }
        }
        return false;
    }

    public boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                AIRPLANE_MODE_ON, 0) != 0;
    }
    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~  // 50-100  // kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return false;// ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return false; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 // kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 // Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return false; // ~// 700-1700// kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23// Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return false ;// ~ 400-7000// kbps
                case Connectivity.NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps
                case Connectivity.NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps
                case Connectivity.NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 // Mbps
                case Connectivity.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps
                case Connectivity.NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
