package com.src.spatiotemporal.Algorithms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Main.SpatioTemporal;

public class Deterministic implements Algorithms{

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	/*F(v, b) =  nb / distance(v, b)2
			nb = number of slots available
			v = current position of the vehicle
			b = position of block*/

	@Override
	public List<Node> fetchRoute(long blockId,Date d) {
		
		//4/6/2012  12:03:12 AM
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("y-M-d H:m:s.S");
			try {
				d = format.parse("2012-04-06 0:03:12.0");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		//Current block details			
		Block b = SpatioTemporal.blockDetails.get(blockId);
		
		//Get adjacent nodes for the block
		TreeMap<Long,Integer> adjNodeDist1 = SpatioTemporal.nodeDistMap.get(b.getnodeId1());
		TreeMap<Long,Integer> adjNodeDist2 = SpatioTemporal.nodeDistMap.get(b.getnodeId2());
		
		
		ArrayList<Long> blockList = SpatioTemporal.dateAndBlockDetails.get(d);
		TreeMap<Long, TreeMap<Date, Integer>>  projDetails = SpatioTemporal.g.getCustomProjectionDetails();		
		
		for(Long block:blockList)
		{
			TreeMap<Date, Integer> blockDetails = projDetails.get(block);
			int numOfSlots = blockDetails.get(d);		
			
			System.out.println(block + "\t:"+ numOfSlots + "\t:" );
		}
		
		return null;
		
		
		/*Block b = Utils.findNeighbourhood(latitude, longitude);
		Block b1 = new Block(612312,"Polk St (3100-3198)",
				37.8054164152,-122.4236028968,37.8061265339,-122.423746731,7001,7002,2,8);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.S");
		Date d = null;
		try {
			d = format.parse("2012-04-06 0:19:35.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}*/
		
						
	}
	void calculateForce()
	{
		
	}
}
