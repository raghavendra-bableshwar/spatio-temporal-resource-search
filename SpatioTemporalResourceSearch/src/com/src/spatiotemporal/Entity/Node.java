package com.src.spatiotemporal.Entity;

public class Node {
	private int nodeId;
	private double nodeLatitude;
	private double nodeLongitude;
	private String nodeName;
	
	public Node(){	}
	
	public Node(int nodeId, double nodeLatitude, double nodeLongitude,String nodeName)
	{
		this.nodeId = nodeId;
		this.nodeLatitude = nodeLatitude;
		this.nodeLongitude = nodeLongitude;
		this.nodeName = nodeName;
	}
	
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public double getNodeLatitude() {
		return nodeLatitude;
	}
	public void setNodeLatitude(double nodeLatitude) {
		this.nodeLatitude = nodeLatitude;
	}
	public double getNodeLongitude() {
		return nodeLongitude;
	}
	public void setNodeLongitude(double nodeLongitude) {
		this.nodeLongitude = nodeLongitude;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


}
