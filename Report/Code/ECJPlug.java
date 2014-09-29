package ec.app.trafficSim.sim.io;



import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.CSVReader;

//import ec.lib.au.com.bytecode.opencsv.CSVReader;
import java.io.Reader;

import ec.app.trafficSim.sim.util.Edge;
import ec.app.trafficSim.sim.util.TrafficMap;
import ec.app.trafficSim.sim.util.CarGen;
import ec.app.trafficSim.sim.util.Graph;
import ec.app.trafficSim.sim.util.Node;
import ec.app.trafficSim.sim.core.RuntimeOperations;
import ec.app.trafficSim.sim.core.TrafficSimulator;

@SuppressWarnings("unused")

/**
 * In charge of reading in information and acting as a proxy between the ECJ optimizer
 * And the simulator.  Also contains the fitness evaluator
 * @author
 * 		Adam Wechter
 */
public class ECJPlug {
	
	//These are record keeping lists. used when recording information from exitting cars
	public static List<Double> timeTransit;
	public static List<Double> milesPerGallon;
	public static List<Double> totalDelay;
	public static List<Double> emissions;
	public static List<CarGen> cars;
		/**
		 * Method is important to read in files and intiate both record keeping information and simulator
		 * @param nodeFile
		 * 		Contains node information
		 * @param edgeFile
		 * 		Contains connections of nodes
		 * @param carsFile
		 * 		Contains all car information
		 * @param modifier1
		 * 		Weight of modifier1(Timetransit) (0-1)
		 * @param modifier2
		 * 		Weight of modifier2(timeDelay) (0-1)
		 * @param modifier3
		 * 		Weight of modifier3(mpg) (0-1)
		 * @param timingsFile
		 * 		Light configuration in binary
		 * @return
		 * 		Float fitness for the ecj to use
		 */
		public static double evaluateFitness(String nodeFile, String edgeFile, String carsFile, double modifier1, double modifier2, double modifier3, String timingsFile) throws FileNotFoundException, IOException {
			int width;
			int height;
			int length;
			
			/*
			 * setting up saving stuff
			 */
			
			timeTransit = new ArrayList<Double>();
			milesPerGallon = new ArrayList<Double>();
			totalDelay = new ArrayList<Double>();
			emissions = new ArrayList<Double>();
			cars = new ArrayList<CarGen>();
			numRepeated = 0;
			lastNumInCars = 0;
			numOfLogs = 0;
			
			
			/**  Nodes **/
			File test = new File(nodeFile);
			System.out.println("File:  " + test.getAbsolutePath());
			CSVReader reader = new CSVReader(new FileReader(nodeFile));
		    String [] nextLine;
		    nextLine = reader.readNext();
	        height = Integer.parseInt(nextLine[0]);
		    width = Integer.parseInt(nextLine[1]);
	        length = Integer.parseInt(nextLine[2]);
		    
	        ArrayList<Boolean> nodeIsI = new ArrayList<Boolean>();
	        ArrayList<String> nodeId = new ArrayList<String>();
	        ArrayList<Integer> nodeSpeedLimit = new ArrayList<Integer>();
	        ArrayList<String> nodeType = new ArrayList<String>();
	        
		    
	        while ((nextLine = reader.readNext()) != null) {
		    	nodeIsI.add(Boolean.parseBoolean(nextLine[0]));
		    	nodeId.add(nextLine[1]);
		    	nodeSpeedLimit.add(Integer.parseInt(nextLine[2]));
		    	nodeType.add(nextLine[3]);
		    }
	        reader.close();
	        
	        
	        /** EDGES **/
	        reader = new CSVReader(new FileReader(edgeFile));
		    ArrayList<String> node1Id = new ArrayList<String>();
		    ArrayList<String> node2Id = new ArrayList<String>();
		    while((nextLine = reader.readNext()) != null) {
		    	node1Id.add(nextLine[0]);
		    	node2Id.add(nextLine[1]);
		    	node1Id.add(nextLine[1]);
		    	node2Id.add(nextLine[0]);
		    }
		   //create the map 
		    TrafficMap trafficMap = new TrafficMap(height, width, length, nodeIsI, nodeId, nodeSpeedLimit, nodeType, node1Id, node2Id);
		    ArrayList<Edge> edges = new ArrayList<Edge>();
		    edges.addAll(trafficMap.getMap().getE());
		    reader.close();
		    
		    /**CARS*/
		    reader = new CSVReader(new FileReader(carsFile));
		    ArrayList<String> startNodes = new ArrayList<String>();
		    ArrayList<String> destNodes = new ArrayList<String>();
		    ArrayList<Double> greenGas = new ArrayList<Double>();
		    ArrayList<String> carId = new ArrayList<String>();
		    ArrayList<Double> averageTimeConsumption = new ArrayList<Double>();
		    while((nextLine = reader.readNext()) != null) {
		    	startNodes.add(nextLine[0]);
		    	destNodes.add(nextLine[1]);
		    	greenGas.add(Double.parseDouble(nextLine[2]));
		    	carId.add(nextLine[3]);
		    	averageTimeConsumption.add(Double.parseDouble(nextLine[4]));
		    }
		    reader.close();
		    //car init
		    ArrayList<CarGen> autos = new ArrayList<CarGen>();
		    for(int i = 0; i<startNodes.size(); i++) {  //may be just a < instead of <=
		    	autos.add(new CarGen(startNodes.get(i), destNodes.get(i), greenGas.get(i), averageTimeConsumption.get(i), carId.get(i), trafficMap));
		    }
			
		    
		    /**    Timings **/
		    ArrayList<Boolean> isLightIntersection = new ArrayList<Boolean>();
		    ArrayList<Integer> timeNorthSouth = new ArrayList<Integer>();
		    ArrayList<Integer> timeEastWest = new ArrayList<Integer>();
		    ArrayList<Boolean> isNorthSouthFirst = new ArrayList<Boolean>();
		   
		    reader = new CSVReader(new FileReader(timingsFile));
		    System.out.println("TIMINGS!!");
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
		    reader.close();

		    Graph g = trafficMap.getMap();
		    ArrayList<Node> nodes = g.getV();
		    int i = 0;
		    for(int k = 0; k < nodes.size(); k++) {
		    	if(nodes.get(k).getType().equals("intersection")) {
		    		nodes.get(k).setIsLightIntersection(isLightIntersection.get(i));
		    		nodes.get(k).setNorthSouthTime((timeNorthSouth.get(i)*15) + 15);
		    		nodes.get(k).setEastWestTime((timeEastWest.get(i)*15) + 15);
		    		nodes.get(k).setCurrentState(isNorthSouthFirst.get(i));
		    		i++;
		    	}
		    }

		    new TrafficSimulator().initiate(nodes, autos);
		    
		    
		    float fitness = 0;
		    fitness = calculateFitness(modifier1, modifier2, modifier3);

			return fitness;
		}
		/**
		 *  Converts binary string (3 digits) into an int
		 * @param str
		 * 		String of 3 binary digits
		 * @return sum
		 * 		value of binary number 
		 */
		private static int intify(String str) {
			int sum = 0;
			char[] chars = str.toCharArray();
			int [] mults = {4,2,1};
			
			for(int i = 0; i<3; i++) {
				sum += mults[i] * Character.getNumericValue(chars[i]);
			}
			return sum;
		}
		/**
		 * Fitness Calculator
		 * Takes record lists and calculates each car's fitness in the system.
		 * this number is the sum of all of them and eventuallly is divided by number of cars to get
		 * fitness of the average car in the system.
		 * @return fitness
		 * 		Fitness value of simulation
		 */
		private static float calculateFitness(double modifier1, double modifier2, double modifier3) {
			
			float fitness = 0;

			for(int i = 0; i < ECJPlug.cars.size(); i++) {
				
				double tt = ECJPlug.timeTransit.get(i);
				double mpg = ECJPlug.milesPerGallon.get(i);  //already calculated
				double td = ECJPlug.totalDelay.get(i);

				
				
				
				CarGen c = ECJPlug.cars.get(i);
				double currDist = c.getTotalDistance();
				double trueTt = 0;
				double trueTd = 0;

				if(currDist < 1) {
					trueTd = td / 1;
					trueTt = tt / 1;
				}
				else {
					trueTt = tt/currDist;
					trueTd = td/currDist;
				}

				
				fitness += ((modifier1 * (trueTt)) * 10000) + ((modifier2 * (trueTd)) * 10000) + ((modifier3*(mpg)) * 10000);	
			}
			fitness = fitness/ECJPlug.cars.size();  
			
			return fitness;
		}

}
