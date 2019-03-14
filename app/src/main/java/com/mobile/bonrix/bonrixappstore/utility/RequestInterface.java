package com.mobile.bonrix.bonrixappstore.utility;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {

    @GET("GetApp_List")
    Call<ResponseBody> getLatestStatus();
}
