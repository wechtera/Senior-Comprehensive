package io;
import core.TrafficSimulator;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.*;

import au.com.bytecode.opencsv.*;
import util.Edge;
import util.TrafficMap;
import util.CarGen;
import util.Graph;
import util.Node;

//@SuppressWarnings("unused")
public class CLI {
	public static String record = "";
	public static List<Double> timeTransit = new ArrayList<Double>();
	public static List<Double> milesPerGallon = new ArrayList<Double>();
	public static List<Double> totalDelay = new ArrayList<Double>();
	public static List<Double> emissions = new ArrayList<Double>();
	public static List<CarGen> cars = new ArrayList<CarGen>();
	
	public static void main(String [] args) throws FileNotFoundException, IOException{
		Parameters parameters = new Parameters();
		JCommander j = new JCommander(parameters, args);
		j.usage();
		
		int width;
		int height;
		int length;
		
		
		/**
		 * GET THE NODE FILE HERE AND SAVE IT 
		 */
	    CSVReader reader = new CSVReader(new FileReader(parameters.getNodeFile()));
	    String [] nextLine;
	    //read in width, height, and length
	    nextLine = reader.readNext();
        height = Integer.parseInt(nextLine[0]);
	    width = Integer.parseInt(nextLine[1]);
        length = Integer.parseInt(nextLine[2]);
	    
        ArrayList<Boolean> nodeIsI = new ArrayList<Boolean>();
        ArrayList<String> nodeId = new ArrayList<String>();
        ArrayList<Integer> nodeSpeedLimit = new ArrayList<Integer>();
        ArrayList<String> nodeType = new ArrayList<String>();
        
        System.out.println("\n----------Nodes------------");
	    
        while ((nextLine = reader.readNext()) != null) {
	    	nodeIsI.add(Boolean.parseBoolean(nextLine[0]));
	    	nodeId.add(nextLine[1]);
	    	System.out.println(nextLine[1]);
	    	nodeSpeedLimit.add(Integer.parseInt(nextLine[2]));
	    	nodeType.add(nextLine[3]);
	    }
	    
        reader.close();
	    
	    /**
	     * GET THE EDGE FILE HERE AND SAVE IT
	     */    
	    reader = new CSVReader(new FileReader(parameters.getEdgeFile()));
	    ArrayList<String> node1Id = new ArrayList<String>();
	    ArrayList<String> node2Id = new ArrayList<String>();
	    System.out.println("\n------------Edges-----------");
	    while((nextLine = reader.readNext()) != null) {
	    	System.out.println(nextLine[0] +", " + nextLine[1]);
	    	node1Id.add(nextLine[0]);
	    	node2Id.add(nextLine[1]);
	    	node1Id.add(nextLine[1]);
	    	node2Id.add(nextLine[0]);
	    }
	   //create the map 
	    TrafficMap trafficMap = new TrafficMap(height, width, length, nodeIsI, nodeId, nodeSpeedLimit, nodeType, node1Id, node2Id);
	    ArrayList<Edge> edges = new ArrayList<Edge>();
	    edges.addAll(trafficMap.getMap().getE());
	    System.out.println("---------Edges----------");
	    for(int i = 0; i<edges.size(); i++) {
	    	System.out.println(i+": " + edges.get(i).getNode1().getId() + "  " + edges.get(i).getNode2().getId() );
	    }
	    
	    /**
	     * CAR FILE HERE
	     */
	   // int cars=0;  //keeps track of how many car objects we need
	    
	    reader = new CSVReader(new FileReader(parameters.getCarsFile()));
	    ArrayList<String> startNodes = new ArrayList<String>();
	    ArrayList<String> destNodes = new ArrayList<String>();
	    ArrayList<Double> greenGas = new ArrayList<Double>();
	    ArrayList<Double> averageTimeConsumption = new ArrayList<Double>();
	    ArrayList<String> carId = new ArrayList<String>();
	    while((nextLine = reader.readNext()) != null) {
	    	startNodes.add(nextLine[0]);
	    	destNodes.add(nextLine[1]);
	    	greenGas.add(Double.parseDouble(nextLine[2]));
	    	carId.add(nextLine[3]);
	    	averageTimeConsumption.add(Double.parseDouble(nextLine[4]));
	    	//cars++;
	    }
	    reader.close();
	    

	    
	    ArrayList<CarGen> autos = new ArrayList<CarGen>();
	    for(int i = 0; i<startNodes.size(); i++) {  //may be just a < instead of <=
	    	autos.add(new CarGen(startNodes.get(i), destNodes.get(i), greenGas.get(i), averageTimeConsumption.get(i), carId.get(i), trafficMap));
	    }
	    //cars are now officially in the system we have them as objects, do we need to add them to queues now?  NO because we will
	    //do this in the first step of the event simulator as they move to their next node let simulator logic do this so we dont need to repeat
	    
	    
	    /**
	     * ACTUAL TIMING SCHEDULE HERE
	     */
	    reader = new CSVReader(new FileReader(parameters.getChromFile()));
	    ArrayList<Boolean> isLightIntersection = new ArrayList<Boolean>();
	    ArrayList<Integer> timeNorthSouth = new ArrayList<Integer>();
	    ArrayList<Integer> timeEastWest = new ArrayList<Integer>();
	    ArrayList<Boolean> isNorthSouthFirst = new ArrayList<Boolean>();
	    
	    while((nextLine = reader.readNext()) != null) {
	    	if(nextLine[0].equals("1"))
	    		isLightIntersection.add(true);
	    	else
	    		isLightIntersection.add(false);
	    	timeNorthSouth.add(intify(nextLine[1]));
	    	timeEastWest.add(intify(nextLine[2]));
	    	if(nextLine[3].equals("1"))
	    		isNorthSouthFirst.add(true);
	    	else
	    		isNorthSouthFirst.add(false);
	    }
	    
	    //Setting initial information about intersections
	    Graph g = trafficMap.getMap();
	    ArrayList<Node> nodes = g.getV();
	    int i = 0;
	    for(int k = 0; k < nodes.size(); k++) {
	    	if(nodes.get(k).getType().equals("Intersection")) {
	    		nodes.get(k).setIsLightIntersection(isLightIntersection.get(i));
	    		nodes.get(k).setNorthSouthTime(timeNorthSouth.get(i));
	    		nodes.get(k).setEastWestTime(timeEastWest.get(i));
	    		nodes.get(k).setCurrentState(isNorthSouthFirst.get(i));
	    		i++;
	    	}
	    }
	    reader.close();
	    
	    //we should pass off to some starter method
	    //TODO:  add debug boolean for print stuff 
	    //TODO:  We may need to write thsi to a fiel so file writer
	    for(CarGen car : autos) {
	    	System.out.println(car.directionsToString());
	    }
	    System.out.println("\n\n\n\n\n");
	    
	    new TrafficSimulator().initiate(nodes, autos);
	    
	    System.out.println("Finished");
	    System.out.println(CLI.record);
	    
	}
	
	
	
	private static int intify(String str) {
		int sum = 0;
		char[] chars = str.toCharArray();
		int [] mults = {4,2,1};
		
		for(int i = 0; i<3; i++) {
			sum += mults[i] * Character.getNumericValue(chars[i]);
		}
		return sum;
	}

}
