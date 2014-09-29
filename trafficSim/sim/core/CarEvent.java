package ec.app.trafficSim.sim.core;
import ec.app.trafficSim.sim. util.*;
/**
 * Car event contains a car
 * Important method is execute, which allows car to move through system
 * @author 
 *		Adam Wechter
 */
public class CarEvent extends Event {
	private CarGen car;
	/**
	 * Creates a CarEvent
	 * @param car
	 * 		Generic car of the event		
	 */
	CarEvent(CarGen car) {
		this.car = car;
	}
	/**
	 * CarEvent Getter	
	 * @return
	 * 		Car in car event
	*/
	public CarGen getCar() {
		return car;
	}
	/**
	 * Overidden generic execute event.  Sends car to its logic decision making for updates in runtime
	 * operations.  Doesnt return anything, but readds car event to simulator IF hasnt reached destination
	 * @param simulator
	 * 		Abstract simulator that this car is returned to
	 */
	@Override
	public void execute(AbstractSimulator simulator) {
		//has it reached destination
		if(car.getIsInSystem()) {
			if(car.getCurrentNode().equals(car.getDestinationNode()))
				RuntimeOperations.exitSystem(car);
		//means that its not at destination lets determine the type of intersection then make our decision of what happens
			else if(car.getCurrentNode().getIsLightIntersection() == false && car.getTimeAtFront() > 5) {
				RuntimeOperations.setRestOfQueueNoMove(car);
				RuntimeOperations.updateCarInfo(car);
			}
			else if(car.getCurrentNode().getIsLightIntersection() == true && car.getTimeInCurrent() >75 && car.getCurrentQueue().getQueue().peek().equals(this) && ( car.getQueueType() == "north" || car.getQueueType()== "south" ) && car.getCurrentNode().getCurrentState() == Node.Rotation.NORTHSOUTH) {
				RuntimeOperations.setRestOfQueueNoMove(car);
				RuntimeOperations.updateCarInfo(car);
			}
			else if(car.getCurrentNode().getIsLightIntersection() == true && car.getTimeInCurrent() >75 && car.getCurrentQueue().getQueue().peek().equals(this)&& ( car.getQueueType() == "east" || car.getQueueType()== "west" ) && car.getCurrentNode().getCurrentState() == Node.Rotation.EASTWEST) {
				RuntimeOperations.setRestOfQueueNoMove(car);
				RuntimeOperations.updateCarInfo(car);
			}

			else 
				if(car.getCurrentNode().getType().equals("latRoad") || car.getCurrentNode().getType().equals("ladRoad")) {
					RuntimeOperations.roadOperations(car);
				}
				else {  //must be an intersection
				//which type of intersection?
					if(car.getCurrentNode().getIsLightIntersection() == true) 
						RuntimeOperations.lightIntersectionOperation(car);
					else 
						RuntimeOperations.signIntersectionOperation(car);
				
				}
			//Cleaning nodes of any null from queue
			//Important to maintain system integrity
			Node node = car.getCurrentNode();
			if(node.getType().equals("intersection")) {
				node.getNorthQueue().getQueue().remove(null);
				node.getSouthQueue().getQueue().remove(null);
				node.getEastQueue().getQueue().remove(null);
				node.getWestQueue().getQueue().remove(null);
			}
			else if(node.getType().equals("ladRoad")) {
				node.getNorthQueue().getQueue().remove(null);
				node.getSouthQueue().getQueue().remove(null);
			}
			else {
				node.getEastQueue().getQueue().remove(null);
				node.getWestQueue().getQueue().remove(null);
			}
			node = car.getNextNode();
			if(node != null) {
				if(node.getType().equals("intersection")) {
					node.getNorthQueue().getQueue().remove(null);
					node.getSouthQueue().getQueue().remove(null);
					node.getEastQueue().getQueue().remove(null);
					node.getWestQueue().getQueue().remove(null);
				}
				else if(node.getType().equals("ladRoad")) {
					node.getNorthQueue().getQueue().remove(null);
					node.getSouthQueue().getQueue().remove(null);
				}
				else {
					node.getEastQueue().getQueue().remove(null);
					node.getWestQueue().getQueue().remove(null);
				}
			}
			node = car.getPreviousNode();
			if(node != null) {
				if(node.getType().equals("intersection")) {
					node.getNorthQueue().getQueue().remove(null);
					node.getSouthQueue().getQueue().remove(null);
					node.getEastQueue().getQueue().remove(null);
					node.getWestQueue().getQueue().remove(null);
				}
				else if(node.getType().equals("ladRoad")) {
					node.getNorthQueue().getQueue().remove(null);
					node.getSouthQueue().getQueue().remove(null);
				}
				else {
					node.getEastQueue().getQueue().remove(null);
					node.getWestQueue().getQueue().remove(null);
				}
			}
			
			//Add car event back into simulator
			simulator.carInsert(this);
		}
	}
	

}
