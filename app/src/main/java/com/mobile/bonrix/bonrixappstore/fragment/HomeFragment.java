package com.mobile.bonrix.bonrixappstore.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mobile.bonrix.bonrixappstore.R;
import com.mobile.bonrix.bonrixappstore.adapter.AppAdapter;
import com.mobile.bonrix.bonrixappstore.model.AppModel;
import com.mobile.bonrix.bonrixappstore.utility.AppUtils;
import com.mobile.bonrix.bonrixappstore.utility.RequestInterface;
import com.mobile.bonrix.bonrixappstore.utility.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = "HomeFragment";
    static ArrayList<AppModel> data = new ArrayList<>();
    static RecyclerView recyclerView;
    static AppAdapter appAdapter;
    static SwipeRefreshLayout swipeRefreshLayout;
    static SimpleDraweeView image;
    View view;
    static RelativeLayout relScreenLoder;
    static Activity activity;
    static ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initComponent();


//        getAppList();
        return view;
    }

    public static void getAppList() {
        progressBar.setVisibility(View.VISIBLE);

        data.clear();
        if (data.size() == 0) {
            swipeRefreshLayout.setRefreshing(false);
        }
        RequestInterface req = RetrofitClient.getClient(activity).create(RequestInterface.class);
        Call<ResponseBody> call = req.getLatestStatus();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                relScreenLoder.setVisibility(relScreenLoder.GONE);
                progressBar.setVisibility(View.GONE);

                if (data.size() == 0) {
//                    relScreenLoder.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    data.clear();
                } else {
                    data.remove(data.size() - 1);
                }
                try {
                    String jsonst = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonst);
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    if (jsonArray.length() <= 0) {
                        relScreenLoder.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AppModel appModel = new AppModel();
                            appModel.setId(String.valueOf(i + 1));
                            appModel.setApkName(jsonObject1.getString("ApkName"));
                            appModel.setPublishdate(jsonObject1.getString("DateTime"));
                            appModel.setImageUrl(jsonObject1.getString("ImageUrl"));
                            appModel.setName(jsonObject1.getString("Name"));
                            appModel.setPackage(jsonObject1.getString("Package"));
                            appModel.setVersion(jsonObject1.getString("Version"));
                            appModel.setApkPath(jsonObject1.getString("ApkPath"));
                            data.add(appModel);
                        }
                        appAdapter.notifyDataSetChanged();


                    } else {
//                        relScreenLoder.setVisibility(View.VISIBLE);
                        Toast.makeText(activity, "No Data Found....!!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                relScreenLoder.setVisibility(relScreenLoder.VISIBLE);
                System.out.print("error" + t.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAppList();
    }


    private void initComponent() {
        recyclerView = view.findViewById(R.id.recyclerView);
        relScreenLoder = view.findViewById(R.id.rel_screen_loder);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appAdapter = new AppAdapter(data, getActivity());
        recyclerView.setAdapter(appAdapter);
    }

    @Override
    public void onRefresh() {

        if (AppUtils.isNetworkConnected(getActivity())) {
            data.clear();
            getAppList();
            swipeRefreshLayout.setRefreshing(true);
            recyclerView.removeAllViews();
//            isPullRefresh=true;
        } else {
            data.clear();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "No Network Present!!", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void setToolbarForFragment() {

    }
}
