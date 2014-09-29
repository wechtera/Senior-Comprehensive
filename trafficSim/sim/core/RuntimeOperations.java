package ec.app.trafficSim.sim.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import ec.app.trafficSim.sim.io.ECJPlug;
import ec.app.trafficSim.sim.util.*;

/**
 * Logic for moving the car through system as well as updating cars in system and having cars exit the system
 * @author
 * 		Adam Wechter
 */
public class RuntimeOperations {
	
	
	/**
	 * checks for conflicts for stop signs
	 * @param car1
	 * @param car2
	 * @return
	 * 		true if no conflict, false otherwise
	 */
	public static boolean hasNoConflict(CarGen car1, CarGen car2) {
		if(car2 == null)
			return true;
		String car1CurrentQueueType = car1.getQueueType();
		String car2CurrentQueueType = car2.getQueueType();
		
		String car1Movement = car1.getCarMovement();
		String car2Movement = car2.getCarMovement();
		
		if( (car1CurrentQueueType.equals("NORTH") && car2CurrentQueueType.equals("SOUTH")) || (car1CurrentQueueType.equals("SOUTH") && car2CurrentQueueType.equals("NORTH")) || (car1CurrentQueueType.equals("EAST") && car2CurrentQueueType.equals("WEST")) || (car1CurrentQueueType.equals("WEST") && car2CurrentQueueType.equals("EAST")) ) {
			if(car1Movement.equals("STRAIGHT") || car1Movement.equals("RIGHT")) {
					if(car2Movement.equals("STRAIGHT") || car2Movement.equals("RIGHT"))
						return true;
					else
						return false;
			}
			else {
				if(car2Movement.equals("LEFT"))
					return true;
				else
					return false;
			}
		}
		
		else if( (car1CurrentQueueType.equals("NORTH") && car2CurrentQueueType.equals("WEST")) || (car1CurrentQueueType.equals("WEST") && car2CurrentQueueType.equals("SOUTH")) || (car1CurrentQueueType.equals("SOUTH") && car2CurrentQueueType.equals("EAST")) || (car1CurrentQueueType.equals("EAST") && car2CurrentQueueType.equals("NORTH")) ) {
			if(car1Movement.equals("STRAIGHT"))
				return false;
			else if(car1Movement.equals("LEFT")) {
				if(car2Movement.equals("RIGHT"))
					return true;
				else
					return false;
			}
			else {
				if(car2Movement.equals("STRAIGHT") || car2Movement.equals("RIGHT"))
					return true;
				else
					return false;
			}
				
		}
		
		else {
			if(car1Movement.equals("STRAIGHT")) {
				if(car2Movement.equals("RIGHT"))
					return true;
				else
					return false;
			}
			else if(car1Movement.equals("RIGHT")) {
				if(car2Movement.equals("LEFT"))
					return true;
				else
					return false;
			}
			else {
				if(car2Movement.equals("LEFT"))
					return true;
				else
					return false;
			}
		}
	}

	/**
	 * Preps and removes car from a system as well as sends the car to have its information recorded
	 * @param car
	 */
	public static void exitSystem(CarGen car) {
		car.setIsInSystem(false);
		if(car.getCurrentQueue() != null)
			car.getCurrentQueue().getQueue().remove(car);
		if(!car.getIsFirstRotation() && car.getTotalTime() > 2)
			car.getCurrentQueue().getQueue().remove(car);  
		recordInfo(car);
	}

	/**
	 * Clear all times other than total time in system
	 * Also moves into next node/queue
	 * @param car
	 * 		car in question
	 */
	public static void updateCarInfo(CarGen car) {
		car.setPreviousNode();

		car.setCurrentNode();
		car.setNextNode(); 

		car.setTimeAtFront(0.0);
		car.setTimeInCurrent(0);
		if(car.getCurrentNode().getType().equals("intersection")) {
			car.setDelayTime(car.getDelayTime() + 1);
		}

		car.updateCurrentQueue(false);
		car.setCarMovement();
		car.setNodeType();
		car.setCanGo(false);
		car.setPriority(-1);
		car.setTotalTime((car.getTotalTime()+1));	
		
	}

	/**
	 * will only be used the first time and just to move it into the next node
	 * @param car
	 * 		car in question
	 */
	public static void updateCarInfoFirst(CarGen car) {
		car.setIsFirstRotation();
		car.setPreviousNode();
		car.setNextNode();
		car.setCurrentNode();
		car.setNextNode();
		car.updateCurrentQueue(true);
		car.setTimeAtFront(0);
		car.setTimeInCurrent(0);
		car.setCarMovement();
		car.setTotalTime((car.getTotalTime()+1));
		
	}


	/**
	 * if car is still on the road, we just want to increment timers so basically "do nothing"
	 * @param car
	 * 		car is in some queue and we want it to just sit
	 */
	public static void doNothing(CarGen car) {
		if(car.getCurrentQueue().getQueue().peek().equals(car))
			car.setTimeAtFront((car.getTimeAtFront() + 1));
		car.setTimeInCurrent((car.getTimeInCurrent() +1));
		car.setTotalTime((car.getTotalTime()+1));
		if(car.getCurrentNode().getType().equals("intersection")) {
			car.setDelayTime(car.getDelayTime() + 1);
		}
		car.setCanGo(false);
	}


	/**
	 * Handles decison making and logic as to whether or not the car can update/move on or must just sit/donothing
	 * @param car
	 */
	public static void roadOperations(CarGen car) {
		double timeOnRoad = car.getTimeInCurrent();
	
		if(car.getIsFirstRotation() == true) {
			updateCarInfoFirst(car);
			return;
		}
		if(!car.getCanGo()) {
			doNothing(car);
			return;
		}
		if(!car.getCurrentQueue().getQueue().peek().equals(car)) {
			doNothing(car);
			return;
		}
		setRestOfQueueNoMove(car); 

		if((timeOnRoad * car.getCurrentNode().getSpeedLimit()) >= car.getCurrentNode().getDistance()  && car.getCurrentQueue().getQueue().peek().equals(car)) {
			updateCarInfo(car);
			return;
		}
		else
			doNothing(car);
		
	}


	/**
	 * Handles decision making and logic as to whether or not the car can update/move on or needs to do nothing and just sit
	 * @param car
	 */
	public static void signIntersectionOperation(CarGen car) {
		if(car.getIsFirstRotation() == true) {  //first rotation
			updateCarInfoFirst(car);
			return;
		}
		if(!car.getCanGo()) {
			doNothing(car);
			return;
		}
		if(!car.getCurrentQueue().getQueue().peek().equals(car)) {  //if not at the front cant go
			doNothing(car);
			return;
		}
		
		setRestOfQueueNoMove(car);  //at front of its respective queue, rest cannot move this second
		if(car.getTimeAtFront() > 4) {
			updateCarInfo(car);
			return;
		}
		ArrayList<CarGen> thereLongest = getThereLongest(car);		
		
		if(!thereLongest.contains(car)) {
			doNothing(car);
			return;
		}
		else {
			for(int i = 0; i < thereLongest.size(); i++) {
				if( thereLongest.get(i) == null) {
					thereLongest.remove(i);
					i = 0;
				}
			}
			if(thereLongest.size() == 1 ) { //if theres only one car that has priority and if its this one, it can go
				updateCarInfo(car);
				return;
			}
			else if (thereLongest.get(1)==null) {
				updateCarInfo(car);
				return;
			}
			else if(thereLongest.size() == 2 ) {
				//check for conflict
				CarGen car2 = thereLongest.get((thereLongest.get(0).equals(car) ? 1 : 0));
				if(hasNoConflict(car, car2)) {
					updateCarInfo(car);
					return;
				}

				int maybe = (int)(Math.random()*200);
				if(maybe %2 != 0) {  //cant go
					doNothing(car);
					return;
				}	
				else {
					updateCarInfo(car);
					car2.setCanGo(false);
				}
			}
			else if(thereLongest.size() == 3 ) {
				int maybe = (int)(Math.random()*200);
				if(maybe %3 != 0) {  //cant go
					doNothing(car);
					return;
				}
				else {
					thereLongest.remove(car);
					CarGen car2 = thereLongest.get(0);
					if(hasNoConflict(car, car2)) {
						updateCarInfo(car);
						thereLongest.get(1).setCanGo(false);
						return;
					}
					else if(hasNoConflict(car, thereLongest.get(1))) {
						updateCarInfo(car);
						car2.setCanGo(false);
						return;
					}
					else {
						updateCarInfo(car);
						for(CarGen temp : thereLongest)
							temp.setCanGo(false);
						return;
					}
				}
			}
			else if(thereLongest.size() == 4) {  //all four arrived at same time
				int maybe = (int)(Math.random()*200);
				if(maybe %4 != 0) {  //cant go
					doNothing(car);
					return;
				}
				else {
					thereLongest.remove(car);
					if(hasNoConflict(car, thereLongest.get(0))) {
						updateCarInfo(car);
						thereLongest.get(1).setCanGo(false);
						thereLongest.get(2).setCanGo(false);
						return;
					}
					else if(hasNoConflict(car, thereLongest.get(1))) {
						updateCarInfo(car);
						thereLongest.get(0).setCanGo(false);
						thereLongest.get(2).setCanGo(false);
						return;
					}
					else if(hasNoConflict(car, thereLongest.get(2))) {
						updateCarInfo(car);
						thereLongest.get(0).setCanGo(false);
						thereLongest.get(1).setCanGo(false);
						return;
					}
					else {
						updateCarInfo(car);
						thereLongest.get(0).setCanGo(false);
						thereLongest.get(1).setCanGo(false);
						thereLongest.get(2).setCanGo(false);
						return;
					}
				}
				
			}
		}
	}

	
	/**
	 * Handles decision making and logic as to whether or not the car can update/move on or needs to do nothing
	 * @param car
	 */
	public static void lightIntersectionOperation(CarGen car) {
		if(car.getIsFirstRotation() == true)  { //first rotation
			updateCarInfoFirst(car);
			return;
		}
		if(!car.getCanGo()) {
			doNothing(car);
			return;
		}
		
		boolean isGreen = isGreen(car);
		
		if(isGreen) {
			if(!car.getCurrentQueue().getQueue().peek().equals(car))  //if not front
				doNothing(car);	
			else {  //front of queue
				setRestOfQueueNoMove(car); //nothing else can move
				if(car.getCarMovement().equals("STRAIGHT") || car.getCarMovement().equals("RIGHT")) {
					updateCarInfo(car);
				}
				else
					if(getQueueOpposite(car).getQueue().isEmpty()) { 
						updateCarInfo(car);
					}
					else
						doNothing(car);
			}
		}
		else { //is red or transition
			if(car.getCurrentNode().getCurrentState() == Node.Rotation.TRANSITION && car.getCarMovement().equals("LEFT") && car.getCurrentQueue().getQueue().peek().equals(car)) {
				updateCarInfo(car);
				setRestOfQueueNoMove(car);
			}
			else if( car.getCurrentNode().getCurrentState() != Node.Rotation.TRANSITION && car.getCarMovement().equals("Right") && getQueueLeft(car).getQueue().isEmpty() && car.getCurrentQueue().getQueue().peek().equals(car)) {
				updateCarInfo(car);
				setRestOfQueueNoMove(car);
			}
			else
				doNothing(car);
		}
		
	}


	/**
	 * Simple method to determine if green or not green.  Note if not green return false, notonly if red, but also transition
	 * @param car
	 * @return
	 * 		if the car.currentQueue's light is green
	 */
	private static boolean isGreen(CarGen car) {
		if( (car.getQueueType().equals("NORTH") || car.getQueueType().equals("SOUTH")) )
			if(car.getCurrentNode().getCurrentState() == Node.Rotation.NORTHSOUTH)
				return true;
			else
				return false;
		else
			if(car.getCurrentNode().getCurrentState() == Node.Rotation.EASTWEST)
				return true;
			else
				return false;
	}


	/**
	 * Gets opposite direction queue
	 * @param car
	 * @return
	 * 		queue opposite of car's queue
	 */
	private static DirectionalQueue getQueueOpposite(CarGen car) {
		if(car.getCurrentQueue().getDirection().equals("NORTH"))
				return car.getCurrentNode().getSouthQueue();
		else if(car.getCurrentQueue().getDirection().equals("SOUTH"))
			return car.getCurrentNode().getNorthQueue();
		else if(car.getCurrentQueue().getDirection().equals("EAST"))
			return car.getCurrentNode().getWestQueue();
		else
			return car.getCurrentNode().getEastQueue();
	}


	/**
	 * Gets the queue to the left in the intersection
	 * @param car
	 * @return
	 * 		queue to left of car's queue
	 */
	private static DirectionalQueue getQueueLeft(CarGen car) {
		if(car.getCurrentQueue().getDirection().equals("NORTH"))
			return car.getCurrentNode().getEastQueue();
		else if(car.getCurrentQueue().getDirection().equals("SOUTH"))
			return car.getCurrentNode().getWestQueue();
		else if(car.getCurrentQueue().getDirection().equals("EAST"))
			return car.getCurrentNode().getSouthQueue();
		else
			return car.getCurrentNode().getNorthQueue();
	}


	/**
	 * Gets the queue to the right in the intersection
	 * @param car
	 * @return
	 * 		queue to right of car's queue
	 */
	private static DirectionalQueue getQueueRight(CarGen car) {
		if(car.getCurrentQueue().getDirection().equals("NORTH"))
			return car.getCurrentNode().getWestQueue();
		else if(car.getCurrentQueue().getDirection().equals("SOUTH"))
			return car.getCurrentNode().getEastQueue();
		else if(car.getCurrentQueue().getDirection().equals("EAST"))
			return car.getCurrentNode().getNorthQueue();
		else
			return car.getCurrentNode().getSouthQueue();
	}


	/**
	 * Method calculates of all of the cars at an intersection, which was there longest
	 * Only used in stop sign intersections.
	 * @param car
	 * @return
	 * 		Car that was in the queue longest out of all of them
	 */
	private static ArrayList<CarGen> getThereLongest(CarGen car) {
		ArrayList<CarGen> thereLongest= new ArrayList<CarGen>();
		thereLongest.add(car);
		
		
		boolean isLeftEmpty = getQueueLeft(car).getQueue().isEmpty();
		boolean isRightEmpty = getQueueRight(car).getQueue().isEmpty();
		boolean isOppositeEmpty = getQueueOpposite(car).getQueue().isEmpty();
		
		if(!isLeftEmpty && getQueueLeft(car).getQueue().peek()==null){
			
			isLeftEmpty = true;
		}
		if(!isRightEmpty && getQueueRight(car).getQueue().peek()==null)
			isRightEmpty = true;
		if(!isOppositeEmpty && getQueueOpposite(car).getQueue().peek()== null)
			isOppositeEmpty = true;
		double leftTimeAtFront = 0;
		double rightTimeAtFront = 0;
		double oppositeTimeAtFront = 0;
		double thisTimeAtFront = car.getTimeAtFront();
		double longestTimeAtFront = thisTimeAtFront;
		if(!isLeftEmpty && getQueueLeft(car).getQueue().peek().getCanGo()) 
			leftTimeAtFront = getQueueLeft(car).getQueue().peek().getTimeAtFront();
		
		if(!isRightEmpty && getQueueRight(car).getQueue().peek().getCanGo()) 
			rightTimeAtFront = getQueueRight(car).getQueue().peek().getTimeAtFront();
		
		if(!isOppositeEmpty && getQueueOpposite(car).getQueue().peek().getCanGo()) 
			oppositeTimeAtFront = getQueueOpposite(car).getQueue().peek().getTimeAtFront();
		
		if(leftTimeAtFront > longestTimeAtFront) {
			thereLongest.clear();
			longestTimeAtFront = leftTimeAtFront;
			thereLongest.add(getQueueLeft(car).getQueue().peek());
		}
		else if(leftTimeAtFront == longestTimeAtFront)
			thereLongest.add(getQueueLeft(car).getQueue().peek());
		else;
		
		if(rightTimeAtFront > longestTimeAtFront) {
			thereLongest.clear();
			longestTimeAtFront = rightTimeAtFront;
			thereLongest.add(getQueueRight(car).getQueue().peek());
		}
		else if(rightTimeAtFront == longestTimeAtFront)
			thereLongest.add(getQueueRight(car).getQueue().peek());
		else;
		
		if(oppositeTimeAtFront > longestTimeAtFront) {
			thereLongest.clear();
			longestTimeAtFront = oppositeTimeAtFront;
			thereLongest.add(getQueueOpposite(car).getQueue().peek());
		}
		else if(oppositeTimeAtFront == longestTimeAtFront)
			thereLongest.add(getQueueOpposite(car).getQueue().peek());
		else;
		return thereLongest;
	}


	/**
	 * If the car at the front has moved, that means that another queue still in the same "time frame" could
	 * have its decision making moved later.  If this is the case then we need to indicate that all other 
	 * cars within this queue are not allowed to move.  This method does it by using a temporary queue to store
	 * the existing queue.
	 * @param car
	 * 		some car at the front of a queue that made some action and should forbid others from doing so
	 */
	public static void setRestOfQueueNoMove(CarGen car) {
		CarGen c = null;
		Queue<CarGen> q = car.getCurrentQueue().getQueue();
		//at this point we know it IS at the front so set all of them to cant move
		for(int i = 0; i < car.getCurrentQueue().getQueue().size(); i++) {
			c = car.getCurrentQueue().getQueue().poll();
			c.setCanGo(false);
			q.add(c);	
		}
	}


	/**
	 * record information of the car into the record string
	 * @param car
 	*/
	private static void recordInfo(CarGen car) {
		ECJPlug.cars.add(car);
		ECJPlug.emissions.add(car.getGreenGas());
		ECJPlug.timeTransit.add(car.getTotalTime());
		ECJPlug.totalDelay.add(car.getDelayTime());
		ECJPlug.milesPerGallon.add(car.getTotalTime() * car.getAverageTimeConsumption());
		
	}
}

