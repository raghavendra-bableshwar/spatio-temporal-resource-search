package com.src.spatiotemporal.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class Projection {

	private long blockId;
	private int availableSlots;
	private Date timestamp;
	private static TreeMap<Long,ArrayList<Date>> blockIdTimeStampMap = new TreeMap<Long,ArrayList<Date>>();
	
	public Projection(){}
	
	public Projection(long blockId,int availableSlots, Date timestamp)
	{
		this.blockId = blockId;
		this.availableSlots = availableSlots;
		this.timestamp = timestamp;
		if (Projection.blockIdTimeStampMap.containsKey(blockId)){
			Projection.blockIdTimeStampMap.get(blockId).add(timestamp);
		}
		else{
			ArrayList<Date> dlist = new ArrayList<Date>();
			dlist.add(timestamp);
			Projection.blockIdTimeStampMap.put(blockId, dlist);
		}
	}
	
	public long getBlockId() {
		return blockId;
	}
	public void setBlockId(long blockId) {
		this.blockId = blockId;
	}
	public int getAvailableSlots() {
		return availableSlots;
	}
	public void setAvailableSlots(int availableSlots) {
		this.availableSlots = availableSlots;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
