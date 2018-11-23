package com.testapp.readysteadybang.game;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.testapp.readysteadybang.R;

import java.lang.ref.WeakReference;

public class GameController implements View.OnClickListener {
	private GameCoordinator coordinator;
	private Thread gameplayThread = null;
	private long firingTimer;
	private ControllerHandler handler;

	public GameController(GameCoordinator coordinator){
		this.coordinator = coordinator;
		handler = new ControllerHandler(this);
	}

	public void resume(){
		initGameplayThread();
	}

	public void pause(){
		if(gameplayThread != null){
			gameplayThread.interrupt();
			gameplayThread = null;
		}
	}

	public void destroy(){
		coordinator = null;
		if(gameplayThread != null){
			gameplayThread.interrupt();
			gameplayThread = null;
		}
		handler = null;
 	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id){
			case R.id.exit_btn:
				coordinator.exit();
				break;

			case R.id.start_btn:
				changeState(Game.GameState.Ready);
				coordinator.getFirstShooter().setAlive();
				coordinator.getFirstShooter().setFiringSpeed(0);
				coordinator.getSecondShooter().setAlive();
				initGameplayThread();
				break;

			case R.id.first_player_field:
				if(coordinator.getGameState() == Game.GameState.Bang
						&& coordinator.getFirstShooter().getFirindSpeed() == 0
						&& coordinator.getFirstShooter().isAlive()){
					firingTimer = System.currentTimeMillis() - firingTimer;
					gameplayThread.interrupt();
					coordinator.getSecondShooter().kill();

					changeState(Game.GameState.Paused);

				}else{
					coordinator.getFirstShooter().setFiringSpeed(-1);
				}
				break;
		}
	}

	private synchronized void changeState(Game.GameState newState){
		if(newState == Game.GameState.Paused){
			if(!coordinator.getSecondShooter().isAlive()){
				coordinator.getFirstShooter().addNewHit();
				coordinator.getFirstShooter().setFiringSpeed(firingTimer);
			}else{
				if(!coordinator.getFirstShooter().isAlive())
					coordinator.getSecondShooter().addNewHit();
			}
			if(gameplayThread != null){
				gameplayThread.interrupt();
				gameplayThread = null;
			}

			final int numHitsForWin = coordinator.getNumHitsForWin();
			if(coordinator.getFirstShooter().getNumberOfHits() >= numHitsForWin
					|| coordinator.getSecondShooter().getNumberOfHits() >= numHitsForWin){
				newState = Game.GameState.Finished;
			}
		}
		coordinator.changeState(newState);
	}


	private void initGameplayThread(){
		gameplayThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					try {
						switch (coordinator.getGameState()) {
							case Ready:
								Thread.sleep(700);
								if(!Thread.currentThread().isInterrupted())
									handler.obtainMessage(0, Game.GameState.Steady).sendToTarget();
								break;

							case Steady:
								long sleepTime = 200 + (long)(Math.random() * 1800);
								Thread.sleep(sleepTime);
								if(!Thread.currentThread().isInterrupted()) {
									handler.obtainMessage(0, Game.GameState.Bang).sendToTarget();
									firingTimer = System.currentTimeMillis();
								}
								break;

							case Bang:
								Thread.sleep(coordinator.getSecondShooter().getFirindSpeed());
								if(!Thread.currentThread().isInterrupted()
										&& coordinator.getSecondShooter().isAlive()) {
									coordinator.getFirstShooter().kill();
									handler.obtainMessage(0, Game.GameState.Paused).sendToTarget();
								}
								Thread.currentThread().interrupt();
								break;
						}
					}catch (InterruptedException e){
						return;
					}
				}
			}
		});
		gameplayThread.start();
	}

	private static class ControllerHandler extends Handler{
		private final WeakReference<GameController> controller;

		public ControllerHandler(GameController gameController){
			controller = new WeakReference<>(gameController);
		}

		@Override
		public void handleMessage(Message msg) {
			if(msg.obj instanceof Game.GameState)
				controller.get().changeState((Game.GameState) msg.obj);
		}
	}
}
