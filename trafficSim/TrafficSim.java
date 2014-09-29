package ec.app.trafficSim;

import ec.*;
import ec.simple.*;
import ec.vector.*;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import ec.app.trafficSim.sim.io.ECJPlug;
/**
 * Method called by ECJ Evaluate method to solve for the optimal fitness
 *@author
 *		Adam Wechter
 */
public class TrafficSim extends Problem implements SimpleProblemForm {
	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {

		File fnew = new File("/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Timings.txt");
		if(fnew.exists()) {
			System.out.println("DELETING EXISTING TIMINGS FILE");
			fnew.delete();
		}
		if(ind.evaluated)
			return;

		if(!(ind instanceof BitVectorIndividual))
			state.output.fatal("Not a BitVector individual!!!",null);

		double thisFitness = 0;
		BitVectorIndividual ind2 = (BitVectorIndividual)ind;
		ArrayList<int []> timings = new ArrayList<int []>();

		System.out.println(ind2.genotypeToStringForHumans());
		for(int i = 0; i<ind2.genome.length;) {
			int [] temp = new int[8];
			for(int j = 0; j < 8; j++) {
				
				int x;
				if(ind2.genome[i] == true)
					x = 1;
				else
					x = 0;
				
				temp[j] = x;
				i++;
			}
			
			timings.add(temp);
		}
		
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < timings.size(); i++) {
			sb.append(timings.get(i)[0]+","+timings.get(i)[1]);
			sb.append(timings.get(i)[2]);
			sb.append(timings.get(i)[3]+","+timings.get(i)[4]);
			sb.append(timings.get(i)[5]);
			sb.append(timings.get(i)[6]+","+timings.get(i)[7]+"\n");
		}
		try {
			fnew.createNewFile();
			FileWriter writer = new FileWriter(fnew,false);
			BufferedWriter buff = new BufferedWriter(writer);
			System.out.println(sb.toString());
			buff.write(sb.toString());
			buff.close();
			writer.close();
		} catch(IOException e) {
			System.out.println("Problem Writing to Timings");
			System.out.println("File Name: " + fnew.getAbsolutePath());
			e.printStackTrace();
		}

		try {
		thisFitness = ECJPlug.evaluateFitness("/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Node.txt", "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Edge.txt", "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Cars1.txt",.1,.1,.1, "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Timings.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			state.output.fatal("Could Not Find File");
		} catch (IOException e) {
			e.printStackTrace();
			state.output.fatal("Problem with input");
		}
		
		((SimpleFitness)ind2.fitness).setFitness(state, (float)thisFitness, false);  //setfit(evstate, fitness, is it ideal?)
		 ind2.evaluated = true;
		
	}
}
