package com.testapp.readysteadybang.game;

import android.os.Bundle;

import com.testapp.readysteadybang.activities.GameActivity;

public class Game {
	private final String GAME_STATE = "GAME_STATE";
	private final String NUM_HITS_FOR_WIN = "NUM_HITS_FOR_WIN";
	private final String FIRST_SHOOTER = "FIRST_SHOOTER";
	private final String SECOND_SHOOTER = "SECOND_SHOOTER";

	private GameActivity activity;
	private GameController controller;
	private GameDrawer drawer;
	private GameCoordinator coordinator;

	private GameState gameState;

	private Shooter firstShooter, secondShooter;

	private int numHitsForWin;

	public enum GameState{
		Loaded, Ready, Steady, Bang, Paused, Finished
	}

	public Game(GameActivity activity){
		this.activity = activity;
		initGameCoordinator();
		controller = new GameController(coordinator);
		drawer = new GameDrawer(activity, coordinator, controller);

		gameState = GameState.Loaded;

		firstShooter = new Shooter();
		secondShooter = new Shooter(400);

		numHitsForWin = 3;
	}

	public void saveInstanceState(Bundle outState){
		outState.putSerializable(GAME_STATE, gameState);
		outState.putInt(NUM_HITS_FOR_WIN, numHitsForWin);

		outState.putBundle(FIRST_SHOOTER, firstShooter.saveInstanceState());
		outState.putBundle(SECOND_SHOOTER, secondShooter.saveInstanceState());
	}

	public void restoreInstanceState(Bundle savedState){
		if(savedState.containsKey(NUM_HITS_FOR_WIN))
			numHitsForWin = savedState.getInt(NUM_HITS_FOR_WIN);

		if(savedState.containsKey(FIRST_SHOOTER) && savedState.getBundle(FIRST_SHOOTER) != null)
			firstShooter.restoreInstanceState(savedState.getBundle(FIRST_SHOOTER));

		if(savedState.containsKey(SECOND_SHOOTER) && savedState.getBundle(SECOND_SHOOTER) != null)
			secondShooter.restoreInstanceState(savedState.getBundle(SECOND_SHOOTER));

		if(savedState.containsKey(GAME_STATE) && savedState.getSerializable(GAME_STATE) != null)
			coordinator.changeState((GameState) savedState.getSerializable(GAME_STATE));

		resume();
	}

	public void resume(){
		if(gameState == GameState.Bang) {
			coordinator.changeState(GameState.Ready);
			controller.resume();
		}else{
			if(gameState == GameState.Ready || gameState == GameState.Steady)
				controller.resume();
		}
	}

	public void pause(){
		controller.pause();
	}

	public void destroy(){
		activity = null;

		controller.destroy();
		controller = null;

		drawer.destroy();
		drawer = null;

		coordinator = null;
	}

	private void initGameCoordinator(){
		coordinator = new GameCoordinator() {
			@Override
			public void exit() {
				activity.exit();
			}

			@Override
			public void changeState(GameState newState) {
				gameState = newState;
				drawer.changeState(gameState);
			}

			@Override
			public GameState getGameState() {
				return gameState;
			}

			@Override
			public Shooter getFirstShooter() {
				return firstShooter;
			}

			@Override
			public Shooter getSecondShooter() {
				return secondShooter;
			}

			@Override
			public int getNumHitsForWin() {
				return numHitsForWin;
			}
		};
	}
}
