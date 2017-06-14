package com.example.cars;

import java.util.logging.Logger;

public class Car {
	
	private static final Logger log = Logger.getLogger(Car.class.getName());
	
	/** Top speed in m/s */
	private double topSpeed;
	
	/** Accel in m/s2 */
	private double accel;
	
	private static final double hf = 0.8;
	
	private boolean hasNitro;
	
	/** Current speed in m/s */
	private double speed;
	
	private int position;
	
	private Race track;
	
	private int teamno;
	
	public Car(int teamno) {
		assert teamno > 0;
		this.topSpeed = (double)(150 + teamno * 10) / 3.6; // kmh to mps
		this.accel = 2 * teamno;
		this.teamno = teamno;
		log.info("New car #"+teamno+" with topSpeed "+topSpeed+" accel "+ accel);
	}
	
	public void enterRace(Race track, int startPosition) {
		this.hasNitro = true;
		this.speed = 0;
		this.track = track;
		this.position = startPosition;
		log.info(""+this);
	}
	
	public int getPosition() {
		return position;
	}
	
	/** Moves the car based on its current speed */
	private void move() {
		position = (int)Math.round(position + speed * 2);
	}
	
	/** Accelerates as per instructions */
	private void accelerate() {
		speed = Math.min(topSpeed, speed + accel * 2);
	}
	
	/** Apply nitro */
	private void nitro() {
		if (!hasNitro) return;
		log.info("Car #"+teamno+" NITRO!");
		speed = Math.min(topSpeed, speed * 2);
		hasNitro = false;
	}
	
	/** Apply the handling factor */
	private void applyHF() {
		speed *= hf;
		log.info("Car #"+teamno+" HANDLING!");
	}
	
	public void assess() {
		double oSpeed = speed;
		int oPosition = position;
		
		// Move & accelerate
		move();
		accelerate();
		
		// See if other cars are around
		if (track.carsWithin(position-10,position+10).size() > 1) applyHF(); 
		// Note carsWithin will return ourselves so it needs to be greater than one

		// Check ranking & apply nitro
		if (track.getRanking(this) == 0 && hasNitro) nitro();
		
		log.info("Car #"+teamno+" move "+oPosition+" -> "+position+" speed "+oSpeed+" -> "+speed);
	}
	
	@Override public String toString() {
		return "Car #"+teamno+" at "+position+" speed "+speed;
	}

}
