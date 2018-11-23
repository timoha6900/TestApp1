package com.testapp.readysteadybang.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.testapp.readysteadybang.App;
import com.testapp.readysteadybang.R;
import com.testapp.readysteadybang.api.AllowModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

	private final String SERVER_RESPONSE = "SERVER_RESPONSE";

	private final int STATE_LOADING = 1;
	private final int STATE_FAILED = 2;

	private SharedPreferences sPrefs;

	private TextView stateTV;
	private Button repeatBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		initUI();

		sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		changeState(STATE_LOADING);

		checkServerResponse();
	}

	@Override
	protected void onDestroy() {
		sPrefs = null;
		super.onDestroy();
	}

	private void initUI(){
		stateTV = findViewById(R.id.state_tv);

		repeatBtn = findViewById(R.id.repeat_btn);
		repeatBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.repeat_btn) {
					changeState(STATE_LOADING);
					sendRequest();
				}
			}
		});
	}

	private void changeState(int newState){
		switch (newState){
			case STATE_LOADING:
				stateTV.setText(R.string.loading);
				repeatBtn.setVisibility(View.INVISIBLE);
				break;

			case STATE_FAILED:
				stateTV.setText(R.string.failed);
				repeatBtn.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void checkServerResponse(){
		if(sPrefs.contains(SERVER_RESPONSE)){
			checkFlag(sPrefs.getBoolean(SERVER_RESPONSE, false));
		}else{
			sendRequest();
		}
	}

	private void saveAllow(AllowModel allow){
		boolean flag = allow.getAllow();
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putBoolean(SERVER_RESPONSE, flag);
		editor.apply();

		checkFlag(flag);
	}

	private void serverAccessFailed(){
		changeState(STATE_FAILED);
	}

	private void sendRequest(){
		App.getApi().getData("allow").enqueue(new Callback<AllowModel>() {
			@Override

			public void onResponse(Call<AllowModel> call, Response<AllowModel> response) {
				if(response.body() != null) {
					AllowModel answer = response.body();
					Log.d("myLogs", Boolean.toString(answer.getAllow()));
					saveAllow(answer);
				}else{
					Log.d("myLogs", "response.body() == null");
					serverAccessFailed();
				}
			}

			@Override
			public void onFailure(Call<AllowModel> call, Throwable t) {
				Log.d("myLogs", "getData.onFailure", t);
				serverAccessFailed();
			}
		});
	}

	private void checkFlag(boolean flag){
		if(flag){
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			finish();
		}else{
			Intent intent = new Intent(this, WebViewActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
