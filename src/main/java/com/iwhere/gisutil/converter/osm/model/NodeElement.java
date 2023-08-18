package com.iwhere.gisutil.converter.osm.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 对应Openstreetmap osm文件中的<node>元素
 * @author zhouchangjin
 *
 */
public class NodeElement implements ISerializedString{
	
	String id;
	String action;
	String visible;
	double lon;
	double lat;
	String timestamp;
	int version=1;
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	List<TagElement> taglist;
	
	public NodeElement() {
		taglist=new ArrayList<TagElement>();
	}
	
	public void AddTag(TagElement tag) {
		taglist.add(tag);
	}
	
	public void AddTag(String k,String v) {
		TagElement tag=new TagElement(k,v);
		taglist.add(tag);
	}
	
	List<TagElement> getTags(){
		return taglist;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	@Override
	public String toXMLString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<node id='").append(id)
		.append("' visible='").append(visible)
		.append("' version='").append(version)
		.append("' lat='").append(lat)
		.append("' lon='").append(lon).append("' />");
		return sb.toString();
	}

}
