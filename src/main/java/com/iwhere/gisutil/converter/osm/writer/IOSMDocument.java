package com.iwhere.gisutil.converter.osm.writer;

import com.iwhere.gisutil.converter.osm.model.WayElement;

public interface IOSMDocument {
	
	public int addWay(WayElement way);
	
	public int addNode(double longitude,double latitude);
	
	public void writeHeader();
	
	public void writeNode(int nodeindex);
	
	public void writeWay(int wayindex);
	
	public void writeDocEnd();
	
	public int nodeCnt();
	
	public int wayCnt();

}
