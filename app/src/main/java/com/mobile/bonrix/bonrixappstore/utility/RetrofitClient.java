package com.mobile.bonrix.bonrixappstore.utility;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import com.mobile.bonrix.bonrixappstore.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CRAFT BOX on 5/1/2017.
 */

public class RetrofitClient {
    static String TAG = "RetrofitClient";

    private static Retrofit retrofit = null;
    public static String Image_url;

//    public static String service_url = Resources.getSystem().getResourceName(R.string.service_url);
//    public static String Image_url = Resources.getSystem().getResourceName(R.string.Image_url);
//    public static String service_url = "http://bonrix.myappstore.co.in/BonrixAppStore/Services/Service.asmx/";
//    public static String Image_url = "http://bonrix.myappstore.co.in/BonrixAppStore/Raw_Details/Store/easovation/App/";


    public static Retrofit getClient(Activity activity) {
        String service_url = activity.getResources().getString(R.string.service_url);
        Image_url = activity.getResources().getString(R.string.Image_url);

        Log.e(TAG, "service_url   " + service_url);
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(36000, TimeUnit.SECONDS)
                    .connectTimeout(36000, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(service_url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
