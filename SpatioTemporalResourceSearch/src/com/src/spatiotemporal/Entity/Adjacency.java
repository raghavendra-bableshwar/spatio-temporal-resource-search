package com.src.spatiotemporal.Entity;

public class Adjacency {
	private int nodeId;
	private double nodeLatitude;
	private double nodeLongitude;
	private String nodeName;
	
	//new fields
	private int n1;
	private int d1;
	private int n2;
	private int d2;
	private int n3;
	private int d3;
	
	public Adjacency(){	}
	
	public Adjacency(int nodeId, double nodeLatitude, double nodeLongitude,String nodeName,
					 int n1,int d1, int n2,int d2,int n3,int d3)
	{
		this.nodeId = nodeId;
		this.nodeLatitude = nodeLatitude;
		this.nodeLongitude = nodeLongitude;
		this.nodeName = nodeName;
		this.n1=n1;
		this.d1=d1;
		this.n2=n2;
		this.d2=d2;
		this.n3=n3;
		this.d3=d3;
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
	
	public int getNodeN1(){
		return n1;
	}
	public int getDistanceD1(){
		return d1;
	}
	public int getNodeN2(){
		return n2;
	}
	public int getDistanceD2(){
		return d2;
	}
	public int getNodeN3(){
		return n3;
	}
	public int getDistanceD3(){
		return d3;
	}

}
