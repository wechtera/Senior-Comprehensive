package ec.app.trafficSim.sim.util;

import java.util.ArrayList;
/**
 * Node file contains directional quues too
 *@author
 *		Adam Wechter
 */
public class Node {
	//general
	private String type;
	private String id;
	private int intId;
	private DirectionalQueue north;
	private DirectionalQueue south;
	private DirectionalQueue east;
	private DirectionalQueue west;
	
	
	//for roads
	double distance = 0;  //equal to seconds
	double speedLimit = 0;
	
	//for intersections
	public static enum Rotation {
		NORTHSOUTH, EASTWEST, TRANSITION;
	}
	private double northSouthTime;
	private double eastWestTime;
	private double timeInCurrentLight;  //is used in nodeEvent (ECLIPS LIEZ)
	private Rotation previousState;
	private Rotation currentState;
	private boolean isLightIntersection;
	
	private double timeInCurrentRotation;
	private final double TRANSITIONTIME = 2; //2 seconds per transition period
	

	/**
	 *Basic constructor, requires type of node, id, distance assoc.
	 *and an int id determined by io class
	 */
	public Node(String type, String id, int distance, int speedLimit, int intId) {
		this.type = type;
		this.id = id;
		this.intId = intId;
		
		if(type.equals("intersection")) {
			north = new DirectionalQueue("NORTH");
			south = new DirectionalQueue("SOUTH");
			east = new DirectionalQueue("EAST");
			west = new DirectionalQueue("WEST");
			this.distance = 2;
		}
		else if(type.equals("latRoad")) {
			east = new DirectionalQueue("EAST");
			west = new DirectionalQueue("WEST");
			this.speedLimit = speedLimit;
			this.distance = distance;
		}
		else if(type.equals("ladRoad")) {  //must be up and down roads (north / south)
			north = new DirectionalQueue("NORTH");
			south = new DirectionalQueue("SOUTH");
			this.speedLimit = speedLimit;
			this.distance = distance;
		}
	}
	
	public DirectionalQueue getNorthQueue() {
		return north;
	}
	public DirectionalQueue getSouthQueue() {
		return south;
	}
	public DirectionalQueue getEastQueue() {
		return east;
	}
	public DirectionalQueue getWestQueue() {
		return west;
	}	
	public void setNorthQueue(DirectionalQueue north) {
		this.north = north;
	}
	public void setSouthQueue(DirectionalQueue south) {
		this.south = south;
	}
	public void setEastQueue(DirectionalQueue east) {
		this.east = east;
	}
	public void setWestQueue(DirectionalQueue west) {
		this.west = west;
	}
	public String getType() {
		return type;
	}
	public String getId() {
		return id;
	}
	public double getDistance() {
		return distance;
	}
	public double getSpeedLimit() {
		return speedLimit;
	}
	public Rotation getCurrentState() {
		return currentState;
	}
	public void setNorthSouthTime(double northSouthTime) {
		this.northSouthTime = northSouthTime;
	}
	public void setEastWestTime(double eastWestTime) {
		this.eastWestTime = eastWestTime;
	}
	public void setTimeInCurrentRotation(double timeInCurrentRotation) {
		this.timeInCurrentRotation = timeInCurrentRotation;
	}
	public double getNorthSouthTime() {
		return northSouthTime;
	}
	public double getEastWestTime() {
		return eastWestTime;
	}
	public double getTimeInCurrentRotation() {
		return timeInCurrentRotation;
	}
	public double getTransitionTime () {
		return TRANSITIONTIME;
	}
	public void setIsLightIntersection(boolean isLightIntersection) {
		this.isLightIntersection = isLightIntersection;
	}
	public int getIntId() {
		return intId;
	}
	/**
	 * This is for the initial light settings
	 * @param isNorthSouthFirst
	 */
	public void setCurrentState(boolean isNorthSouthFirst) {
		if(isNorthSouthFirst == true) {
			currentState = Rotation.NORTHSOUTH;
		}
		else {
			currentState = Rotation.EASTWEST;
		}
	}
	public boolean getIsLightIntersection() {
		return isLightIntersection;
	}
	/**
	 * Takes into account the previous rotation and sets the new rotation.  Also stores the current
	 * rotation state at the time this method is called and stores it inprevious after next state decision is made
	 */
	public void changeRotationState() {
		if(currentState == Rotation.NORTHSOUTH || currentState == Rotation.EASTWEST) {
			previousState = currentState;
			currentState = Rotation.TRANSITION;
		}
		else { //current state = trasition
			if(previousState == Rotation.NORTHSOUTH) {
				previousState = currentState;
				currentState = Rotation.EASTWEST;
			}
			else {  //previousState was EAST WEST
				previousState = currentState;
				currentState = Rotation.NORTHSOUTH;
			}			
		}
	}

	/**
	 * Gets any nodes adgacent to this node
	 * @param map
	 * 		we need some reference
	 * @return
	 * 		array list of any connecting nodes to this node
	 */
	public ArrayList<Node> getConnections(Graph map) {
		ArrayList<Edge> edges = map.getE();
		ArrayList<Node> children = new ArrayList<Node>();
		for(Edge e : edges) {
			if(e.getNode1().getId().equals(id)) {				
				children.add(e.getNode2());
				
			}
		}
		return children;
	}
}
