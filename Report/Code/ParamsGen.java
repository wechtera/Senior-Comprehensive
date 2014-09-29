
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
/**
 * Code to generate TrafficSim.java files and Parameter files
 *@author
 *		Adam Wechter
 */
public class ParamsGen {
	public static void main(String [] args) {
		StringBuilder indexSB = new StringBuilder("");
		StringBuilder paramsSB = new StringBuilder("");
		
		String nodeTxtFile = "ecj/ec/app/trafficSim/sim/files/Nodes";
		String edgeTxtFile = "ecj/ec/app/trafficSim/sim/files/Edge";
		String carTxtFile = "ecj/ec/app/trafficSim/sim/files/Cars";
		String paramsName = "trafficSim";
		String javaName = "TrafficSim";
		String outputStat = "$out"; 
		final String javaTxt = "package ec.app.trafficSim;\nimport ec.*;\nimport ec.simple.*;\nimport ec.vector.*;\nimport java.util.ArrayList;\nimport java.io.File;\nimport java.io.FileNotFoundException;\nimport java.io.FileWriter;\nimport java.io.BufferedWriter;\nimport java.io.IOException;\nimport ec.app.trafficSim.sim.io.ECJPlug;\npublic class ";
		final String javaTxt6 = " extends Problem implements SimpleProblemForm {\npublic void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {\nSystem.out.println(\"\\n\\n----------NEW RUN------------\");\nFile fnew = new File(\"/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Timings.txt\");\nif(fnew.exists()) {\nSystem.out.println(\"DELETING EXISTING TIMINGS FILE\");\nfnew.delete();\n}\nif(ind.evaluated)\nreturn;\nif(!(ind instanceof BitVectorIndividual))\nstate.output.fatal(\"Not a BitVector individual!!!\",null);\ndouble thisFitness = 0;\nBitVectorIndividual ind2 = (BitVectorIndividual)ind;\nArrayList<int []> timings = new ArrayList<int []>();\nSystem.out.println(ind2.genotypeToStringForHumans());\nfor(int i = 0; i<ind2.genome.length;) {\nint [] temp = new int[8];\nfor(int j = 0; j < 8; j++) {\nint x;\nif(ind2.genome[i] == true)\nx = 1;\nelse\nx = 0;\ntemp[j] = x;\ni++;\n}\ntimings.add(temp);\n}\nStringBuilder sb = new StringBuilder(\"\");\nfor(int i = 0; i < timings.size(); i++) {\nsb.append(timings.get(i)[0]+\",\"+timings.get(i)[1]);\nsb.append(timings.get(i)[2]);\nsb.append(timings.get(i)[3]+\",\"+timings.get(i)[4]);\nsb.append(timings.get(i)[5]);\nsb.append(timings.get(i)[6]+\",\"+timings.get(i)[7]+\"\\n\");\n}\ntry {\nfnew.createNewFile();\nFileWriter writer = new FileWriter(fnew,false);\nBufferedWriter buff = new BufferedWriter(writer);\nSystem.out.println(sb.toString());\nbuff.write(sb.toString());\nbuff.close();\nwriter.close();\n} catch(IOException e) {\nSystem.out.println(\"Problem Writing to Timings\");\nSystem.out.println(\"File Name: \" + fnew.getAbsolutePath());\ne.printStackTrace();\n}\ntry {\nthisFitness = ECJPlug.evaluateFitness(\"";  //NodeFile
		final String javaTxt2 = "\", \""; //edge   cars
		final String javaTxt3 = "\", ";   //    modifier1    
		final String javaTxt4 = ", ";     //modifier2    modifier3
		final String javaTxt5 = ", \"/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Timings.txt\");\n} catch (FileNotFoundException e) {\ne.printStackTrace();\nstate.output.fatal(\"Could Not Find File\");\n} catch (IOException e) {\ne.printStackTrace();\nstate.output.fatal(\"Problem with input\");\n}\n((SimpleFitness)ind2.fitness).setFitness(state, (float)thisFitness, false); //setfit(evstate, fitness, is it ideal?)\nind2.evaluated = true;\nSystem.out.println(\"----------- RUN END ------------\\n\\n\");\n}\n}";

		final String paramsTxt = "breedthreads = 1\nevalthreads  = 1\nseed.0 = ";  //Seed Number here
		final String paramsTxt2 = "\nstate = ec.simple.SimpleEvolutionState\npop = ec.Population\ninit = ec.simple.SimpleInitializer\nfinish = ec.simple.SimpleFinisher\nbreed = ec.simple.SimpleBreeder\neval = ec.simple.SimpleEvaluator\nstat = ec.simple.SimpleStatistics\nexch = ec.simple.SimpleExchanger\ngenerations = "; //Generations
		final String paramsTxt3 = "\nquit-on-complete = true\ncheckpoint = false\ncheckpoint-prefix = ec\ncheckpoint-modulo = 1\nstat.file ";  //Stat File
		final String paramsTxt4 = "\npop.subpops = 1\npop.subpop.0 = ec.Subpopulation\npop.subpop.0.size = "; //Pop Size
		final String paramsTxt5 = "\npop.subpop.0.duplicate-retries = 0\npop.subpop.0.species = ec.vector.BitVectorSpecies\npop.subpop.0.species.fitness = ec.simple.SimpleFitness\npop.subpop.0.species.ind = ec.vector.BitVectorIndividual\npop.subpop.0.species.genome-size = "; //genomeSize
		final String paramsTxt6 = "\npop.subpop.0.species.crossover-type = two\npop.subpop.0.species.crossover-prob = "; //CrossOverProb
		final String paramsTxt7 = "\npop.subpop.0.species.mutation-prob  = "; //Mutation Prob
		final String paramsTxt8 = "\npop.subpop.0.species.pipe = ec.vector.breed.VectorMutationPipeline\npop.subpop.0.species.pipe.source.0 = ec.vector.breed.VectorCrossoverPipeline\npop.subpop.0.species.pipe.source.0.source.0 = ec.select.TournamentSelection\npop.subpop.0.species.pipe.source.0.source.1 = ec.select.TournamentSelection\nselect.tournament.size = 2\neval.problem = ec.app.trafficSim."; //Java File Name


		int testNum = 0;
		int [] seedNumList = {923,866,288,353,782,234,78,510,603,291,809,810,835,855,528,755,642,823,326,764,284,731,391,380,201,65,7,559,708,931};
		
		//Set up heading for index
		
		indexSB.append("testNum,genNum,popSize,crossProb,mutProb,modifiers,seedNum\n");

		OuterLoop:
		for(int genNum = 20; genNum < 121; genNum+=20) {
			for(int popSize = 25; popSize < 61; popSize+= 20) {
					for(double crossProb = 0; crossProb <= .8; crossProb+=0.2) {
						for(double mutProb = 0; mutProb < .5; mutProb+=0.1) {
							for(int modifiers = 0; modifiers < 4; modifiers++) {
								for(int seedNum = 0; seedNum < 20; seedNum++) {
									String tempNodeTxtFile = "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Node.txt";
									String tempEdgeTxtFile = "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Edge.txt";
									String tempCarTxtFile = "/Users/wechtera/Desktop/FinalComp/ecj/ec/app/trafficSim/sim/files/Cars.txt";
									String tempParamsName;
									String tempJavaName;
									String tempOutPutStat;
									double modifier1;
									double modifier2;
									double modifier3;

								//Do modifiers here
									/*
									*	if 1, then favors modifier 1, 2 favors 2, 3 favors 3 0 has no favorite
									*/
									if(modifiers == 0) {
										modifier1 = .33;
										modifier2 = .33;
										modifier3 = .33;
									}
									else if (modifiers == 1) {
										modifier1 = .66;
										modifier2 = .33;
										modifier3 = .33;
									}
									else if (modifiers == 2) {
										modifier1 = .33;
										modifier2 = .66;
										modifier3 = .33;
									}
									else {
										modifier1 = .33;
										modifier2 = .33;
										modifier3 = .66;
									}

								//GENERATE File Names
									StringBuilder sb0 = new StringBuilder(paramsName);
									sb0.append(testNum + ".params");
									tempParamsName = sb0.toString();

									sb0 = new StringBuilder(javaName);
									sb0.append(testNum + ".java");
									tempJavaName = sb0.toString();

									sb0 = new StringBuilder(outputStat);
									sb0.append(testNum + ".stat");
									tempOutPutStat = sb0.toString();


									int totalNodeNumber = 800;

								


								//build java
									//javatxt + node + javatxt2 + edge + javatext2 + cars + javatxt3 + mod1 + javatxt4 + mod2 + javatext4 + mod3 + javatxt5
									sb0 = new StringBuilder(javaTxt);
									sb0.append(javaName + testNum);
									sb0.append(javaTxt6);
									sb0.append(tempNodeTxtFile);
									sb0.append(javaTxt2);
									sb0.append(tempEdgeTxtFile);
									sb0.append(javaTxt2);
									sb0.append(tempCarTxtFile);
									sb0.append(javaTxt3);
									sb0.append(modifier1);
									sb0.append(javaTxt4);
									sb0.append(modifier2);
									sb0.append(javaTxt4);
									sb0.append(modifier3);
									sb0.append(javaTxt5);
								//Writing the java file
									File newFile = new File(tempJavaName);
									if(newFile.exists()) {
										System.out.println("DELETING EXISTING JAVA FILE");
										newFile.delete();
									}

									try {
										newFile.createNewFile();
										FileWriter writer = new FileWriter(newFile,false);
										BufferedWriter buff = new BufferedWriter(writer);
										buff.write(sb0.toString());
										buff.close();
										writer.close();
									} catch(IOException e) {
										System.out.println("Problem Writing to Timings");
										System.out.println("File Name: " + newFile.getAbsolutePath());
										e.printStackTrace();
									}

								//writing params file

									sb0 = new StringBuilder(paramsTxt);
									sb0.append(seedNumList[seedNum]);
									sb0.append(paramsTxt2);
									sb0.append(genNum);
									sb0.append(paramsTxt3);
									sb0.append(tempOutPutStat);
									sb0.append(paramsTxt4);
									sb0.append(popSize);
									sb0.append(paramsTxt5);
									sb0.append(totalNodeNumber);
									sb0.append(paramsTxt6);
									sb0.append(crossProb);
									sb0.append(paramsTxt7);
									sb0.append(mutProb);
									sb0.append(paramsTxt8);
									sb0.append(javaName+testNum);



									newFile = new File(tempParamsName);

									if(newFile.exists()) {
										System.out.println("DELETING EXISTING PARAMS FILE");
										newFile.delete();
									}
									try {
										newFile.createNewFile();
										FileWriter writer = new FileWriter(newFile,false);
										BufferedWriter buff = new BufferedWriter(writer);
										buff.write(sb0.toString());
										buff.close();
										writer.close();
									} catch(IOException e) {
										System.out.println("Problem Writing to Params");
										System.out.println("File Name: " + newFile.getAbsolutePath());
										e.printStackTrace();
									}

								//Writing Index
									indexSB.append(testNum+","+genNum+","+popSize+","+crossProb+","+mutProb+","+modifiers+","+seedNumList[seedNum]+"\n");
								//java file list
									paramsSB.append(tempParamsName + "\n");



									testNum++;
								}
							}
						}
						
					
				}
				break OuterLoop;  //end here because pop and gen num do not influence enough
			}
		}

		//Writing index file

		File indexFile = new File("fileIndex6.txt");
		if(indexFile.exists()) {
			System.out.println("DELETING EXISTING INDEX FILE");
			indexFile.delete();
		}
		try {
			indexFile.createNewFile();
			FileWriter writer = new FileWriter(indexFile,false);
			BufferedWriter buff = new BufferedWriter(writer);
			buff.write(indexSB.toString());
			buff.close();
			writer.close();
		} catch(IOException e) {
			System.out.println("Problem Writing to index file");
			System.out.println("File Name: " + indexFile.getAbsolutePath());
			e.printStackTrace();
		}
		indexFile = new File("paramsIndex7.txt");
		if(indexFile.exists()) {
			System.out.println("DELETING EXISTING INDEX FILE");
			indexFile.delete();
		}
		try {
			indexFile.createNewFile();
			FileWriter writer = new FileWriter(indexFile,false);
			BufferedWriter buff = new BufferedWriter(writer);
			buff.write(paramsSB.toString());
			buff.close();
			writer.close();
		} catch(IOException e) {
			System.out.println("Problem Writing to index file");
			System.out.println("File Name: " + indexFile.getAbsolutePath());
			e.printStackTrace();
		}
	}


}
