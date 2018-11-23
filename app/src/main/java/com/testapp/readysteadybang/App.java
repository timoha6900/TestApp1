package com.testapp.readysteadybang;

import android.app.Application;

import com.testapp.readysteadybang.api.AllowApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

	private static AllowApi allowApi;
	private Retrofit retrofit;

	@Override
	public void onCreate() {
		super.onCreate();

		retrofit = new Retrofit.Builder()
				.baseUrl(getString(R.string.baseURL))
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		allowApi = retrofit.create(AllowApi.class);
	}

	public static AllowApi getApi() {
		return allowApi;
	}

}
