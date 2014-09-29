package ec.app.trafficSim.sim.core;

/**
 * Abstract event, inheritable execute method
 * @author Adam Wechter
 */
public abstract class AbstractEvent {
	/**
	 * abstract event execute, defined in child
	 * @param simulator
	 * 		simulator to which the event is re added to		
	 */
	abstract void execute(AbstractSimulator simulator);

}
