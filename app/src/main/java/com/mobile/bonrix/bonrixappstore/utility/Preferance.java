package com.mobile.bonrix.bonrixappstore.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;


public class Preferance {

    static String TAG = "Preferance";

    public static ArrayList<String> setchItem = new ArrayList<>();


    public static SharedPreferences getPreferanse(Context context) {
        return context.getSharedPreferences("PANDEVIDEOSTATUS", Activity.MODE_PRIVATE);
    }


    public static void saveAdsClickCount(Context context, int positon) {
        SharedPreferences.Editor editor = getPreferanse(context).edit();
        editor.putInt("adsclickecount", positon);
        editor.commit();
    }

    public static int getAdsClickCount(Context context) {
        SharedPreferences mSharedPreferences = getPreferanse(context);
        return mSharedPreferences.getInt("adsclickecount", 0);
    }


    public static void saveDownloadFileValue(Context context, String para, int positon) {
        SharedPreferences.Editor editor = getPreferanse(context).edit();
        editor.putInt("DOWN" + para, positon);
        editor.commit();
    }


}