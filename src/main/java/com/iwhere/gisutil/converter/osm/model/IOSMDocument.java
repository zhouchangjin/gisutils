package com.iwhere.gisutil.converter.osm.model;

public interface IOSMDocument {
	
	public int addWay(WayElement way);
	
	public int addNode(double longitude,double latitude);
	
	public int nodeCnt();
	
	public int wayCnt();
	
	public NodeElement getNode(int nodeIndex);
	
	public WayElement getWay(int wayIndex);

}
