package com.lenovo.smarttraffic.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.lenovo.smarttraffic.myinterface.BroadListener;

/**
 *
 * 监听网络状况
 * */
public class Bodywork extends BroadcastReceiver {
    private BroadListener mbroadInter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (network != null) {
                Toast.makeText(context, "有网1", Toast.LENGTH_SHORT).show();
                mbroadInter.netFreeEvent();
            } else {
                Toast.makeText(context, "网络有问题请检查1", Toast.LENGTH_SHORT).show();
                mbroadInter.netEvent();
            }
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                }
                Toast.makeText(context, "有网2", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "网络有问题请检查2", Toast.LENGTH_SHORT).show();
                mbroadInter.netEvent();
            }
        }
    }

    public void getonClick(BroadListener broadlistener) {
        mbroadInter = broadlistener;
    }
}
