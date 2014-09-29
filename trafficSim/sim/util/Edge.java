package ec.app.trafficSim.sim.util;
/**
 * Edge file of edges connecting nodes
 *@author
 *		Adam Wechter
 */
public class Edge {
	int distance;
	Node node1;
	Node node2;
	
	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
		distance = 1;
	}
	
	public int getDistance() {
		return distance;
	}
	public Node getNode1() {
		return node1;
	}
	public Node getNode2() {
		return node2;
	}
}
