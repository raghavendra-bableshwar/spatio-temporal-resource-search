/**
 * Stores the graphical representation of the provided csv
 * Uses adjacency list representation 
 */
package com.src.spatiotemporal.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * @author INSPIRON
 *
 */
public class Graph {
	private Hashtable<Long, ArrayList<Block>> adjList;
	private Hashtable<Long, Boolean> isVisited;
	// private LinkedHashMap<Double, Block> customBlockDetails;
	private TreeMap<Double, Block> customBlockDetails;
	private TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails;

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
	public TreeMap<Double, Block> getCustomBlockDetails() {
		return customBlockDetails;
	}

	/**
	 * @param customBlockDetails
	 *            the customBlockDetails to set
	 */
	public void setCustomBlockDetails(TreeMap<Double, Block> customBlockDetails) {
		this.customBlockDetails = customBlockDetails;
	}

	/**
	 * @param starting
	 *            longitude of the block and its details
	 */
	public void addCustomBlockDetailsEntry(Double longitude, Block b) {
		customBlockDetails.put(longitude, b);
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
		this.customBlockDetails = new TreeMap<Double, Block>();
		this.customProjectionDetails = new TreeMap<Long, TreeMap<Date, Integer>>();
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

}
