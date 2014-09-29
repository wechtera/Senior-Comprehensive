package ec.app.trafficSim.sim.util;
import java.util.ArrayList;
/**
 * Graph composed of nodes and Edges
 *@author
 *		Adam Wechter
 */
public class Graph {
	private ArrayList<Node> V;  //vertices (nodes)
	private ArrayList<Edge> E;  //Edges (weights/distances)
	/**
	 *  Both parameters refer to number of intersections in the grid
	 * @param height
	 * @param width
	 * @param length  basically distance of roads
	 */
	public Graph(int height, int width, int length, ArrayList<Boolean> nodeIsI, ArrayList<String> nodeId, ArrayList<Integer> nodeSpeedLimit, ArrayList<String> nodeType, ArrayList<String> node1Id, ArrayList<String> node2Id) {
		V = new ArrayList<Node>(((width + width-1) * (width+width-1))); 
		E = new ArrayList<Edge>((((height+1)*(width)) + ((height+1) * (width)) * 2)); 
		
		
		
		int widthCount = 0;
		boolean isVertical = false;
		
		for(int i = 0; i<nodeIsI.size(); i++) {
			int  thisDistance;
			if(nodeType.get(i).equals("intersection"))
				thisDistance = 0;
			else
				thisDistance = length;
			
			//begin building
			Node n = new Node(nodeType.get(i), nodeId.get(i), thisDistance, nodeSpeedLimit.get(i), i);
			V.add(n);
			
			//logic for laderal vs lateral
			if(isVertical == false && widthCount == width) { //so end of intersections should go to vertical roads
				isVertical = true;
				widthCount = 0;
			}
			else if(isVertical == true && widthCount == width) {
				isVertical = false;
				widthCount = 0;
			}
			//ADDING NODES SHOULD BE DONE NOW
		}
		
		//Create and add edges to the chart
		for(int i = 0; i<node1Id.size(); i++) {
			Node node1 = getCorrespondingNode(node1Id.get(i));
			Node node2 = getCorrespondingNode(node2Id.get(i));;
			E.add(new Edge(node1, node2));
		}
	}
	/**
	 * @return V
	 *		ArrayList of all nodes
	 */
	public ArrayList<Node> getV() {
		return V;
	}
	/**
	 * @return E
	 *		ArrayList of all edges
	 */
	public ArrayList<Edge> getE() {
		return E;
	}
	/**
	 * @param nodeId
	 *		Gets specific node
	 */
	public Node getCorrespondingNode(String nodeId) {
		for(Node n : V) {
			if(n.getId().equals(nodeId))
				return n;
		}
		System.out.println("NODE NOT FOUND:   " + nodeId);
		return null;
	}

	
	

}
