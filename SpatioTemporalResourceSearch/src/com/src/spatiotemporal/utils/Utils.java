/**
 * This is a class where all the static method definition goes.
 * Example findNeighbourhood
 */
package com.src.spatiotemporal.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;

import com.src.spatiotemporal.Entity.Adjacency;
import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Entity.Projection;
import com.src.spatiotemporal.Main.SpatioTemporal;

/**
 * @author Raghavendra
 *
 */
public class Utils {
	public static Block findNeighbourhood(Double longitude, Double latitude) {
		/*
		 * TODO: inserted by Raghavendra: [Feb 29, 2016:4:16:12 PM] Implement
		 * the method
		 */
		return SpatioTemporal.g.getBlockDetails(longitude, latitude);
		// return null;
	}

	public static void populateNode(String csvPath) throws IOException {
		String csvFile = csvPath + "\\SFPark_nodes_FishermansWharf.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		br = new BufferedReader(new FileReader(csvFile));
		 line = br.readLine();//this is to skip first line as its the header.
		while ((line = br.readLine()) != null) {
			String[] nodeDetails = line.split(cvsSplitBy);
			Node node = new Node(Integer.parseInt(nodeDetails[0]), Double.parseDouble(nodeDetails[1]),
					Double.parseDouble(nodeDetails[2]), nodeDetails[3]);
			SpatioTemporal.nodeList.add(node);
			// populating isVisited for graph
			SpatioTemporal.g.addNode(Long.parseLong(nodeDetails[0]));
		}
		br.close();
	}

	public static void populateBlock(String csvPath) throws IOException {
		String csvFile = csvPath + "\\SFPark_edges_FishermansWharf2.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		br = new BufferedReader(new FileReader(csvFile));
		 line = br.readLine();//this is to skip first line as its the header.
		// Block id Block name Longitude1 Laltitude1 Longitude2 Laltitude2
		// Blockid1 Blockid2 No_Blocks Operational
		while ((line = br.readLine()) != null) {
			String[] blockDetails = line.split(cvsSplitBy);
			Block block = new Block(Long.parseLong(blockDetails[0]), blockDetails[1],
					Double.parseDouble(blockDetails[2]), Double.parseDouble(blockDetails[3]),
					Double.parseDouble(blockDetails[4]), Double.parseDouble(blockDetails[5]),
					Long.parseLong(blockDetails[6]), Long.parseLong(blockDetails[7]), Integer.parseInt(blockDetails[8]),
					Integer.parseInt(blockDetails[9]));
			SpatioTemporal.blockList.add(block);
			/**************************************************************/
			// BigDecimal dobj = new BigDecimal(blockDetails[2]);
			// System.out.println(dobj);
			// DecimalFormat myFormatter = new
			// DecimalFormat("###.############");
			// String output = myFormatter.format(blockDetails[2]);
			// System.out.println(blockDetails[2]);
			/**************************************************************/
			// Populating graph too!
			SpatioTemporal.g.addEdge(Long.parseLong(blockDetails[6]), block);
			SpatioTemporal.g.addCustomBlockDetailsEntry(Double.parseDouble(blockDetails[2]), block);
		}
		br.close();
	}

	public static void populateProjection(String csvPath) throws IOException, ParseException {
		String csvFile = csvPath + "\\dbProjection.csv";
		/**************************************************************/
		// creating insert statement
		/*String insertQuery = " insert into dbProjection " + " (block_id, available, timestamp) values " + " (?,?,?) ";
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:./data_files/dbProjection.db");
			Statement stat = connection.createStatement();
			// stat.executeUpdate("drop table if exists people;");
			stat.executeUpdate(
					"create table if not exists dbProjection (block_id long, available integer, timestamp varchar(50));");
			// connection.commit();

			PreparedStatement prepStatement = null;
			try {
				prepStatement = connection.prepareStatement(insertQuery);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			/**************************************************************/
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();// this is to skip first line as its the
									// header.
			// Block id Block name Longitude1 Laltitude1 Longitude2 Laltitude2
			// Blockid1 Blockid2 No_Blocks Operational
			while ((line = br.readLine()) != null) {
				String[] projectionDetails = line.split(cvsSplitBy);
				// The timestamp is read in following format: 2012-04-05
				// 21:10:50.543
				SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
				Date d1 = format.parse(projectionDetails[2]);
				Projection projection = new Projection(Long.parseLong(projectionDetails[0]),
						Integer.parseInt(projectionDetails[1]), d1);
				/**************************************************************/
				/*try {
					prepStatement.setLong(1, Long.parseLong(projectionDetails[0]));
					prepStatement.setInt(2, Integer.parseInt(projectionDetails[1]));
					prepStatement.setString(3, projectionDetails[2]);
					prepStatement.addBatch();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				/**************************************************************/
				SpatioTemporal.projectionList.add(projection);
				if (SpatioTemporal.dateAndBlockDetails.containsKey(d1))
					SpatioTemporal.dateAndBlockDetails.get(d1).add(Long.parseLong(projectionDetails[0]));
				else {
					ArrayList<Long> blockIdList = new ArrayList<Long>();
					blockIdList.add(Long.parseLong(projectionDetails[0]));
					SpatioTemporal.dateAndBlockDetails.put(d1, blockIdList);
				}
				SpatioTemporal.g.addCustomProjectionDetailsEntry(Long.parseLong(projectionDetails[0]), d1,
						Integer.parseInt(projectionDetails[1]));
			}
			// Takes huge time, shd see if we can optimize
			// prepStatement.executeBatch();
			//connection.close();
			br.close();
	}

	public static void populateAdjacency(String csvPath) throws IOException, ParseException {
		String csvFile = csvPath + "\\Adjacency.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		br = new BufferedReader(new FileReader(csvFile));
		line = br.readLine();// this is to skip first line as its the header.

		while ((line = br.readLine()) != null) {
			TreeMap<Long, Integer> toNodeDistMap = new TreeMap<Long, Integer>();
			String[] AdjacencyDetails = line.split(cvsSplitBy);
			int[] AdjacentNode_Dist = new int[10];
			Arrays.fill(AdjacentNode_Dist, 0);

			if (AdjacencyDetails.length == 6) {
				AdjacentNode_Dist[4] = Integer.parseInt(AdjacencyDetails[4]);
				AdjacentNode_Dist[5] = Integer.parseInt(AdjacencyDetails[5]);
				toNodeDistMap.put((long) AdjacentNode_Dist[4], AdjacentNode_Dist[5]);

			} else if (AdjacencyDetails.length == 8) {
				AdjacentNode_Dist[4] = Integer.parseInt(AdjacencyDetails[4]);
				AdjacentNode_Dist[5] = Integer.parseInt(AdjacencyDetails[5]);
				AdjacentNode_Dist[6] = Integer.parseInt(AdjacencyDetails[6]);
				AdjacentNode_Dist[7] = Integer.parseInt(AdjacencyDetails[7]);
				toNodeDistMap.put((long) AdjacentNode_Dist[4], AdjacentNode_Dist[5]);
				toNodeDistMap.put((long) AdjacentNode_Dist[6], AdjacentNode_Dist[7]);

			} else if (AdjacencyDetails.length == 10) {
				AdjacentNode_Dist[4] = Integer.parseInt(AdjacencyDetails[4]);
				AdjacentNode_Dist[5] = Integer.parseInt(AdjacencyDetails[5]);
				AdjacentNode_Dist[6] = Integer.parseInt(AdjacencyDetails[6]);
				AdjacentNode_Dist[7] = Integer.parseInt(AdjacencyDetails[7]);
				AdjacentNode_Dist[8] = Integer.parseInt(AdjacencyDetails[8]);
				AdjacentNode_Dist[9] = Integer.parseInt(AdjacencyDetails[9]);
				toNodeDistMap.put((long) AdjacentNode_Dist[4], AdjacentNode_Dist[5]);
				toNodeDistMap.put((long) AdjacentNode_Dist[6], AdjacentNode_Dist[7]);
				toNodeDistMap.put((long) AdjacentNode_Dist[8], AdjacentNode_Dist[9]);
			}

			Adjacency adjacency = new Adjacency(Integer.parseInt(AdjacencyDetails[0]),
					Double.parseDouble(AdjacencyDetails[1]), Double.parseDouble(AdjacencyDetails[2]),
					AdjacencyDetails[3],

					AdjacentNode_Dist[4], AdjacentNode_Dist[5], AdjacentNode_Dist[6], AdjacentNode_Dist[7],
					AdjacentNode_Dist[8], AdjacentNode_Dist[9]);
			SpatioTemporal.adjacencyList.add(adjacency);
			SpatioTemporal.nodeDistMap.put(Long.parseLong(AdjacencyDetails[0]), toNodeDistMap);
		}
		br.close();
	}

}
