/**
 * 
 */
package com.src.spatiotemporal.Algorithms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

//import org.json.*;
import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Main.SpatioTemporal;
import com.src.spatiotemporal.utils.Utils;
import java.util.Stack;

/**
 * @author INSPIRON
 *
 */
public class Baseline implements Algorithms {

	/*
	 * TODO: inserted by Raghavendra: [Mar 4, 2016:10:26:59 PM] Create a context
	 * class to pass on the variables from one class to another instead of using
	 * global variables
	 */
	// private Context context;

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Node> fetchRoute(long blockId, Date d) {
		// TODO Auto-generated method stub
		Block b = Utils.findNeighbourhood(blockId);
		// Currently hardcoded to 37.8055642728,-122.4134540334 and
		// block information is also hardcoded
		// Block b1 = new Block(612312,"Polk St (3100-3198)",
		// 37.8054164152,-122.4236028968,37.8061265339,-122.423746731,7001,7002,2,8);
		/*
		 * SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S"); Date
		 * d = null; try { d = format.parse("2012-04-06 00:19:35.36"); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * TODO: inserted by Raghavendra: [Mar 4, 2016:1:57:50 PM] Currently
		 * this method just prints the route. Should be made to return the route
		 * traversed. Also decide on the datatype of the return value of this
		 * method
		 */

		/********************************************************/
		// System.out.println(b.getBlockId());
		/********************************************************/
		return traceRoute(b, d);
	}

	private List<Node> traceRoute(Block b, Date timestamp) {
		// For our logic toNodeId will be the starting node, because
		// in the current block the car is traveling towards the toNode
		Hashtable<Long, Boolean> isVisited = SpatioTemporal.g.getIsVisited();
		return selectPaths(b, isVisited, SpatioTemporal.g.getAdjList(), SpatioTemporal.g.getCustomProjectionDetails(),
				timestamp);
	}

	private List<Node> selectPaths(Block b, Hashtable<Long, Boolean> isVisited,
			Hashtable<Long, ArrayList<Block>> adjList, TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails,
			Date timestamp) {
		// TODO Should search a parking lot while traversal
		// Should handle for the case where a block has both sides parking slots
		// Logic for above: If we encounter same end nodes twice for the same
		// starting node
		// then we have traced a block having both sides operational
		// Should be able to trace from the starting node of the starting block
		// Also decide on output format

		Stack<Long> nodesTraced = new Stack<Long>();
		Date initialTime = timestamp;
		Integer totalTimeInSecs = 0;
		while (true) {
			nodesTraced.push(b.getnodeId1());
			nodesTraced.push(b.getnodeId2());
			/*****************************************************/
			// Search for the parking slot in the very first block
			boolean slotFound = false;
			Integer timeToTravelBlockInSecs = null;
			Integer timeToTravelFirstBlockInSecs = null;
			ArrayList<Object> retVal = findSlot(customProjectionDetails, b, timestamp, adjList);
			if (retVal.size() > 0) {
				timestamp = (Date) retVal.get(0);
				slotFound = (boolean) retVal.get(1);
				timeToTravelBlockInSecs = (Integer) retVal.get(2);
				totalTimeInSecs += timeToTravelBlockInSecs; 
				timeToTravelFirstBlockInSecs = timeToTravelBlockInSecs;
			} else {
				return null;
			}
			if (slotFound){
				System.out.println(b.getBlockId()+","+initialTime+","+totalTimeInSecs);
				return SpatioTemporal.g.getNodeFromNodeId(nodesTraced);
			}
			/*****************************************************/
			Long startNode = b.getnodeId2();
			if (adjList.containsKey(startNode)) {
				ArrayList<Block> toNodes = adjList.get(startNode);
				for (int i = 0; i < toNodes.size(); i++) {
					ArrayList<Object> retVal1 = findSlot(customProjectionDetails, toNodes.get(i), timestamp, adjList);
					nodesTraced.push(toNodes.get(i).getnodeId2());
					Date newTime = null;
					if (retVal1.size() > 0) {
						newTime = (Date) retVal1.get(0);
						slotFound = (boolean) retVal1.get(1);
						timeToTravelBlockInSecs = (Integer) retVal1.get(2);
						totalTimeInSecs += timeToTravelBlockInSecs;
					} else {
						return null;
					}
					// Add the timeToTravelBlockInSecs twice because, once it
					// reaches the end of a block, it has to return empty and
					// hence add same to the node it started from. Here
					// itself the logic for handling blocks with two sides
					// parking slots should be done
					if (slotFound) {
						System.out.println(b.getBlockId()+","+initialTime+","+totalTimeInSecs);
						return SpatioTemporal.g.getNodeFromNodeId(nodesTraced);
					}
					nodesTraced.pop();
					Calendar cal = Calendar.getInstance();
					cal.setTime(newTime);
					cal.add(Calendar.SECOND, timeToTravelBlockInSecs);
					SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
					try {
						newTime = format.parse(format.format(cal.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timestamp = newTime;
				}
			}
			/*****************************************************/
			// After searching the parking slot in one direction and failed
			// to find one, return back to starting node and start searching
			// backwards
			nodesTraced.pop();
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.SECOND, timeToTravelFirstBlockInSecs);
			SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
			Date newTime = null;
			try {
				newTime = format.parse(format.format(cal.getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timestamp = newTime;
			startNode = b.getnodeId1();
			if (adjList.containsKey(startNode)) {
				ArrayList<Block> toNodes = adjList.get(startNode);
				for (int i = 0; i < toNodes.size(); i++) {
					if (toNodes.get(i).getBlockId() != b.getBlockId()) {
						ArrayList<Object> retVal2 = findSlot(customProjectionDetails, toNodes.get(i), timestamp,
								adjList);
						nodesTraced.push(toNodes.get(i).getnodeId2());
						Date newTime1 = null;
						if (retVal2.size() > 0) {
							newTime1 = (Date) retVal2.get(0);
							slotFound = (boolean) retVal2.get(1);
							timeToTravelBlockInSecs = (Integer) retVal2.get(2);
							totalTimeInSecs += timeToTravelBlockInSecs;
						} else {
							return null;
						}
						// Add the timeToTravelBlockInSecs twice because, once
						// it reaches the end of a block, it has to return
						// empty and hence add same to the node it started
						// from. Here itself the logic for handling blocks
						// with two sides parking slots should be done
						if (slotFound) {
							System.out.println(b.getBlockId()+","+initialTime+","+totalTimeInSecs);
							return SpatioTemporal.g.getNodeFromNodeId(nodesTraced);
						}
						nodesTraced.pop();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTime(newTime1);
						cal1.add(Calendar.SECOND, timeToTravelBlockInSecs);
						SimpleDateFormat format1 = new SimpleDateFormat("y-M-d H:m:s.S");
						try {
							newTime1 = format1.parse(format1.format(cal1.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						timestamp = newTime1;
					}
				}
			}
			nodesTraced.clear();
			/*****************************************************/
		}
	}

	private ArrayList<Object> findSlot(TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails, Block b,
			Date timestamp, Hashtable<Long, ArrayList<Block>> adjList) {
		ArrayList<Object> retVal = new ArrayList<Object>();
		boolean slotFound = false;
		Long currTravelingBlock = b.getBlockId();
		Long startNode = b.getnodeId1();
		Long endNode = b.getnodeId2();
		Long secondBlockId = null;
		
		if(currTravelingBlock.longValue() == -1){
			retVal.add(timestamp);
			retVal.add(slotFound);
			retVal.add(0);
		}

		if (b.getOperational() == 2) {
			// we have to do something
			if (adjList.contains(startNode)) {
				ArrayList<Block> toList = adjList.get(startNode);
				for (Block entry : toList) {
					if (entry.getOperational() == 2 && entry.getnodeId2() == endNode) {
						secondBlockId = entry.getBlockId();
					}
				}
			}
		}

		if (customProjectionDetails.containsKey(currTravelingBlock)) {
			TreeMap<Date, Integer> timeStampAndAvailSlotsMap = customProjectionDetails.get(currTravelingBlock);

			TreeMap<Date, Integer> timeStampAndAvailSlotsMap2 = null;
			if (secondBlockId != null) {
				if (customProjectionDetails.containsKey(secondBlockId)) {
					timeStampAndAvailSlotsMap2 = customProjectionDetails.get(secondBlockId);
				}
			}
			Integer blockLength = SpatioTemporal.nodeDistMap.get(startNode).get(endNode);
			Integer timeToTravelBlockInSecs = blockLength * 36 / 528;
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.SECOND, timeToTravelBlockInSecs);
			SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
			Date newTime = null;
			try {
				newTime = format.parse(format.format(cal.getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(newTime);

			SortedMap<Date, Integer> availableSlotsBetweenTimestamps = timeStampAndAvailSlotsMap.subMap(timestamp, true,
					newTime, false);
			SortedMap<Date, Integer> availableSlotsBetweenTimestamps2 = null;
			if (availableSlotsBetweenTimestamps.isEmpty()) {
				availableSlotsBetweenTimestamps = findParkingActivites(timestamp, newTime, timeStampAndAvailSlotsMap);
			}

			ArrayList<Integer> parkingSlotsInSecondBlock = new ArrayList<Integer>();
			if (timeStampAndAvailSlotsMap2 != null) {
				availableSlotsBetweenTimestamps2 = timeStampAndAvailSlotsMap2.subMap(timestamp, true, newTime, false);
				if (availableSlotsBetweenTimestamps2.isEmpty()) {
					availableSlotsBetweenTimestamps2 = findParkingActivites(timestamp, newTime,
							timeStampAndAvailSlotsMap2);
				}
				for (Map.Entry<Date, Integer> entry : availableSlotsBetweenTimestamps2.entrySet()) {
					parkingSlotsInSecondBlock.add(entry.getValue());
				}
			}

			Integer numTimeSlots = availableSlotsBetweenTimestamps.size();
			Integer nParkingSlotAvailability = 0;
			int i = 0;

			for (Map.Entry<Date, Integer> entry : availableSlotsBetweenTimestamps.entrySet()) {
				int nSlotsInSecondBlock = 0;
				if (!parkingSlotsInSecondBlock.isEmpty() && i < parkingSlotsInSecondBlock.size()) {
					nSlotsInSecondBlock = parkingSlotsInSecondBlock.get(i).intValue();
				}
				if (entry.getValue().intValue() + nSlotsInSecondBlock > 0) {
					// System.out.println("Got parking slot");
					nParkingSlotAvailability++;
					// return;
				}
				i++;
			}
			if (nParkingSlotAvailability == numTimeSlots) {
				//System.out.println("Got parking slot");
				slotFound = true;
				// System.out.println((int)Math.ceil(numTimeSlots / 2.0));
			}
			timestamp = newTime;
			retVal.add(timestamp);
			retVal.add(slotFound);
			retVal.add(timeToTravelBlockInSecs);
		}
		return retVal;
	}

	private SortedMap<Date, Integer> findParkingActivites(Date d1, Date d2,
			TreeMap<Date, Integer> timeStampAndAvailSlotsMap) {
		Date[] orderedDate = timeStampAndAvailSlotsMap.keySet()
				.toArray(new Date[timeStampAndAvailSlotsMap.keySet().size()]);
		int idx1 = binSearch(orderedDate, 0, orderedDate.length, d1, true);
		int idx2 = binSearch(orderedDate, 0, orderedDate.length, d2, false);
		d1 = orderedDate[idx1];
		d2 = orderedDate[idx2];
		return timeStampAndAvailSlotsMap.subMap(d1, true, d2, false);
	}

	private int binSearch(Date[] orderedDate, int s, int e, Date searchTerm, boolean isFirstTimeCall) {
		int mid = (s + e) / 2;
		if (s <= e) {
			if (mid < orderedDate.length-1 && searchTerm.equals(orderedDate[mid])) {
				return mid;
			} else if (mid < orderedDate.length-1 && searchTerm.after(orderedDate[mid]) && searchTerm.before(orderedDate[mid + 1])) {
				//System.out.println(orderedDate.length+ "," +mid);
				if (isFirstTimeCall)
					return mid;
				else
					return mid + 1;
			} else if (mid < orderedDate.length-1 && searchTerm.after(orderedDate[mid])) {
				return binSearch(orderedDate, mid + 1, e, searchTerm, isFirstTimeCall);
			} else {
				return binSearch(orderedDate, s, mid - 1, searchTerm, isFirstTimeCall);
			}
		} else {
			return s;
		}
	}

}

