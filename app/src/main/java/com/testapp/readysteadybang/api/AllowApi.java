package com.testapp.readysteadybang.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AllowApi {
	@GET("/api")
	Call<AllowModel> getData(@Query("action") String resourceName);
}
