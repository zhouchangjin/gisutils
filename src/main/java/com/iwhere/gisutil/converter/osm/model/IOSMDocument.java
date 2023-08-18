package com.iwhere.gisutil.converter.osm.model;

/**
 * OSM 文档的对应接口
 * @author zhouchangjin
 *
 */
public interface IOSMDocument {
	
	public int addWay(WayElement way);
	
	public int addNode(double longitude,double latitude);
	
	public int nodeCnt();
	
	public int wayCnt();
	
	public NodeElement getNode(int nodeIndex);
	
	public WayElement getWay(int wayIndex);

}
