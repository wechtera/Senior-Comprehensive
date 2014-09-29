package ec.app.trafficSim.sim.core;

import java.util.LinkedList;
import java.util.Queue;
/**
 * Abstract Simulator, important to note any events need queues defined here
 * Insert methods add events back into simulator
 * @author Adam Wechter		
 */
public class AbstractSimulator {
	Queue<CarEvent> carQueue = new LinkedList<CarEvent>();
	Queue<NodeEvent> nodeQueue = new LinkedList<NodeEvent>();
/**
 * Adds car events into simulator (cast)
 * @param e
 * 		Abstract event cast into car event
 */
    void carInsert(AbstractEvent e) {
        carQueue.add((CarEvent)e);
    }
/**
 * Adds node event into simulator (cast) 
 * @param e
 * 		Abstract event cast into Node event
 */    
    void nodeInsert(AbstractEvent e) {
    	nodeQueue.add((NodeEvent) e);
    }
}
