/**
 * Stores the graphical representation of the provided csv
 * Uses adjacency list representation 
 */
package com.src.spatiotemporal.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author INSPIRON
 *
 */
public class Graph {
	private Hashtable<Long, ArrayList<Block>> adjList;
	private Hashtable<Long, Boolean> isVisited;
	private TreeMap<Long, Block> customBlockDetails;
	private TreeMap<Long, Node> customNodeDetails;
	private TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails;
	
	/**
	 * @return the customNodeDetails
	 */
	public TreeMap<Long, Node> getCustomNodeDetails() {
		return customNodeDetails;
	}

	/**
	 * @param customNodeDetails the customNodeDetails to set
	 */
	public void setCustomNodeDetails(TreeMap<Long, Node> customNodeDetails) {
		this.customNodeDetails = customNodeDetails;
	}
	
	public void addCustomNodeDetails(Node node){
		this.customNodeDetails.put((long) node.getNodeId(), node);
	}
	
	public List<Node> getNodeFromNodeId(Stack<Long> nodeIds){
		ArrayList<Node> nodeList = new ArrayList<Node>();
		while(!nodeIds.empty()){
			Long nodeId = nodeIds.pop();
			if(this.customNodeDetails.containsKey(nodeId)){
				nodeList.add(this.customNodeDetails.get(nodeId));
			}
		}
		Collections.reverse(nodeList);
		return nodeList;
	}


	/**
	 * @return the customProjectionDetails
	 */
	public TreeMap<Long, TreeMap<Date, Integer>> getCustomProjectionDetails() {
		return customProjectionDetails;
	}

	/**
	 * @param customProjectionDetails
	 *            the customProjectionDetails to set
	 */
	public void setCustomProjectionDetails(TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails) {
		this.customProjectionDetails = customProjectionDetails;
	}

	/**
	 * @param customProjectionDetails
	 *            the customProjectionDetails to set
	 */
	public void addCustomProjectionDetailsEntry(Long blockId, Date timeStamp, Integer numAvailslots) {
		if (this.customProjectionDetails.containsKey(blockId)) {
			this.customProjectionDetails.get(blockId).put(timeStamp, numAvailslots);
		} else {
			TreeMap<Date, Integer> timeStampAndAvailSlotsMap = new TreeMap<Date, Integer>();
			timeStampAndAvailSlotsMap.put(timeStamp, numAvailslots);
			this.customProjectionDetails.put(blockId, timeStampAndAvailSlotsMap);
		}
	}

	/**
	 * @return the customBlockDetails
	 */
	public TreeMap<Long, Block> getCustomBlockDetails() {
		return customBlockDetails;
	}

	/**
	 * @param customBlockDetails
	 *            the customBlockDetails to set
	 */
	public void setCustomBlockDetails(TreeMap<Long, Block> customBlockDetails) {
		this.customBlockDetails = customBlockDetails;
	}

	/**
	 * @param starting
	 *            longitude of the block and its details
	 */
	public void addCustomBlockDetailsEntry(Long blockId, Block b) {
		customBlockDetails.put(blockId, b);
	}

	public Hashtable<Long, Boolean> getIsVisited() {
		return isVisited;
	}

	public void setIsVisited(Hashtable<Long, Boolean> isVisited) {
		this.isVisited = isVisited;
	}

	public void addNode(Long nodeId) {
		isVisited.put(nodeId, false);
	}

	public void addEdge(long from, Block to) {
		if (this.adjList.containsKey(from)) {
			this.adjList.get(from).add(to);
		} else {
			ArrayList<Block> toNodes = new ArrayList<Block>();
			toNodes.add(to);
			this.adjList.put(from, toNodes);
		}
	}

	public Graph() {
		this.adjList = new Hashtable<Long, ArrayList<Block>>();
		this.isVisited = new Hashtable<Long, Boolean>();
		this.customBlockDetails = new TreeMap<Long, Block>();
		this.customProjectionDetails = new TreeMap<Long, TreeMap<Date, Integer>>();
		this.customNodeDetails = new TreeMap<Long, Node>();
	}

	/**
	 * @param adjList
	 */
	public Graph(Hashtable<Long, ArrayList<Block>> adjList, Hashtable<Long, Boolean> isVisited) {
		// super();
		this.adjList = adjList;
		this.isVisited = isVisited;
	}

	/**
	 * @return the adjList
	 */
	public Hashtable<Long, ArrayList<Block>> getAdjList() {
		return adjList;
	}

	/**
	 * @param adjList
	 *            the adjList to set
	 */
	public void setAdjList(Hashtable<Long, ArrayList<Block>> adjList) {
		this.adjList = adjList;
	}

	public Block getBlockDetails(Double longitude, Double latitude) {
		Double[] orderedLongitudesArray = customBlockDetails.keySet()
				.toArray(new Double[customBlockDetails.keySet().size()]);
		int blockIdx = binSearch(orderedLongitudesArray, 0, orderedLongitudesArray.length, longitude);
		if (blockIdx != -1 && checkCorrectnessOfBlock(longitude, latitude,
				customBlockDetails.get(orderedLongitudesArray[blockIdx]))) {
			return customBlockDetails.get(orderedLongitudesArray[blockIdx]);
		}
		return null;
	}

	private Boolean checkCorrectnessOfBlock(Double longitude, Double latitude, Block block) {
		// TODO Auto-generated method stub
		if (longitude.doubleValue() >= block.getBlockLongitude1().doubleValue()
				&& longitude.doubleValue() <= block.getBlockLongitude2().doubleValue()
				&& latitude.doubleValue() <= block.getBlockLatitude1().doubleValue()
				&& latitude.doubleValue() >= block.getBlockLatitude2().doubleValue()) {
			return true;
		} else
			return false;
	}

	private int binSearch(Double[] orderedLongitudesArray, int s, int e, Double searchTerm) {
		// TODO Auto-generated method stub
		int mid = (s + e) / 2;
		if (s <= e) {
			if (searchTerm.doubleValue() < orderedLongitudesArray[mid].doubleValue()
					&& searchTerm.doubleValue() > orderedLongitudesArray[mid - 1].doubleValue())
				return mid - 1;
			else if (searchTerm.doubleValue() == orderedLongitudesArray[mid].doubleValue())
				return mid;
			else if (searchTerm.doubleValue() < orderedLongitudesArray[mid].doubleValue())
				return binSearch(orderedLongitudesArray, s, mid - 1, searchTerm);
			else
				return binSearch(orderedLongitudesArray, mid + 1, e, searchTerm);
		} else
			return -1;
	}

	public Block getBlockDetails(Long blockId) {
		if(customBlockDetails.containsKey(blockId)){
			return customBlockDetails.get(blockId);
		}
		return null;
	}

}
