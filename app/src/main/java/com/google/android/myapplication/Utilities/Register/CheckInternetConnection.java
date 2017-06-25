package com.google.android.myapplication.Utilities.Register;

import android.content.Context;
import android.net.ConnectivityManager;


/**
 * Created by Oana on 25-Jun-17.
 */

public class CheckInternetConnection {

    public  boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() ||
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            return true;
        }

        return false;
    }
}
