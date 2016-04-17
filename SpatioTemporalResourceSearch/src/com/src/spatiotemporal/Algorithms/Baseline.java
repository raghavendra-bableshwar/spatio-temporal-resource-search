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
	public void fetchRoute(Double latitude, Double longitude) {
		// TODO Auto-generated method stub
		Block b = Utils.findNeighbourhood(latitude, longitude);
		// Currently hardcoded to 37.8055642728,-122.4134540334 and
		// block information is also hardcoded
		// Block b1 = new Block(612312,"Polk St (3100-3198)",
		// 37.8054164152,-122.4236028968,37.8061265339,-122.423746731,7001,7002,2,8);
		SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
		Date d = null;
		try {
			d = format.parse("2012-04-05 21:10:50.543");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * TODO: inserted by Raghavendra: [Mar 4, 2016:1:57:50 PM] Currently
		 * this method just prints the route. Should be made to return the route
		 * traversed. Also decide on the datatype of the return value of this
		 * method
		 */
		traceRoute(b, d, latitude, longitude);
		/********************************************************/
		// System.out.println(b.getBlockId());
		/********************************************************/
	}

	private void traceRoute(Block b, Date timestamp, Double latitude, Double longitude) {
		// For our DFS toNodeId will be the starting node, because
		// in the current block the car is traveling towards the toNode
		Hashtable<Long, Boolean> isVisited = SpatioTemporal.g.getIsVisited();
		// isVisited.put(b.getnodeId1(), true);
		DFS(b, isVisited, SpatioTemporal.g.getAdjList(), SpatioTemporal.g.getCustomProjectionDetails(), timestamp,
				latitude, longitude);
		/**********************************************************
		 * Just ensuring all the entries are present in the map for (Entry<Date,
		 * ArrayList<Projection>> entry :
		 * SpatioTemporal.dateAndBlockDetails.entrySet()) {
		 * ArrayList<Projection> projList = entry.getValue(); for(int i=0;
		 * i<projList.size();i++) System.out.println("Key: " + entry.getKey() +
		 * ". Value: " + projList.get(i).getBlockId()); }
		 **********************************************************/
		// SpatioTemporal.g.getCustomBlockDetails();
	}

	private void DFS(Block b, Hashtable<Long, Boolean> isVisited, Hashtable<Long, ArrayList<Block>> adjList,
			TreeMap<Long, TreeMap<Date, Integer>> customProjectionDetails, Date timestamp, Double latitude,
			Double longitude) {
		// TODO Should search a parking lot while traversal
		// Should handle for the case where a block has both sides parking slots
		// Should handle a case where block id = -1 i.e., no road information
		// for it
		// Also decide on output format

		/*****************************************************/
		// Search for the parking slot in the very first block
		Long startNode = b.getnodeId1();
		Long endNode = b.getnodeId2();
		Long currTravelingBlock = b.getBlockId();
		if (customProjectionDetails.containsKey(currTravelingBlock)) {
			TreeMap<Date, Integer> timeStampAndAvailSlotsMap = customProjectionDetails.get(currTravelingBlock);

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
			System.out.println(newTime);
			SortedMap<Date, Integer> availableSlotsBetweenTimestamps = timeStampAndAvailSlotsMap.subMap(timestamp,
					newTime);
			Integer numTimeSlots = availableSlotsBetweenTimestamps.size();
			Integer nParkingSlotAvailability = 0;

			for (Map.Entry<Date, Integer> entry : availableSlotsBetweenTimestamps.entrySet()) {
				if (entry.getValue() > 0) {
					//System.out.println("Got parking slot");
					nParkingSlotAvailability++;
					// return;
				}
			}
			if (nParkingSlotAvailability >= (int)Math.ceil(numTimeSlots / 2.0) && nParkingSlotAvailability != 0) {
				System.out.println("Got parking slot");
				//System.out.println((int)Math.ceil(numTimeSlots / 2.0));
				return;
			}
			timestamp = newTime;

			/*
			 * if (timeStampAndAvailSlotsMap.containsKey(timestamp)) { if
			 * (timeStampAndAvailSlotsMap.get(timestamp) > 0) {
			 * System.out.println("Exists!"); return; } }
			 */
		}
		/*****************************************************/
		startNode = b.getnodeId2();
		isVisited.put(startNode, true);
		if (adjList.containsKey(startNode)) {
			ArrayList<Block> toNodes = adjList.get(startNode);
			for (int i = 0; i < toNodes.size(); i++) {
				currTravelingBlock = toNodes.get(i).getBlockId();
				startNode = toNodes.get(i).getnodeId1();
				endNode = toNodes.get(i).getnodeId2();
				if (customProjectionDetails.containsKey(currTravelingBlock)) {
					TreeMap<Date, Integer> timeStampAndAvailSlotsMap = customProjectionDetails.get(currTravelingBlock);

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
					System.out.println(newTime);
					SortedMap<Date, Integer> availableSlotsBetweenTimestamps = timeStampAndAvailSlotsMap
							.subMap(timestamp, newTime);
					Integer numTimeSlots = availableSlotsBetweenTimestamps.size();
					Integer nParkingSlotAvailability = 0;

					for (Map.Entry<Date, Integer> entry : availableSlotsBetweenTimestamps.entrySet()) {
						if (entry.getValue() > 0) {
							//System.out.println("Got parking slot");
							nParkingSlotAvailability++;
							// return;
						}
					}
					if (nParkingSlotAvailability >= (int)Math.ceil(numTimeSlots / 2.0) && nParkingSlotAvailability != 0) {
						System.out.println("Got parking slot");
						//System.out.println((int)Math.ceil(numTimeSlots / 2.0));
						return;
					}
					timestamp = newTime;

					/*
					 * if (timeStampAndAvailSlotsMap.containsKey(timestamp)) {
					 * if (timeStampAndAvailSlotsMap.get(timestamp) > 0) {
					 * System.out.println("Exists!"); break; } }
					 */
				}
			}
		}
	}

	@Override
	public List<Node> fetchRoute(Block b,String hour, String min, String sec) {
		return null;
		// TODO Auto-generated method stub
		
	}
}
