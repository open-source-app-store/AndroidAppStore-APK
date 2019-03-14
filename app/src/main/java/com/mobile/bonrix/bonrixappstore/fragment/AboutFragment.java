package com.mobile.bonrix.bonrixappstore.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mobile.bonrix.bonrixappstore.R;
import com.mobile.bonrix.bonrixappstore.activity.MainActivity;
import com.mobile.bonrix.bonrixappstore.adapter.AppAdapter;
import com.mobile.bonrix.bonrixappstore.model.AppModel;
import com.mobile.bonrix.bonrixappstore.utility.AppUtils;
import com.mobile.bonrix.bonrixappstore.utility.RequestInterface;
import com.mobile.bonrix.bonrixappstore.utility.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends BaseFragment {
    private String TAG = "HomeFragment";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        return view;
    }

    @Override
    public void setToolbarForFragment() {

    }


}
