package com.testapp.readysteadybang.game;

import android.os.Bundle;

public class Shooter {
	private static final long DefFiringSpeed = 0;

	private static final String ALIVE = "ALIVE";
	private static final String NUMBER_OF_HITS = "NUMBER_OF_HITS";
	private static final String FIRING_SPEED = "FIRING_SPEED";

	private boolean alive;
	private int numberOfHits;
	private long firindSpeed;

	public Shooter(){
		this(DefFiringSpeed);
	}

	public Shooter(long firindSpeed){
		alive = true;
		numberOfHits = 0;
		this.firindSpeed = firindSpeed;
	}

	public Bundle saveInstanceState(){
		Bundle outState = new Bundle();
		outState.putBoolean(ALIVE, alive);
		outState.putInt(NUMBER_OF_HITS, numberOfHits);
		outState.putLong(FIRING_SPEED, firindSpeed);
		return outState;
	}

	public void restoreInstanceState(Bundle savedState){
		if(savedState.containsKey(ALIVE))
			alive = savedState.getBoolean(ALIVE);

		if(savedState.containsKey(NUMBER_OF_HITS))
			numberOfHits = savedState.getInt(NUMBER_OF_HITS);

		if(savedState.containsKey(FIRING_SPEED))
			firindSpeed = savedState.getLong(FIRING_SPEED);
	}


	public boolean isAlive(){
		return alive;
	}

	public void kill(){
		alive = false;
	}

	public void setAlive(){
		alive = true;
	}

	public int getNumberOfHits(){
		return numberOfHits;
	}

	public void addNewHit(){
		numberOfHits++;
	}

	public long getFirindSpeed(){
		return firindSpeed;
	}

	public void setFiringSpeed(long speed){
		firindSpeed = speed;
	}

}
