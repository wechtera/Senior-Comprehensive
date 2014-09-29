package ec.app.trafficSim.sim.util;

import java.util.ArrayList;

/**
 * Map used to put everything together allows further customization
 *@author
 *		Adam Wechter
 */
public class TrafficMap {
	private Graph map;
	public TrafficMap(int height, int width, int length, ArrayList<Boolean> nodeIsI, ArrayList<String> nodeId, ArrayList<Integer> nodeSpeedLimit, ArrayList<String> nodeType, ArrayList<String> node1Id, ArrayList<String> node2Id) {
		map = new Graph(height, width, length, nodeIsI, nodeId, nodeSpeedLimit, nodeType, node1Id, node2Id);
	}
	
	public Graph getMap() {
		return map;
	}
}
