package io;

import java.io.File;

import com.beust.jcommander.*; 

public class Parameters {
	@Parameter(names = "-node", description = "Node csv file contianing nodes of the grid")
	private String nodeFile;
	
	@Parameter(names = "-edge", description = "Edge csv file containing connections between nodes")
	private String edgeFile;
	
	@Parameter(names = "-cars", description = "Car csv file of cars, starts, and dests, and types")
	private String carsFile;
	
	@Parameter(names = "-chrom", description = "")
	private String chromFile;
	
	public String getNodeFile() {
		return nodeFile;
	}
	
	public String getEdgeFile() {
		return edgeFile;
	}
	
	public String getCarsFile() {
		return carsFile;
	}
	
	public String getChromFile() {
		return chromFile;
	}
	
	
}
