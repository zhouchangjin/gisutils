package com.iwhere.gisutil.converter.osm;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.iwhere.gisutil.converter.osm.model.NodeElement;
import com.iwhere.gisutil.converter.osm.model.TagElement;
import com.iwhere.gisutil.converter.osm.model.WayElement;

public class OSMDocument {
	
	Document document;
	Element osmEle;
	
	Map<String,Integer> nodesetMap;
	
	ArrayList<NodeElement> nodelist;
	ArrayList<WayElement> waylist;
	
	int startNodeId=1;
	int startWayId=1;
	
	public String getLastNodeId() {
		return nodelist.get(nodelist.size()-1).getId();
	}
	
	public String getLastWayId() {
		return ""+waylist.get(waylist.size()-1).getId();
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

	public void processNode() {
		for(NodeElement ele :nodelist) {
			osmEle.addElement("node")
			.addAttribute("id", ele.getId()).addAttribute("lon",""+ele.getLon())
			.addAttribute("lat", ""+ele.getLat()).addAttribute("visible", ele.getVisible())
			.addAttribute("version", ""+ele.getVersion());
		}
	}
	
	public void processWay() {
		for(WayElement way:waylist) {
			Element wayxmlele= osmEle.addElement("way");
			wayxmlele.addAttribute("id", ""+way.getId());
			wayxmlele.addAttribute("version", ""+way.getVersion());
			List<Integer> nodelist=way.getNodeList();
			  for(Integer i:nodelist) {
				  wayxmlele.addElement("nd").addAttribute("ref", ""+i);
			  }
			  List<TagElement> taglist=way.getTags();
			  for(TagElement tag:taglist) {
				  wayxmlele.addElement("tag").addAttribute("k", tag.getK()).addAttribute("v", tag.getV());
			  }
		}
	}
	
	public void WriteFile(String path,String filename) {
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
        }
        try (FileOutputStream fos = new FileOutputStream(path+filename + ".osm")) {
            fos.write(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public OSMDocument() {
		initialize();
	}
	
	public int addWay(WayElement way) {
		int id=waylist.size()+startWayId;
		way.setId(id);
		waylist.add(way);
		return id;
	}
	
	public int addNode(double longitude,double latitude) {
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
	
	public void clearNode() {
		nodelist.clear();
	}
	
	public void initialize() {
		nodesetMap=new HashMap<String,Integer>();
		nodelist=new ArrayList<NodeElement>();
		waylist=new ArrayList<WayElement>();
		
		document = DocumentHelper.createDocument();
		osmEle = document.addElement("osm");
		osmEle.addAttribute("version", "0.6");
		osmEle.addAttribute("generator", "iwhere");
	}

}
