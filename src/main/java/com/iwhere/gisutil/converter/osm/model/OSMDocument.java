package com.iwhere.gisutil.converter.osm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OSMDocument implements IOSMDocument{
	
	Map<String,Integer> nodesetMap;
	ArrayList<NodeElement> nodelist;
	ArrayList<WayElement> waylist;
	
	int startNodeId;
	int startWayId;
	
	public OSMDocument() {
		nodesetMap=new HashMap<String,Integer>();
		nodelist=new ArrayList<NodeElement>();
		waylist=new ArrayList<WayElement>();
		startNodeId=1;
		startWayId=1;
	}

	@Override
	public int addWay(WayElement way) {
		int id=waylist.size()+startWayId;
		way.setId(id);
		waylist.add(way);
		return id;
	}

	@Override
	public int addNode(double longitude, double latitude) {
		String key=Double.toString(longitude)+"_"+Double.toString(latitude);
		if(nodesetMap.keySet().contains(key)) {
			return nodesetMap.get(key);
		}else {
			NodeElement ele=new NodeElement();
			ele.setLon(longitude);
			ele.setLat(latitude);
			ele.setAction("");
			ele.setVisible("true");
			int id=nodelist.size()+startNodeId;
			ele.setId(""+id);
			nodelist.add(ele);
			nodesetMap.put(key, id);
			return id;
		}
	}


	@Override
	public int nodeCnt() {
		return nodelist.size();
	}

	@Override
	public int wayCnt() {
		return waylist.size();
	}

	@Override
	public NodeElement getNode(int nodeIndex) {
		return nodelist.get(nodeIndex);
	}

	@Override
	public WayElement getWay(int wayIndex) {
		return waylist.get(wayIndex);
	}
	
	

}
