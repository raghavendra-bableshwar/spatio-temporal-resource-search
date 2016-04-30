package com.src.spatiotemporal.Main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.src.spatiotemporal.Algorithms.Algorithms;
import com.src.spatiotemporal.Algorithms.Baseline;
import com.src.spatiotemporal.Algorithms.Probabilistic;
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
	
	public static final int CONGESTION_LEVEL = 0;

	public static void populateData(String csvPath) throws IOException, ParseException
	{
		Utils.populateNode(csvPath);
		Utils.populateBlock(csvPath);
		Utils.populateProjection(csvPath);
		Utils.populateAdjacency(csvPath);
		//System.out.println(projectionList.size());
	//	System.out.println("Adjacency \n:"+g.getCustomBlockDetails().toString()+"\n");
		//System.out.println("Adjacency \n:"+g.getAdjList());
		
		//Should be handled in a switch case in order to 
		//switch between algos
		Algorithms chooseAlgo = new Baseline();
		//Dummy latitude and longitude passed
		//chooseAlgo.fetchRoute(37.8054164153, -122.4236028968);
		System.out.println("Done!");
	}
}
