package com.src.spatiotemporal.Entity;

public class Block {

	//Block id	Block name	Longitude1	Laltitude1	Longitude2	Laltitude2
	//nodeId1	nodeId2	No_Blocks	Operational

	private long blockId;
	private String blockName;
	private Double blockLatitude1;
	private Double blockLongitude1;
	private Double blockLatitude2;
	private Double blockLongitude2;
	private long nodeId1;
	private long nodeId2;
	private int noOfBlocks;
	private int operational;
	
	public Block(){}
	
	public Block(long blockId, String blockName,Double blockLongitude1, Double blockLatitude1, Double blockLongitude2, Double blockLatitude2,
			 long nodeId1, long nodeId2,int noOfBlocks,int operational)
	{
		this.blockId = blockId;
		this.blockName = blockName;
		this.blockLatitude1 = blockLatitude1;
		this.blockLongitude1 = blockLongitude1;
		this.blockLatitude2 = blockLatitude2;
		this.blockLongitude2 = blockLongitude2;
		this.nodeId1 = nodeId1;
		this.nodeId2 = nodeId2;
		this.noOfBlocks = noOfBlocks;
		this.operational = operational;
	}
	
	public long getBlockId() {
		return blockId;
	}
	public void setBlockId(long blockId) {
		this.blockId = blockId;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public Double getBlockLatitude1() {
		return blockLatitude1;
	}
	public void setBlockLatitude1(Double blockLatitude1) {
		this.blockLatitude1 = blockLatitude1;
	}
	public Double getBlockLongitude1() {
		return blockLongitude1;
	}
	public void setBlockLongitude1(Double blockLongitude1) {
		this.blockLongitude1 = blockLongitude1;
	}
	public Double getBlockLatitude2() {
		return blockLatitude2;
	}
	public void setBlockLatitude2(Double blockLatitude2) {
		this.blockLatitude2 = blockLatitude2;
	}
	public Double getBlockLongitude2() {
		return blockLongitude2;
	}
	public void setBlockLongitude2(Double blockLongitude2) {
		this.blockLongitude2 = blockLongitude2;
	}
	public long getnodeId1() {
		return nodeId1;
	}
	public void setnodeId1(long nodeId1) {
		this.nodeId1 = nodeId1;
	}
	public long getnodeId2() {
		return nodeId2;
	}
	public void setnodeId2(long nodeId2) {
		this.nodeId2 = nodeId2;
	}
	public int getNoOfBlocks() {
		return noOfBlocks;
	}
	public void setNoOfBlocks(int noOfBlocks) {
		this.noOfBlocks = noOfBlocks;
	}
	public int getOperational() {
		return operational;
	}
	public void setOperational(int operational) {
		this.operational = operational;
	}

}
