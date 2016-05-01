package com.src.spatiotemporal.Algorithms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Main.SpatioTemporal;
import com.src.spatiotemporal.utils.Utils;

public class Probabilistic  implements Algorithms {
	
	private ArrayList<Integer> mtraversalList=new ArrayList<Integer>();	
	public static int total_time=0;
	
	public Block findBlockObject(long inputBlockID){
		for(int i=0;i<SpatioTemporal.blockList.size();i++){
			if(inputBlockID==SpatioTemporal.blockList.get(i).getBlockId()){
				return SpatioTemporal.blockList.get(i);
	       }	
		}
		return null;
	}
	
	public ArrayList<Integer> getAdjacentNodesAndDistances(int nodeId){
		ArrayList<Integer> from_nd_list=new ArrayList<Integer>();
		for(int i=0;i<SpatioTemporal.adjacencyList.size();i++){
			if(SpatioTemporal.adjacencyList.get(i).getNodeId() == nodeId){
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
		
	public Date addSecondsToTime (String inputTime,int time_in_seconds){
		Date originalDate = null;
		/*VISHALXI-CHANGE*/
	   DateFormat df = new SimpleDateFormat("y-M-d H:m:s.S");
	    try{
	    	originalDate = df.parse(inputTime);
	    }
	    catch ( Exception ex ){
	        //System.out.println(ex);
	    }
	    Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(originalDate.getTime());
        cal.add(Calendar.SECOND, time_in_seconds);
        Date laterTime=cal.getTime();
        //System.out.println("Org="+df.format(originalDate));
        //System.out.println("later"+df.format(laterTime));
        
		return laterTime;
	}
	
	public Boolean isSlotAvailableAtRange(Date startDate,Date endDate,List<Long> blockIDList){
		Date projDate;
		//System.out.println("Start date="+startDate);
		//System.out.println("End Date ="+endDate);
		//System.out.println("blockID ="+blockIDList);
		
		for(int j=0;j<blockIDList.size();j++){
			for(int i=0;i<SpatioTemporal.projectionList.size();i++){
				if(SpatioTemporal.projectionList.get(i).getBlockId()==blockIDList.get(j))
				{
					projDate=SpatioTemporal.projectionList.get(i).getTimestamp();
					if(projDate.after(startDate) && projDate.before(endDate)) {
					    if(SpatioTemporal.projectionList.get(i).getAvailableSlots()!=0){
					    	//System.out.println("Time stamp:"+projDate);
					    	return true;
					    }
					}
					if(projDate.equals(startDate)|| projDate.equals(endDate)){
						if(SpatioTemporal.projectionList.get(i).getAvailableSlots()!=0){
							//System.out.println("Time stamp_:"+projDate);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public String getUpdatedInputTime(int currentNodeId,int lastNodeId,ArrayList<Integer> node_dist_list,String inputTime){
		int dist_btw_nodes=0;
		if(lastNodeId==node_dist_list.get(0))
		 dist_btw_nodes=node_dist_list.get(1);
		 else if (lastNodeId==node_dist_list.get(2))
			dist_btw_nodes=node_dist_list.get(3);
		 else if(lastNodeId==node_dist_list.get(4))
			dist_btw_nodes=node_dist_list.get(5);
		//System.out.println("Distance between"+currentNodeId+"and"+ lastNodeId+"="+dist_btw_nodes);
		int time_in_seconds=dist_btw_nodes*36/528;
		//int time_in_seconds=240;
		//System.out.println("Time in seconds="+time_in_seconds);
		Date endDate=addSecondsToTime(inputTime,time_in_seconds);
		SimpleDateFormat formatter = new SimpleDateFormat("y-M-d H:m:s.S");
		inputTime = formatter.format(endDate);
		return inputTime;	
	}
	
	public Boolean isSlotAvailable(int currentNodeId,int lastNodeId,ArrayList<Integer> node_dist_list,String inputTime){
		//add the x to timestamp  inputTime
		//x is Distance*36/528
		Date startDate=null;
		int dist_btw_nodes=0;
		if(lastNodeId==node_dist_list.get(0))
		 dist_btw_nodes=node_dist_list.get(1);
		 else if (lastNodeId==node_dist_list.get(2))
			dist_btw_nodes=node_dist_list.get(3);
		 else if(lastNodeId==node_dist_list.get(4))
			dist_btw_nodes=node_dist_list.get(5);
		
		//System.out.println("Distance between"+currentNodeId+"and"+ lastNodeId+"="+dist_btw_nodes);
		int time_in_seconds=dist_btw_nodes*36/528;
		//int time_in_seconds=240;
		//System.out.println("Time in seconds="+time_in_seconds);
		total_time=total_time+time_in_seconds;
		//System.out.println("TOTAL TIME="+total_time);
		Date endDate=addSecondsToTime(inputTime,time_in_seconds);
		List<Long> blockIDList=new ArrayList<Long>();
		blockIDList=getBlockIDList(currentNodeId,lastNodeId);
		//System.out.println("isSlotAvailable() block ID found is: "+blockIDList);
		
		DateFormat df = new SimpleDateFormat("y-M-d H:m:s.S");
	    try{
	    	startDate = df.parse(inputTime);
	    }
	    catch ( Exception ex ){
	        //System.out.println(ex);
	    }		
		if(isSlotAvailableAtRange(startDate,endDate,blockIDList)){	
			return true;
		}
		else{
			return false;
		}
	}
	
	public Long getBlockID(int n1,int n2){
		int node1,node2;
		if(n1==0||n2==-1)
			return (long)-1;
		//TO ensure that from nodeID is smaller than to nodeID
		if(n1<n2){
			node1=n1;
			node2=n2;
		}else{
			node1=n2;
			node2=n1;
		}
		for(int i=0;i<SpatioTemporal.blockList.size();i++){
			if((int)SpatioTemporal.blockList.get(i).getnodeId1()== node1 &&
					((int)SpatioTemporal.blockList.get(i).getnodeId2()== node2)){
				return SpatioTemporal.blockList.get(i).getBlockId();
				
			}
		}
		return (long)-1;
	}
	public double getRatioOfDistanceToExpectedslots(int currentNodeId,int nextNode,int node_dist,String inputTime_sub){
		double ratio_dist_slot=0.0;
		double avg_s=0.0;
		Long b1_id=getBlockID(currentNodeId,nextNode);
		//System.out.println("____________________________First: currentNodeId="+currentNodeId+"FirstNodeId="+nextNode+" Block detected="+b1_id);
		if(b1_id !=-1){
			avg_s=Utils.getAverageExpectedSlots_1(b1_id,inputTime_sub);
			//System.out.println("____________________________Average is : "+avg_s);
			if(avg_s!=0.0){
				ratio_dist_slot=avg_s/node_dist;
				//System.out.println("____________________________Ratio is : "+ratio_dist_slot);
			}
		}
		return ratio_dist_slot;	
	}
	
	public List<Long> getBlockIDList(int n1,int n2){
		int node1,node2;
		List<Long> blockIDList=new ArrayList<Long>();
		if(n1==0||n2==-1)
			return null;
		//TO ensure that from nodeID is smaller than to nodeID
		if(n1<n2){
			node1=n1;
			node2=n2;
		}else{
			node1=n2;
			node2=n1;
		}
		for(int i=0;i<SpatioTemporal.blockList.size();i++){
			if((int)SpatioTemporal.blockList.get(i).getnodeId1()== node1 &&
					((int)SpatioTemporal.blockList.get(i).getnodeId2()== node2)){
				try{
					blockIDList.add(SpatioTemporal.blockList.get(i).getBlockId());
				}catch(Exception e){
					
				}
							
			}
		}
		return blockIDList;
	}
	
	public List<Double> getRatioOfDistanceToExpectedslotsList(int currentNodeId,int nextNode,int node_dist,String inputTime_sub){

		double avg_s=0.0;
		List<Double> ratioList=new ArrayList<Double>();
		List<Long>blockIDList=new ArrayList<Long>();
		
		blockIDList=getBlockIDList(currentNodeId,nextNode);
		//System.out.println("Calculating ratio For each Block.........................................................................");
		//System.out.println("____________________________First: currentNodeId="+currentNodeId+"FirstNodeId="+nextNode+" Block detected="+blockIDList);
		if(blockIDList !=null){
			for(int i=0;i<blockIDList.size();i++){
				avg_s=Utils.getAverageExpectedSlots_1(blockIDList.get(i),inputTime_sub);
				//System.out.println("____________________________For BlockID:"+blockIDList.get(i)+"Average is : "+avg_s);
				if(avg_s!=0.0){
					ratioList.add(avg_s/node_dist);
					//System.out.println("____________________________For BlockID:"+blockIDList.get(i)+"Ratio is : "+ratioList.get(i));
				}
			}
			return ratioList;	
		}
		return null;
	}

	public int getStartingNodeId(int fromNodeId,int toNodeId,String inputTime_sub){
		int index=-1;
		ArrayList<Integer> node_dist_list_start=getAdjacentNodesAndDistances(fromNodeId);
		
		double ratio_dist_slot1=0.0,ratio_dist_slot2=0.0,ratio_dist_slot3=0.0;
		ratio_dist_slot1=getRatioOfDistanceToExpectedslots(fromNodeId,node_dist_list_start.get(0),node_dist_list_start.get(1),inputTime_sub);
		ratio_dist_slot2=getRatioOfDistanceToExpectedslots(fromNodeId,node_dist_list_start.get(2),node_dist_list_start.get(3),inputTime_sub);
		ratio_dist_slot3=getRatioOfDistanceToExpectedslots(fromNodeId,node_dist_list_start.get(4),node_dist_list_start.get(5),inputTime_sub);
		//System.out.println("r1="+ratio_dist_slot1+"r2="+ratio_dist_slot2+"r3="+ratio_dist_slot3);
		
		ArrayList<Integer> node_dist_list_end=getAdjacentNodesAndDistances(toNodeId);
		
		double ratio_dist_slot4=0.0,ratio_dist_slot5=0.0,ratio_dist_slot6=0.0;
		ratio_dist_slot4=getRatioOfDistanceToExpectedslots(toNodeId,node_dist_list_end.get(0),node_dist_list_end.get(1),inputTime_sub);
		ratio_dist_slot5=getRatioOfDistanceToExpectedslots(toNodeId,node_dist_list_end.get(2),node_dist_list_end.get(3),inputTime_sub);
		ratio_dist_slot6=getRatioOfDistanceToExpectedslots(toNodeId,node_dist_list_end.get(4),node_dist_list_end.get(5),inputTime_sub);
		//System.out.println("r4="+ratio_dist_slot4+"r5="+ratio_dist_slot5+"r6="+ratio_dist_slot6);
		
		 List<Double> ratioList=new ArrayList<Double>();
		 ratioList.add(ratio_dist_slot1);
		 ratioList.add(ratio_dist_slot2);
		 ratioList.add(ratio_dist_slot3);
		 ratioList.add(ratio_dist_slot4);
		 ratioList.add(ratio_dist_slot5);
		 ratioList.add(ratio_dist_slot6);
		 		 
		 
		 double max;
		 if(ratioList.size()!=0)
			  max=Collections.max(ratioList);
		 else
			 max=0.0;
		 
		 if(max==0.0){
			 return fromNodeId;
		 }

		 if(ratioList.contains(max)){
		        index=ratioList.indexOf(max); 
		        //System.out.println("the index is:"+index+"maximum ratio is:"+max);
		 }
		 if(index>=0 && index<=2){
			 return fromNodeId;
		 }
		 if(index>=3 && index<=5){
			 return toNodeId;		
		 }
		 return -1;
	}
	
	public int addMaximumRatioOfBlockPairToTraversalList(List<Double> ratioL1,List<Double> ratioL2,List<Double> ratioL3,ArrayList<Integer> node_dist_list){
		 List<Double> ratioList=new ArrayList<Double>();
		 double maxRatio=0.0;
		 //System.out.println("Size="+ratioL1.size()+"Ratio1="+ratioL1+"Ratio2="+ratioL2+"Ratio3="+ratioL3);
		 if(ratioL1.size()!=0)
			 ratioList.addAll(ratioL1);
		 if(ratioL2.size()!=0)
			 ratioList.addAll(ratioL2);
		 if(ratioL3.size()!=0)
			 ratioList.addAll(ratioL3);
		 if(ratioList.size()!=0)
			 maxRatio=Collections.max(ratioList);
		 if(maxRatio==0.0){
			//System.out.println("addMaximumRatioToTraversalList()"+ "All ratios are zero");
			int dist1=node_dist_list.get(1);
			int dist2=node_dist_list.get(3);
			int dist3=node_dist_list.get(5);
			if(dist1==0)
				dist1=Integer.MAX_VALUE;
			if(dist2==0)
				dist2=Integer.MAX_VALUE;
			if(dist3==0)
				dist3=Integer.MAX_VALUE;
			
			if(dist1<dist2 && dist1<dist3){
				mtraversalList.add(node_dist_list.get(0));
				return node_dist_list.get(0);
			}else if(dist2<dist1 && dist2<dist3){
				mtraversalList.add(node_dist_list.get(2));
				return node_dist_list.get(2);
			}else if(dist3<dist1 && dist3<dist2){
				mtraversalList.add(node_dist_list.get(4));
				return node_dist_list.get(4);
			}
			//return -1;
		 }
		 
		 if(ratioL1.contains(maxRatio)){
			mtraversalList.add(node_dist_list.get(0));
			return node_dist_list.get(0);
		 }else if(ratioL2.contains(maxRatio)){
			mtraversalList.add(node_dist_list.get(2));
			return node_dist_list.get(2);	
		 }else if(ratioL3.contains(maxRatio)){
			mtraversalList.add(node_dist_list.get(4));
			return node_dist_list.get(4);
		 }else{
			 //System.out.println("addMaximumRatioToTraversalList()"+ "All ratios are zero can't move forward");
			return -1;
		 }
		}
	
	
	@Override
	public List<Node> fetchRoute(long inputBlockID, Date inputTime_date){
		
		//Take only the date part of timestamp to get average expected slots
		
		/*VISHALAXI-CHANGE*/
		SimpleDateFormat formatter = new SimpleDateFormat("y-M-d H:m:s.S");
		String inputTime = formatter.format(inputTime_date);
		
		SimpleDateFormat formatter_2 = new SimpleDateFormat("H");
		String inputTime_sub = formatter_2.format(inputTime_date);	
		/*VISHALAXI-CHANGE*/
		//String inputTime_sub= inputTime.substring(0, 8);
		
		List<Node> node_list=new ArrayList<Node>();
		int fromNodeId=0,toNodeId=0,lastNodeId=0;
		Boolean parkingFound=false;
		List<Double> ratio_dist_slotL1=null,ratio_dist_slotL2=null,ratio_dist_slotL3=null;
		int currentNodeId=-1;
		
		//Get the Block Object to obtain start and end node IDs
		Block inputBlock=findBlockObject(inputBlockID);
		if(inputBlock!=null){
			fromNodeId=(int)inputBlock.getnodeId1();
			toNodeId=(int)inputBlock.getnodeId2();
			//System.out.println("Starting Node:"+fromNodeId+"Ending Node:"+toNodeId);
		}		
		else{
			//System.out.println("Input block object is null for block id="+inputBlockID);   
		}
		//Decide which node to select as the starting point
		currentNodeId=getStartingNodeId(fromNodeId,toNodeId,inputTime_sub);
		//System.out.println("Current node returned is:"+currentNodeId);
		mtraversalList.add(currentNodeId);
		
		while(parkingFound==false){ 
	
			//System.out.println("#######################################################################################################");
			ArrayList<Integer> node_dist_list=getAdjacentNodesAndDistances(currentNodeId);
			//System.out.println("_______________________Adjacency List for node="+currentNodeId+" is:"+node_dist_list);
			
			ratio_dist_slotL1=getRatioOfDistanceToExpectedslotsList(currentNodeId,node_dist_list.get(0),node_dist_list.get(1),inputTime_sub);
			ratio_dist_slotL2=getRatioOfDistanceToExpectedslotsList(currentNodeId,node_dist_list.get(2),node_dist_list.get(3),inputTime_sub);
			ratio_dist_slotL3=getRatioOfDistanceToExpectedslotsList(currentNodeId,node_dist_list.get(4),node_dist_list.get(5),inputTime_sub);
			
			lastNodeId=addMaximumRatioOfBlockPairToTraversalList(ratio_dist_slotL1,ratio_dist_slotL2,ratio_dist_slotL3,node_dist_list);
			if(lastNodeId==-1){
				//System.out.println("##################################Stopping the algorithm##################################");
				total_time=99999;
				break;
			}
		    
			if(isSlotAvailable(currentNodeId,lastNodeId,node_dist_list,inputTime))
			{
				//System.out.println("##################################PARKING FOUND AT BLOCK ID:"+getBlockID(currentNodeId,lastNodeId)+"##################################");
				parkingFound=true;
				
				break;
			}
			currentNodeId=lastNodeId;
			inputTime=getUpdatedInputTime(currentNodeId,lastNodeId,node_dist_list,inputTime);
			ratio_dist_slotL1=null;
			ratio_dist_slotL2=null;
			ratio_dist_slotL3=null;
		
		}
		if(total_time==99999){
			Set<Integer> nodeSet = new HashSet<Integer>(mtraversalList);
			mtraversalList.clear();
			mtraversalList.addAll(nodeSet);
		}
		
		//System.out.println("##################################TRAVERSAL LIST"+ mtraversalList+"######################################" );
		for(int i=0;i<mtraversalList.size();i++){
			//Node n = SpatioTemporal.nodeDetails.get(7001);
			Node n=getNodeFromNodeId(mtraversalList.get(i));
			if(n!=null){
				node_list.add(n);
			}
			
		}
		//System.out.println("Traversal list"+mtraversalList);		
		//System.out.println(inputBlockID+","+inputTime_date+","+total_time+","+mtraversalList);
		System.out.println(inputBlockID+","+inputTime_date+","+total_time);
		return 	node_list;
		
	}
	public Node getNodeFromNodeId(int nodeId){
		for(int i=0;i<SpatioTemporal.nodeList.size();i++){
			if(nodeId==SpatioTemporal.nodeList.get(i).getNodeId()){
				return SpatioTemporal.nodeList.get(i);
			}
		}
		return null;
	}
	 @Override
	 public void move(){
		 
	 }
	
}
