package ec.app.trafficSim.sim.util;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Car object, not called car just because I thought I may have an inheritable object of cars
 * @author Adam Wechter
 */

public class CarGen extends CarObj {
	
	private Node startNode;
	private Node currentNode;
	private Node destinationNode;
	private Node nextNode;
	private Node previousNode;
	private String carMovement;
	//for stop signs
	private int priority;
	private boolean canGo;
	
	private double totalTime;
	
	private double timeInCurrent;
	private double timeAtFront;  //stop signs
	private double delayTime;
	private double greenGas;
	private double averageTimeConsumption;
	private double totalDistance;
	private String carId;
	
	private boolean isInSystem;
	
	private boolean isFirstRotation;
	
	private String nodeType;
	private DirectionalQueue currentQueue;
	
	private Queue<Node> directions;  //directions

	
	public CarGen(String startNode, String destinationNode, double greenGas, double averageTimeConsumption, String carId, TrafficMap map) {
		this.startNode = getNodeStartNode(startNode, map);
		this.destinationNode = getNodeDestinationNode(destinationNode, map);
		this.carId = carId;
		
		currentNode = this.startNode;
		
		totalTime = 0;
		timeInCurrent = 0;
		timeAtFront = 0;
		delayTime = 0;
		this.averageTimeConsumption = averageTimeConsumption;
		
		isFirstRotation = true;
		isInSystem = true;
		this.greenGas = greenGas;
		
		priority = -1;
		canGo = true;
		nodeType = currentNode.getType();
		currentQueue = null;   //initially nothing but then we'll do it
		
		
		directions = getDirections(map.getMap(), this.startNode.getIntId(), this.destinationNode.getIntId());
		
		setTotalDistance();
		nextNode = directions.peek();
		previousNode = currentNode;
	}
	/**
	 * Goal:  Find what directionalqueue this car is in
	 * Procedure:  get all queue directions, search, if find car, return that direction, else search others
	 * @return 
	 * 		Queue which the car is in (lane)
	 */
	private DirectionalQueue getQueue() {
		//get all the queues
		DirectionalQueue north = currentNode.getNorthQueue(); 
		DirectionalQueue south = currentNode.getSouthQueue();
		DirectionalQueue east =  currentNode.getEastQueue();
		DirectionalQueue west =  currentNode.getWestQueue();


		if(!currentNode.getType().equals("latRoad")) {
			Queue<CarGen> northQueue = north.getQueue();
			for(CarObj c : northQueue) {
				if(c.equals(this))
					return north;
			}
			Queue<CarGen> southQueue = south.getQueue();
			for(CarObj c : southQueue) {
				if(c.equals(this))
					return south;
			} 
		}
		Queue<CarGen> eastQueue = east.getQueue();
		for(CarObj c : eastQueue) {
			if(c.equals(this))
				return east;
		}
		Queue<CarGen> westQueue = west.getQueue();
		for(CarObj c : westQueue) {
			if(c.equals(this))
				return west;
		}
		
		//IT WILL NEVER GET TO THIS POINT
		return null;
	}
	/**
	 * @return startNode
	 * 		Cars start node
	 */
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * @param startNode
	 * 		sets startNode as node
	 */
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}
	/**
	 * @return currentNode
	 * 		Cars current node
	 */
	public Node getCurrentNode() {
		return currentNode;
	}

	/**
	 * 	Sets current node based on directions
	 */
	public void setCurrentNode() {
		currentNode = directions.peek();
	}
		/**
	 * @return destinationNode
	 * 		Car's destination node
	 */
	public Node getDestinationNode() {
		return destinationNode;
	}

	/**
	 * @param destinationNode
	 * 		Sets destination Node as Node
	 */
	public void setDestinationNode(Node destinationNode) {
		this.destinationNode = destinationNode;
	}

	/**
	 * @return nextNode
	 * 		cars Next Node	
	 */
	public Node getNextNode() {
		return nextNode;
	}

	/**
	 * This will automatically increment the queue too!!!!
	 * VERY IMPORTANT
	 * Remember this increments queue
	 */
	public void setNextNode() {
		directions.remove();
		nextNode = directions.peek();
	}

	/**
	 * @return totalTime
	 * 		total time in system	
	 */
	public double getTotalTime() {
		return totalTime;
	}

	/**
	 * @param totalTime
	 * 		double how long in system
	 */
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}


	/**
	 * @return greenGas
	 * 		Green house gas emissions 	
	 */
	public double getGreenGas() {
		return greenGas;
	}

	/**
	 * @return carId
	 * 		Id of car
	 */
	public String getCarId() {
		return carId;
	}


	/**
	 * @return nodeType
	 * 		type of currentNode
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 	Updates what type of node
	 */
	public void setNodeType() {
		nodeType = currentNode.getType();
	}

	/**
	 * 		previous node automaticlally set as current node
	 */
	public void setPreviousNode() {
		previousNode = currentNode;
	}

	/**
	 * @return previousNode
	 * 		the previous node
	 */
	public Node getPreviousNode() {
		return previousNode;
	}

	/**
	 * @return timeInCurrent
	 * 		how long car has been in current node
	 */
	public double getTimeInCurrent() {
		return timeInCurrent;
	}

	/**
	 * @param timeInCurrent
	 * 		double set how long its been in current queue
	 */
	public void setTimeInCurrent(double timeInCurrent) {
		this.timeInCurrent = timeInCurrent;
	}

	/**
	 * @return timeAtFront
	 * 		time at the front of the queue
	 */
	public double getTimeAtFront() {
		return timeAtFront;
	}

	/**
	 * @param timeAtFont
	 * 		Set how long its been at front
	 */
	public void setTimeAtFront(double timeAtFront) {
		this.timeAtFront = timeAtFront;
	}

	/**
	 * @return
	 * 		String north, south, east, or west
	 */
	public String getQueueType() {
		return currentQueue.getDirection();
	}

	/**
	 * @return currentQueue
	 * 		current directional queue
	 */
	public DirectionalQueue getCurrentQueue() {
		return currentQueue;
	}

	/**
	 * @return directions
	 * 		the directions queue of this car
	 */
	public Queue<Node> getDirections() {
		return directions;
	}

	/**
	 * @return carMovement
	 * 		which direction the car is moving next
	 */
	public String getCarMovement() {
		return carMovement;
	}

	/**
	 * @return isFirstRotation
	 * 		boolean true if first rotation in system
	 */
	public boolean getIsFirstRotation() {
		return isFirstRotation;
	}

	/**
	 * @return  isInSystem
	 * 		boolean true if in system
	 */
	public boolean getIsInSystem() {
		return isInSystem;
	}

	/**
	 * @param isInSystem
	 * 		Set false once leaves
	 */
	public void setIsInSystem(boolean isInSystem) {
		this.isInSystem = isInSystem;
	}


	/**
	 * only used once that matters (used every time but more explanation in @SEE RuntimeOperations.updateCarInfo
	 * automatically changes firstRotation to False, because initialization changes it to true and only time it should be true
	 */
	public void setIsFirstRotation() {  
		isFirstRotation = false;
	}

	/**
	 * @param priority
	 * 		Intersections
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return priority
	 * 		Priority at stop signs
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * @param canGo
	 * 		Boolean can car go
	 */
	public void setCanGo(boolean canGo) {
		this.canGo = canGo;
	}

	/**
	 * @return canGo
	 * 		boolean, true if can move that rotation
	 */
	public boolean getCanGo() {
		return canGo;
	}

	/**
	 * @return delayTime
	 * 		double how long at intersections
	 */
	public double getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime
	 * 		Set delay time
	 */
	public void setDelayTime(double delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * @return averageTimeConsumption
	 * 		double gas consumed per tick
	 */
	public double getAverageTimeConsumption() {
		return averageTimeConsumption;
	}

	/**
	 * @return totalDistance
	 * 		double total distance covered
	 */
	public double getTotalDistance() {
		return totalDistance;
	}

	/**
	 * used for initial declartion of car.  Since it is straight from raw input data, we get a string
	 * to reference the starting node.  This method provides the mean to transform that node into an 
	 * actual node
	 * @param nodeId
	 * 		String representation of start node
	 * @param map
	 * 		We need some reference
	 * @return
	 * 		The Node with the String Id given
	 */
	public Node getNodeStartNode(String nodeId, TrafficMap map) {
		ArrayList<Node> nodes = map.getMap().getV();
		for(Node c : nodes) {
			if(c.getId().equals(nodeId))
				return c;
		}
		return null;  //if no node found returns empty should throw error
	}

	/**
	 * See getNodeStartNode Description
	 * @param nodeId
	 * @param map
	 * @return
	 * 		Node associated with String Id given in the map
	 */
	public Node getNodeDestinationNode(String nodeId, TrafficMap map) {
		ArrayList<Node> nodes = map.getMap().getV();
		for(Node c : nodes)
			if(c.getId().equals(nodeId))
				return c;
		return null;
		//TODO: throw if returns null cause its an error
	}
	/**
	 * This makes our directions using simple BFS to find the Shortest path
	 * @param map
	 * @param startNode
	 * @param endNode
	 * @return
	 * 		Returns a queue of the directions for this car from Start node to Finish
	 */
	private Queue<Node> getDirections(Graph map, int startNode, int endNode) {
		Queue<Node> directs = new LinkedList<Node>();
		Queue<Integer> q = new LinkedList<Integer>();
		boolean [] visited = new boolean[(map.getV().size())];
		String [] path = new String[map.getV().size()];
		
		q.add(startNode);
		path[startNode] = startNode + " ";
		while(q.peek() != null) {
			if(doBFS(q.poll(), endNode, visited, q, path, map))
				break;
		}
		
		String s = path[endNode];
		Scanner scan = new Scanner(s);
		while(scan.hasNextInt()) 
			directs.add(getNode(scan.nextInt(), map));
		return directs;
	}

	/**
	 * Supporting method for BFS search
	 * @param start
	 * @param end
	 * @param visited
	 * @param q
	 * @param path
	 * @param map
	 * @return
	 * 		Boolean true if the destination node was found so we know which index to use to find the string
	 */
	private boolean doBFS(int start, int end, boolean [] visited, Queue<Integer> q, String [] path, Graph map) {
		if(visited[start]);
		else if(start == end)
			return true;
		else {
			visited[start] = true;
			ArrayList <Node> connected = getNode(start, map).getConnections(map);
			for(Node n : connected) {
				int nextVert = n.getIntId();
				path[nextVert] = path[start] + nextVert + " ";
				q.add(nextVert);
			}
		}
		return false;
	}
 	
	/**
	 * Simple toString of the directions of each car
	 * Used for debugging
	 * @return
	 * 		Directions given as node id's from start to end
	 */
	public String directionsToString() {
		String str = "";
		StringBuilder sb = new StringBuilder(str);
		Queue<Node> temp = new LinkedList<Node>();
		temp.addAll(directions);

		while(!temp.isEmpty()) {
			sb.append(temp.remove().getId() + ",\n");
		}
		return sb.toString();
	}
	/**
 	 * used to update next turn type really needed only in intersections
 	 */
	public void setCarMovement () {
		int currentRow = getRow(currentNode.getId());
		int currentColumn = getColumn(currentNode.getId());
		if(nextNode == null)
			return;
		int nextRow = getRow(nextNode.getId());
		int nextColumn = getColumn(nextNode.getId());
		String currentQueueType = currentQueue.getDirection();

		if(currentQueueType.equals("NORTH")) {
			if(nextRow != currentRow)
				carMovement = "STRAIGHT";
			else if(nextColumn > currentColumn)
				carMovement =  "RIGHT";
			else
				carMovement =  "LEFT";
		}
		else if(currentQueueType.equals("SOUTH")) {
			if(nextRow != currentRow)
				carMovement =  "STRAIGHT";
			else if(nextColumn > currentColumn)
				carMovement =  "LEFT";
			else
				carMovement =  "RIGHT";
		}
		else if(currentQueueType.equals("EAST")) {
			if(nextColumn != currentColumn)
				carMovement =  "STRAIGHT";
			else if(nextRow > currentRow)
				carMovement =  "RIGHT";
			else
				carMovement =  "LEFT";
		}
		else {
			if(nextColumn != currentColumn)
				carMovement =  "STRAIGHT";
			else if(nextRow > currentRow)
				carMovement =  "RIGHT";
			else
				carMovement =  "LEFT";
		}		
	}
	
	/**
	 * Disects a node Id String for the row number
	 * @param id
	 * 		Some NOde Id
	 * @return
	 * 		The row that this node is in
	 */
	private static int getRow(String id) {
		int thisRow = 0;
		char x = 'x';
		int i = 2;
		int multValue = 1;
		while(id.charAt(i) != x) { //get index of row value
			i++;
		}
		i--;
		while(i>1) {
			thisRow += Character.getNumericValue(id.charAt(i)) * multValue;
			i--;
			multValue*=10;
		}
		return thisRow;
	}
	/**
	 * Disects a Node Id string for the column
	 * @param id
	 * 		String of Node Id
	 * @return
	 * 		The column the node is in
	 */
	private static int getColumn(String id) {
		int thisColumn = 0;
		char x = 'x';
		int i = 2;
		int multValue = 1;
		while(id.charAt(i) != x) { //get index of row value
			i++;
		}
		//i now holds end of row (on the x)
		int j = id.length() -1;
		while(j>i) {
			thisColumn += Character.getNumericValue(id.charAt(j)) * multValue;
			j--;
			multValue*=10;
		}
		return thisColumn;
	}
	/**
	 * Used for changing from the number format to the actual nodes for directions
	 * @param intId
	 * 		Int id of node
	 * @param map
	 * 		Map for reference
	 * @return
	 * 		The node associated with the Int Id
	 */
	private static Node getNode(int intId, Graph map) {
		Node node = null;
		ArrayList <Node> nodes = map.getV();
		
		for(Node n : nodes) {
			if(n.getIntId() == intId)
				node = n;
		}
		
		return node;
	}
	/**
	 * See where we are and what the next node is
	 * 
	 * using the idea that we know what direction we are aiming towards determine which direction(north south east west) will get us there
	 */
	public void updateCurrentQueue(boolean isFirst) {
		setCurrentType();
		int previousRow = getRow(previousNode.getId());
		int previousColumn = getColumn(previousNode.getId());
		int currentRow = getRow(currentNode.getId());
		int currentColumn = getColumn(currentNode.getId());
		if(nextNode == null) {
			return;
		}
		int nextRow = getRow(nextNode.getId());
		int nextColumn = getColumn(nextNode.getId());
		//if currently in a queue we should remove it(if its the first we dont need to worry
		if( !isFirst) {
			currentQueue.getQueue().remove(this);  //We should not get here unless it is at front but to ensure we only remove this, we may want to relook
		}


		
		if(nodeType.equals("latRoad")) { //east west queues
			if(currentColumn < nextColumn) //moving eastward
				currentNode.getEastQueue().getQueue().add(this);
			else  //moving from westward
				currentNode.getWestQueue().getQueue().add(this);
		}
		else if(nodeType.equals("ladRoad")) {
			if(currentRow < nextRow)  //moving southward
				currentNode.getSouthQueue().getQueue().add(this);
			else  //moving northward
				currentNode.getNorthQueue().getQueue().add(this);
		}
		else {  //at an intersection
			if(previousRow == currentRow) {  //definately east west queue
				if(currentColumn < previousColumn)
					currentNode.getWestQueue().getQueue().add(this);
				else {
					currentNode.getEastQueue().getQueue().add(this);
				}
			}
			else { // definatel north or south
				if(currentRow < previousRow) 
					currentNode.getNorthQueue().getQueue().add(this);
				else
					currentNode.getNorthQueue().getQueue().add(this);
					
			}
		}
		currentQueue = getQueue();
	}
	
	/**
	 * Sets total distance car must travel based on directions 	
	 */
	private void setTotalDistance() {
		double totalDistance = 0;
		Queue<Node> directs = new LinkedList<Node>();
		directs.addAll(directions);
		while(directs.peek() != null)
		{
			Node n = directs.poll();
			totalDistance += n.getDistance();
		}
		this.totalDistance = totalDistance;
	}
	public void setCurrentType() {
		nodeType = currentNode.getType();
	}
	

}
