package com.lenovo.smarttraffic.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * 自己的工具类
 *
 * @author asus
 */
public class ScreenUtil {

    private static final String TAG = "ScreenUtil";

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Log.e(TAG, "getScreenWidth: " + manager);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        Log.e(TAG, "getScreenWidth: "+metrics.widthPixels );
        return metrics.widthPixels;
    }
}
