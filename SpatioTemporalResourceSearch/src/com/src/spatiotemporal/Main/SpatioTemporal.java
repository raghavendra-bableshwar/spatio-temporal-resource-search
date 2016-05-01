package com.src.spatiotemporal.Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.src.spatiotemporal.Algorithms.Baseline;
import com.src.spatiotemporal.Algorithms.Deterministic;
import com.src.spatiotemporal.Algorithms.Probabilistic;
//import com.src.spatiotemporal.Algorithms.Probabilistic;
import com.src.spatiotemporal.Entity.Adjacency;
import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Graph;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Entity.Projection;
import com.src.spatiotemporal.utils.Utils;

public class SpatioTemporal {
	/*
	TODO: inserted by Raghavendra: [Mar 8, 2016:7:41:06 PM]
	All these global variables to be moved as a function variables
	*/
	public static List<Node> nodeList = new ArrayList<Node>();
	public static List<Block> blockList = new ArrayList<Block>();
	public static List<Projection> projectionList = new ArrayList<Projection>();
	public static TreeMap<Date,ArrayList<Long>> dateAndBlockDetails = 
			new TreeMap<Date,ArrayList<Long>>();
	public static TreeMap<Long,TreeMap<Long,Integer>> nodeDistMap = 
			new TreeMap<Long,TreeMap<Long,Integer>>(); 
	public static Map<Long,Block> blockDetails = new HashMap<Long,Block>();
	
	public static Map<Long,Node> nodeDetails = new HashMap<Long,Node>();
	public static Graph g = new Graph();
	public static ArrayList<Adjacency> adjacencyList = new ArrayList<Adjacency>();
	
	public static int CONGESTION_LEVEL = 0;

	public static void populateData(String csvPath) throws IOException, ParseException
	{
		/* Load congestion value from properties file */
		Properties prop = new Properties();
		InputStream ip = new FileInputStream(csvPath + "\\..\\Congestion.properties");
		prop.load(ip);
		String congestionValue = prop.getProperty("CONGESTION_LEVEL");
		CONGESTION_LEVEL = Integer.parseInt(congestionValue);
		
		
		/* Loading all the application level data in various data structures */
		Utils.populateNode(csvPath);
		Utils.populateBlock(csvPath);
		Utils.populateProjection(csvPath);
		Utils.populateAdjacency(csvPath);
		
		Utils.generatePreditiveModel_1(projectionList);
		
	}
	
	/*The following method is to run the simulations*/
	
	/*Simulations.csv contains list of blocks and corresponding timestamp. The following method contain automated script to execute all the 3 algorithms 
	 * and output on console. You can redirect the standard output to local CSV file. */
	
	public static void main(String argus[]) throws IOException, ParseException
	{
	
		/*Set local path of "data files" folder */
		String localDataFilePath = "C:\\Users\\Swapnil\\git\\spatiotemporalresourcesearch_web\\SpatioTemporalResourceSearch\\WebContent\\data_files";
		
		Properties prop = new Properties();
		InputStream ip = new FileInputStream(localDataFilePath + "\\..\\Congestion.properties");
		prop.load(ip);
		String congestionValue = prop.getProperty("CONGESTION_LEVEL");
		CONGESTION_LEVEL = Integer.parseInt(congestionValue);
		
		Utils.populateNode(localDataFilePath);
		Utils.populateBlock(localDataFilePath);
		Utils.populateProjection(localDataFilePath);
		Utils.populateAdjacency(localDataFilePath);
		
		Utils.generatePreditiveModel_1(projectionList);
		
		BufferedReader br = null;
		String line = "";
		br = new BufferedReader(new FileReader(localDataFilePath + "\\Simulations.csv"));
		line = br.readLine();
		String cvsSplitBy = ",";		

		System.out.println("Block,Timestamp,Travel time");
		
		while((line = br.readLine()) != null)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("y-M-d H:m:s.S");
			String[] simulationDetails = line.split(cvsSplitBy);

			long blockId = Long.parseLong(simulationDetails[0]);
			Date projDate = null;
			try {
				projDate = formatter.parse(simulationDetails[1]);
			} catch(Exception e) 
			{ 
				e.printStackTrace(); 
			}
			
			//Uncomment one of the block to run the corresponding algorithm
			
			/*Probabilistic probAlgo = new Probabilistic();
			probAlgo.fetchRoute(blockId,projDate);
			Probabilistic.total_time=0;*/
			
			/*Deterministic d = new Deterministic();
			d.fetchRoute(blockId, projDate);*/
			
			/*Baseline b = new Baseline();
			b.fetchRoute(blockId, projDate);*/
		}

	}
}
