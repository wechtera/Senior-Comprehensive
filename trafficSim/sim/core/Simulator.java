package ec.app.trafficSim.sim.core;
import java.util.Queue;
/**
 * Abstract Simulator class represents a simulawtor.  Includes a do rotation for 
 * All of the events in some event queue
 * @author
 * 		Adam Wechter
 */
public class Simulator extends AbstractSimulator {
	/**
	 * Rotates through queue and executes each event
	 * @param queue
	 * 		Queue of events
	 */
    void doRotation(Queue queue) {
    	Event e;
    	
    	int queueSize = queue.size();
    	for(int i = 0; i < queueSize; i++) {
    		e = (Event)queue.remove();
    		e.execute(this);
    	}

    }
}
