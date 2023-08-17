package com.iwhere.gisutil.converter.osm.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.iwhere.gisutil.converter.osm.model.NodeElement;
import com.iwhere.gisutil.converter.osm.model.WayElement;

public class SimpleXMLOSMDocument implements IOSMDocument {
	
	Map<String,Integer> nodesetMap;
	ArrayList<NodeElement> nodelist;
	ArrayList<WayElement> waylist;
	
	int startNodeId=1;
	int startWayId=1;
	
	String filePath;
	String fileName;
	
	BufferedWriter docWriter=null;
	FileWriter fileWriter=null;
	
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(int startNodeId) {
		this.startNodeId = startNodeId;
	}

	public int getStartWayId() {
		return startWayId;
	}

	public void setStartWayId(int startWayId) {
		this.startWayId = startWayId;
	}
	
	public SimpleXMLOSMDocument(String path,String file) {
		this.fileName=file;
		this.filePath=path;
	}

	public SimpleXMLOSMDocument() {
		initialize();
	}
	
	public void close() {
		try {
			
			if(fileWriter!=null) {
				fileWriter.close();
			}
			if(docWriter!=null) {
				docWriter.close();
			}
		}catch(IOException e) {
			
		}		
	}
	
	public void open() {
		if(docWriter!=null) {
			
		}else {
			try {
				fileWriter =new FileWriter(new File(filePath+fileName+".osm"));
				docWriter=new BufferedWriter(fileWriter);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initialize() {
		nodesetMap=new HashMap<String,Integer>();
		nodelist=new ArrayList<NodeElement>();
		waylist=new ArrayList<WayElement>();
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
	
	private void nextLine(StringBuilder sb) {
		sb.append(System.getProperty("line.separator"));
	}

	@Override
	public void writeHeader() {
		StringBuilder sb=new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		nextLine(sb);
		sb.append("<osm version='0.6' generator='IWHEREOSM'>");
		nextLine(sb);
		try {
			docWriter.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeNode(int nodeindex) {
		
		NodeElement el=nodelist.get(nodeindex);
		try {
			docWriter.append(el.toXMLString());
			docWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
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
	public void writeWay(int wayindex) {
		WayElement way=waylist.get(wayindex);
		try {
			docWriter.append(way.toXMLString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flushDoc() {
		try {
			docWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeDocEnd() {
		try {
			docWriter.append("</osm>");
			docWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
