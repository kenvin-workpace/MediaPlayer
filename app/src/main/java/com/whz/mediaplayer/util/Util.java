package com.whz.mediaplayer.util;

import android.content.Context;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by kevin on 2018/4/30
 */
public class Util {


    /**
     * 转换时长
     */
    public static String formatTime(int milliseconds) {
        String format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sdf.format(milliseconds);
    }

    /**
     * 设备是否有相机
     */
    public static boolean checkCameraHardware(Context ctx) {
        boolean aBoolean = ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        return aBoolean;
    }
}
