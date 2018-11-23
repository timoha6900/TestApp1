package com.testapp.readysteadybang.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.testapp.readysteadybang.game.Game;
import com.testapp.readysteadybang.R;

public class GameActivity extends AppCompatActivity {

	private Game game;
	private boolean exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		game = new Game(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		game.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		game.restoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		game.resume();
	}

	@Override
	protected void onPause() {
		game.pause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		game.destroy();
		if(exit){
			final int pid = android.os.Process.myPid();
			android.os.Process.killProcess(pid);
		}
		super.onDestroy();
	}

	public void exit(){
		exit = true;
		finish();
	}

}
