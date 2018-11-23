package com.testapp.readysteadybang.game;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testapp.readysteadybang.R;

public class GameDrawer {

	private GameCoordinator coordinator;

	private Button startBtn;
	private TextView myScoreTV, hisScoreTV, commandTV;
	private LinearLayout firstPlayerField;
	private ConstraintLayout bottomPanel;
	private ImageView firstPlayerIV, secondPlayerIV;

	public GameDrawer(Activity activity, GameCoordinator coordinator, View.OnClickListener listener){
		this.coordinator = coordinator;
		initUI(activity, listener);
	}

	private void initUI(Activity activity, View.OnClickListener listener){
		firstPlayerField = activity.findViewById(R.id.first_player_field);
		firstPlayerField.setOnClickListener(listener);
		commandTV = activity.findViewById(R.id.command_tv);
		firstPlayerIV = activity.findViewById(R.id.first_player_img);
		secondPlayerIV = activity.findViewById(R.id.second_player_img);

		bottomPanel = activity.findViewById(R.id.bottom_panel);
		startBtn = activity.findViewById(R.id.start_btn);
		startBtn.setOnClickListener(listener);
		myScoreTV = activity.findViewById(R.id.myScore_tv);
		hisScoreTV = activity.findViewById(R.id.hisScore_tv);

		activity.findViewById(R.id.exit_btn).setOnClickListener(listener);
	}

	private void hideBottomPanel(boolean flag){
		if(flag){
			bottomPanel.setVisibility(View.GONE);
			firstPlayerField.setEnabled(true);
		}else{
			bottomPanel.setVisibility(View.VISIBLE);
			firstPlayerField.setEnabled(false);
		}
	}

	private void checkShooterState(){
		if(coordinator.getFirstShooter().isAlive()){
			secondPlayerIV.setImageResource(R.drawable.shooter_dead);
		}else{
			firstPlayerIV.setImageResource(R.drawable.shooter_dead);
		}
	}

	public void changeState(Game.GameState newState){
		switch (newState){
			case Loaded:
				hideBottomPanel(false);
				commandTV.setText("");
				break;

			case Ready:
				hideBottomPanel(true);
				commandTV.setText(R.string.ready);
				firstPlayerIV.setImageResource(R.drawable.shooter);
				secondPlayerIV.setImageResource(R.drawable.shooter_reflected);
				break;

			case Steady:
				hideBottomPanel(true);
				commandTV.setText(R.string.steady);
				break;

			case Bang:
				hideBottomPanel(true);
				commandTV.setText(R.string.bang);
				break;

			case Paused:
				hideBottomPanel(false);
				checkShooterState();
				myScoreTV.setText(String.valueOf(coordinator.getFirstShooter().getNumberOfHits()));
				hisScoreTV.setText(String.valueOf(coordinator.getSecondShooter().getNumberOfHits()));
				break;

			case Finished:
				hideBottomPanel(false);
				checkShooterState();
				if(coordinator.getFirstShooter().getNumberOfHits() >= coordinator.getNumHitsForWin()){
					commandTV.setText(R.string.win);
				}else{
					commandTV.setText(R.string.lose);
				}
				myScoreTV.setText(String.valueOf(coordinator.getFirstShooter().getNumberOfHits()));
				hisScoreTV.setText(String.valueOf(coordinator.getSecondShooter().getNumberOfHits()));
				startBtn.setVisibility(View.GONE);
				break;
		}
	}

	public void destroy(){
		coordinator = null;
		startBtn = null;
		myScoreTV = null;
		hisScoreTV = null;
		commandTV = null;
		firstPlayerField = null;
		bottomPanel = null;
		firstPlayerIV = null;
		secondPlayerIV = null;
	}

}
