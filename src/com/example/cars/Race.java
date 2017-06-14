package com.example.cars;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * In a Formula-1 challenge, there are n teams numbered 1 to n. Each team has a
 * car and a driver. Car’s specification are as follows: 
 * <li> Top speed: (150 + 10 * i) km per hour 
 * <li> Acceleration: (2 * i) meter per second square. 
 * <li> Handling factor (hf) = 0.8 
 * <li> Nitro : Increases the speed to double or top speed, whichever is less. Can be used only once.
 * 
 * Here i is the team number. The cars line up for the race. The start line for
 * (i + 1)th car is 200 * i meters behind the ith car.
 * 
 * All of them start at the same time and try to attain their top speed. A
 * re-assessment of the positions is done every 2 seconds(So even if the car has
 * crossed the finish line in between, you’ll get to know after 2 seconds).
 * During this assessment, each driver checks if there is any car within 10
 * meters of his car, his speed reduces to: hf * (speed at that moment). Also,
 * if the driver notices that he is the last one on the race, he uses ‘nitro’.
 * 
 * Taking the number of teams and length of track as the input, Calculate the
 * final speeds and the corresponding completion times.
 * 
 */
public class Race {

	private static final Logger log = Logger.getLogger(Race.class.getName());

	private static final int MAX_ITERATIONS = 10000;

	private Car[] cars;
	private Car[] carsRanked;

	private int trackLength;

	public Race(int trackLength, int numTeams) {
		this.trackLength = trackLength;
		// Make cars
		cars = new Car[numTeams];
		for (int i = 0; i < numTeams; i++)
			cars[i] = new Car(i + 1);
		carsRanked = cars.clone();
	}

	public void setup() {
		int position = 0;
		for (int i = cars.length - 1; i >= 0; i--) {
			cars[i].enterRace(this, position);
			position += 200;
		}
		updateRanking();
	}

	public int getRanking(Car car) {
		// Sort as per current position
		for (int i = 0; i < carsRanked.length; i++)
			if (carsRanked[i] == car)
				return i;
		throw new RuntimeException("Car not found");
	}

	public void updateRanking() {
		carsRanked = cars.clone();
		Arrays.sort(carsRanked, (car1, car2) -> car1.getPosition() - car2.getPosition());

		StringBuffer sb = new StringBuffer("Race rankings:\n");
		for (int i = 0; i < carsRanked.length; i++)
			sb.append("Position [" + i + "] car " + carsRanked[i] + "\n");
		log.info(sb.toString());
	}

	public Collection<Car> carsWithin(long min, long max) {
		return Arrays.stream(cars).filter(car -> car.getPosition() >= min && car.getPosition() <= max)
				.collect(Collectors.toList());
	}

	/** does step in race, returns true if the race needs to continue */
	private boolean raceStep() {
		// move each car
		for (Car c : cars)
			c.assess();

		// update rankings
		updateRanking();

		// find car first in the race (with highest position at the end of the
		// list)
		int bestPosition = carsRanked[carsRanked.length - 1].getPosition();
		return (bestPosition <= trackLength);
	}

	/** Executes race and returns winner or throws an exception on failsafe */
	public Car race() {
		setup();

		boolean race;
		int failsafe = 0;
		do {
			race = raceStep();

			if (++failsafe == MAX_ITERATIONS) {
				throw new RuntimeException("Failsafe activated!");
			}
			;

		} while (race);
		return carsRanked[carsRanked.length - 1];
	}

	public static void main(String[] args) {
		Race r = new Race(10000, 5);
		Car winner = r.race();
		log.info("Winner:" + winner);
	}
}
