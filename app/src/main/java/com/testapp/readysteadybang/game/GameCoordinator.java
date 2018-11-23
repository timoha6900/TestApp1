package com.testapp.readysteadybang.game;

public interface GameCoordinator {
	void exit();
	void changeState(Game.GameState newState);
	Game.GameState getGameState();
	Shooter getFirstShooter();
	Shooter getSecondShooter();
	int getNumHitsForWin();
}
