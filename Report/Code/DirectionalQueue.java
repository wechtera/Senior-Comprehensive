package ec.app.trafficSim.sim.util;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Directional queues are queues with directions associated with them.
 * Cars enter and leave toward the direction indicated (car going north will
 * be in a north queue)
 * @author wechtera
 *
 */
public class DirectionalQueue{

	Queue<CarGen> queue;
	String direction;  //cars enter from opposite and leave toward direction
	public DirectionalQueue(String direction) {
		queue = new LinkedList<CarGen>();
		this.direction = direction;
	}
	public DirectionalQueue(String direction, Queue<CarGen> queue) {
		this.queue = queue;
		this.direction = direction;
	}
	/**
	 * @return
	 * String of which direction either north, south, east or west
	 */
	public Queue<CarGen> getQueue() {
		return queue;
	}
	/**
	 * @return
	 * 		Direction the queue exits
	 */
	public String getDirection() {
		return direction;
	}

}
