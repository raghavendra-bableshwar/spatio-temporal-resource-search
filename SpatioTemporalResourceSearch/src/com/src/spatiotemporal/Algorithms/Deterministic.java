package com.src.spatiotemporal.Algorithms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Main.SpatioTemporal;

public class Deterministic implements Algorithms{

	List<Node> outputNodeList = new ArrayList<Node>();
	List<Integer> outputNodes = new ArrayList<Integer>();
	int total_time = 0;

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

	ArrayList<Integer> getAdjacentNodesAndDistances(long nodeId){
		ArrayList<Integer> from_nd_list=new ArrayList<Integer>();

		for(int i=0;i<SpatioTemporal.adjacencyList.size();i++){
			if(SpatioTemporal.adjacencyList.get(i).getNodeId() == nodeId && SpatioTemporal.adjacencyList.get(i).getNodeN1() != 0){
				
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getNodeN1());
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getDistanceD1());
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getNodeN2());
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getDistanceD2());
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getNodeN3());
					from_nd_list.add(SpatioTemporal.adjacencyList.get(i).getDistanceD3());
			}
		}	

		return from_nd_list;

	}
	List<Long> getBlockIDForNodes(long currentNodeId,long lastNodeId){
		long node1,node2;
		List<Long> blockListForNodes = new ArrayList<Long>();
		if(currentNodeId==0||lastNodeId==-1)
		{
			return blockListForNodes;
		}
		//TO ensure that from nodeID is smaller than to nodeID
		if(currentNodeId<lastNodeId){
			node1=currentNodeId;
			node2=lastNodeId;
		}else{
			node1=lastNodeId;
			node2=currentNodeId;
		}
		for(int i=0;i<SpatioTemporal.blockList.size();i++){
			if(SpatioTemporal.blockList.get(i).getnodeId1()== node1 &&					
					SpatioTemporal.blockList.get(i).getnodeId2()== node2)
			{
				blockListForNodes.add(SpatioTemporal.blockList.get(i).getBlockId());
			}
		}
		return blockListForNodes;
	}

	@Override
	public List<Node> fetchRoute(long blockId, Date d) {

		int res = 0;

		Block b = SpatioTemporal.blockDetails.get(blockId);
		long node1 = b.getnodeId1();
		outputNodes.add((int)node1);

		while(res!=1)
		{
			List<Integer> adjNodeList= getAdjacentNodesAndDistances(node1);
			if(adjNodeList.size() > 0)
			{
				List<Long> blkList1 = getBlockIDForNodes(node1,adjNodeList.get(0));
				List<Long> blkList2 = getBlockIDForNodes(node1,adjNodeList.get(2));
				List<Long> blkList3 = getBlockIDForNodes(node1,adjNodeList.get(4));

				int dist1 = adjNodeList.get(1);
				int dist2 = adjNodeList.get(3);
				int dist3 = adjNodeList.get(5);

				Integer timeToTravelBlockInSecs1 = dist1 * 36 / 528;
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, timeToTravelBlockInSecs1);
				SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
				Date newTime = null;
				try {
					newTime = format.parse(format.format(cal.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int num_of_slots1 = getNumofSlots(blkList1,d,newTime);		

				Integer timeToTravelBlockInSecs2 = dist2 * 36 / 528;

				cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, timeToTravelBlockInSecs2);
				newTime = null;
				try {
					newTime = format.parse(format.format(cal.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				int num_of_slots2 =  getNumofSlots(blkList2, d,newTime);


				Integer timeToTravelBlockInSecs3 = dist3 * 36 / 528;
				
				cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, timeToTravelBlockInSecs3);
				newTime = null;
				try {
					newTime = format.parse(format.format(cal.getTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int num_of_slots3 =  getNumofSlots(blkList3, d,newTime);

				double force1, force2, force3;

				if(dist1 != 0)
					force1 = (double)num_of_slots1 / ((double)dist1 * (double)dist1);
				else
					force1 = 0.0;

				if(dist2 != 0)
					force2 = (double)num_of_slots2 / ((double)dist2 * (double)dist2);
				else
					force2 = 0.0;

				if(dist3 != 0)
					force3 = (double)num_of_slots3 / ((double)dist3 * (double)dist3);
				else
					force3 = 0.0;

				res = findNextNodes(force1,force2,force3,adjNodeList,dist1,dist2,dist3);

				if(res!=-1)
				{
					long node = outputNodes.get((outputNodes.size()-1));
					if(node == adjNodeList.get(0))
						total_time += timeToTravelBlockInSecs1;
					else if(node == adjNodeList.get(2))
						total_time += timeToTravelBlockInSecs2;
					else if(node == adjNodeList.get(4))
						total_time += timeToTravelBlockInSecs3;
				}
				else
					total_time = 999999;
				node1 = outputNodes.get(outputNodes.size()-1);
			}//if ends
			else
			{
				total_time = 999999;
				res = 1;
			}
		}//while ends
		
		for(long node:outputNodes)
		{
			Node n = SpatioTemporal.nodeDetails.get(node);
			outputNodeList.add(n);
		}
				System.out.println(b.getBlockId()+","+d+","+total_time);
		
		return outputNodeList;
	}

	/* 	 * Checks for max force and adds the corresponding node. If the node already exists in the output list then it adds node with 2nd highest force value.
	 * If the node with 2nd highest force value already exists in the output list then add the last node.
	 * If the last node already exists in the output list then return false 
	 * If all nodes have equal force then we check for the min distance to each node.
	 * If all the nodes are equi-distant then we try to add first node; if it already exists then second node ; if the 2nd also already exists then we try to add 3rd node.
	 * If all the 3 nodes are already there in the output list then we return false
	 * If the node with least distance already exists in the output list then it adds node with 2nd minimum distance.
	 * If the node with 2nd  minimum distance already exists in the output list then add the last node.
	 * If the last node already exists in the output list then return false*/
	int findNextNodes(double force1, double force2, double force3,List<Integer> adjNodeList,int dist1,int dist2,int dist3)
	{
		boolean checkDist = false;
		if(force1 == force2 && force2 == force3)
			checkDist = true;
		else if(force1 > force2 && force1 > force3)
		{
			if(!outputNodes.contains(adjNodeList.get(0)))
			{
				outputNodes.add(adjNodeList.get(0));
			}
			else if(force2 > force3)
			{
				if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}
				else if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}
				else
					return -1;					
			}
			else //force3 > force2
			{
				if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}
				else if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}
				else
					return -1;
			}
		}
		else if(force2 > force1 && force2 > force3)
		{
			if(!outputNodes.contains(adjNodeList.get(2)))
			{
				outputNodes.add(adjNodeList.get(2));
			}
			else if(force1 > force3)
			{
				if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}
				else if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}
				else
					return -1;					
			}
			else  //force3 > force1
			{
				if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}
				else if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}
				else
					return -1;
			}
		}
		else if(force3 > force1 && force3 > force2)
		{
			if(!outputNodes.contains(adjNodeList.get(4)))
			{
				outputNodes.add(adjNodeList.get(4));
			}		
			else if(force1 > force2)
			{
				if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}
				else if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}
				else
					return -1;				
			}
			else //force2 > force1
			{
				if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}
				else if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}
				else
					return -1;					
			}
		}		
		if(checkDist)
		{
			/* If any distance is zero set it to infinity so that that node is not selected. */
			if(dist1 == 0)
				dist1 = Integer.MAX_VALUE;
			if(dist2 == 0)
				dist2 = Integer.MAX_VALUE;
			if(dist3 == 0)
				dist3 = Integer.MAX_VALUE;

			if(dist1 == dist2 && dist2 == dist3)
			{
				if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}
				else if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}
				else if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}
				else
					return -1;
			}
			else if(dist1 < dist2 && dist1 < dist3)
			{
				if(!outputNodes.contains(adjNodeList.get(0)))
				{
					outputNodes.add(adjNodeList.get(0));
				}		
				else if(dist2 < dist3)
				{
					if(!outputNodes.contains(adjNodeList.get(2)))
					{
						outputNodes.add(adjNodeList.get(2));
					}
					else if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else
						return -1;
				}
				else //dist3 > dist2
				{
					if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else if(!outputNodes.contains(adjNodeList.get(2)))
					{
						outputNodes.add(adjNodeList.get(2));
					}
					else
						return -1;
				}
			}

			else if(dist2 < dist1 && dist2 < dist3)
			{
				if(!outputNodes.contains(adjNodeList.get(2)))
				{
					outputNodes.add(adjNodeList.get(2));
				}		
				else if(dist1 < dist3)
				{
					if(!outputNodes.contains(adjNodeList.get(0)))
					{
						outputNodes.add(adjNodeList.get(0));
					}
					else if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else
						return -1;
				}
				else //dist3 < dist1
				{
					if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else if(!outputNodes.contains(adjNodeList.get(0)))
					{
						outputNodes.add(adjNodeList.get(0));
					}				
					else
						return -1;
				}
			}

			else if(dist3 < dist1 && dist3 < dist2)
			{
				if(!outputNodes.contains(adjNodeList.get(4)))
				{
					outputNodes.add(adjNodeList.get(4));
				}		
				else if(dist2 < dist3)
				{
					if(!outputNodes.contains(adjNodeList.get(2)))
					{
						outputNodes.add(adjNodeList.get(2));
					}
					else if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else
						return -1;
				}
				else //dist3 < dist2
				{
					if(!outputNodes.contains(adjNodeList.get(4)))
					{
						outputNodes.add(adjNodeList.get(4));
					}
					else if(!outputNodes.contains(adjNodeList.get(1)))
					{
						outputNodes.add(adjNodeList.get(1));
					}				
					else
						return -1;
				}
			}
		}

		if(checkDist)
			return 0;
		else
			return 1;
	}
	int getNumofSlots(List<Long> blkList, Date date1, Date date2)
	{
		int numOfSlots = 0;
		if(blkList.size() == 2)/* This will take care of the operational = 2 */
		{			
			for(int i=0;i<SpatioTemporal.projectionList.size();i++){
				if(SpatioTemporal.projectionList.get(i).getBlockId()==blkList.get(0) || SpatioTemporal.projectionList.get(i).getBlockId()==blkList.get(1))
				{
					Date projDate = SpatioTemporal.projectionList.get(i).getTimestamp();

					if((projDate.after(date1) && projDate.before(date2)) || projDate.equals(date1)|| projDate.equals(date2))
					{
						numOfSlots += SpatioTemporal.projectionList.get(i).getAvailableSlots();
					}
				}
			}
		}
		else if(blkList.size() == 1)
		{
			for(int i=0;i<SpatioTemporal.projectionList.size();i++){
				if(SpatioTemporal.projectionList.get(i).getBlockId()==blkList.get(0))
				{
					Date projDate = SpatioTemporal.projectionList.get(i).getTimestamp();
					if(projDate.after(date1) && projDate.before(date2))
					{
						numOfSlots += SpatioTemporal.projectionList.get(i).getAvailableSlots();
					}
				}
			}
		}

		return numOfSlots;	
	}
}
