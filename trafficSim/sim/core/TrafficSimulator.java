package ec.app.trafficSim.sim.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import ec.app.trafficSim.sim.io.ECJPlug;
import ec.app.trafficSim.sim.util.*;

/**
 * Traffic simulator that does car and queue events
 * @author
 * 		Adam Wechter
 */
public class TrafficSimulator extends Simulator {
	/**
	 * Initiate puts the nodes and cars into car events and node events and places them in the simulator queue
	 * It also executes all events
	 * @param nodes
	 * 		List of nodes for node events
	 * @param cars
	 *		List of cars for car events
	 */
	public void initiate(ArrayList<Node> nodes, ArrayList<CarGen> cars) {

		for(CarGen car : cars) {
			carQueue.add(new CarEvent(car));
		}
		for(Node node : nodes) {
			nodeQueue.add(new NodeEvent(node));
		}
		doRotation(carQueue);

		while(!carQueue.isEmpty()) {
			if(carQueue.size() < 100)
				for(CarEvent c : carQueue) 
					if(c.getCar().getCurrentQueue().getQueue().peek() != null && carQueue.contains(c.getCar().getCurrentQueue().getQueue().peek())) //if carqueue doesnt have the first car of a directional queue
						RuntimeOperations.exitSystem(c.getCar().getCurrentQueue().getQueue().poll());
		
			doRotation(carQueue);
			doRotation(nodeQueue);
			for(CarEvent cE : carQueue) {
				cE.getCar().setCanGo(true);	
			}
			

		}
	}

}
