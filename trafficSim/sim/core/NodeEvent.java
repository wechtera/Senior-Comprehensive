package ec.app.trafficSim.sim.core;
import ec.app.trafficSim.sim.util.*;
/**
 * 	Node event, contains each node, used to transition light
 * @author
 * 		Adam Wechter
 */
public class NodeEvent extends Event {
	Node node;
	NodeEvent(Node node) {
		this.node = node;
	}
	/**
	 * Inherited execute is in charge of changing light rotation and adding time
	 * Always return to simulator
	 * @param simulator
	 * 		Simulator to be readded to
	 * @return
	 * 		
	 */
	@Override
	void execute(AbstractSimulator simulator) {
		if(node.getIsLightIntersection()) {
			Node.Rotation currRotation = node.getCurrentState();
			node.setTimeInCurrentRotation(node.getTimeInCurrentRotation() + 1);
			if(currRotation == Node.Rotation.NORTHSOUTH) {
				if(node.getTimeInCurrentRotation() == node.getNorthSouthTime()) {
					node.changeRotationState();
					node.setTimeInCurrentRotation(0);
				}
			}
			else if (currRotation == Node.Rotation.EASTWEST) {
				if(node.getTimeInCurrentRotation() == node.getEastWestTime()) {
					node.changeRotationState();
					node.setTimeInCurrentRotation(0);
				}
			}
			else if(currRotation == Node.Rotation.TRANSITION) {
				if(node.getTimeInCurrentRotation() == node.getTransitionTime()) {
					node.changeRotationState();
					node.setTimeInCurrentRotation(0);
				}
			}
		}
		
		
		
		
		
		simulator.nodeInsert(this);

	}
	public Node getNode() {
		return node;
	}
}
