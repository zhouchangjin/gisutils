package com.iwhere.gisutil.converter.osm.model;

import java.util.ArrayList;
import java.util.List;

import com.iwhere.gisutil.converter.osm.model.names.CommonTagEnum;
import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;

/**
 * 对应osm文件中的way元素<br/>
 * way对应shapefile里的点线面对象<br/>
 * 对于Graphhopper来说，路网的主要数据要素类型是LineString<br/>
 * @author zhouchangjin
 *
 */
public class WayElement implements ISerializedString {

	
	int id;
	int version=1;
	String timestamp;

	List<Integer> nodesRef;
	
	List<TagElement> taglist;
	
	public WayElement() {
		nodesRef=new ArrayList<Integer>();
		taglist=new ArrayList<TagElement>();
	}
	
	public void AddTag(TagElement tag) {
		taglist.add(tag);
	}
	
	public void addTag(String k,String v) {
		TagElement tag=new TagElement(k,v);
		taglist.add(tag);
	}
	
	public List<TagElement> getTags(){
		return taglist;
	}
	
	public void AddRefNode(int nodeId) {
		nodesRef.add(nodeId);
	}
	
	public List<Integer> getNodeList(){
		return nodesRef;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void addHighwayClassTag(FeatureClassEnum classType) {
		addTag(CommonTagEnum.HIGHWAY_TAG.getTag(), classType.getFClass());
	}
	
	public void addFeatureClassTag(FeatureClassEnum classType) {
		   addTag(CommonTagEnum.CLASS_TAG.getTag(), classType.getFClass());
	}
	
	public void addOnewayTag(OnewayEnum onewy) {
		  addTag(CommonTagEnum.ONEWAY_TAG.getTag(), onewy.getType());
	}
	
	public void addNameTag(String name) {
		addTag(CommonTagEnum.NAME_TAG.getTag(), name);
	}

	@Override
	public String toXMLString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<way id='").append(id)
			.append("' visible='true' version='")
			.append(version).append("'>");
		sb.append(System.lineSeparator());
		for(int refid:nodesRef) {
			sb.append("<nd ref='").append(refid)
			.append("' />");
			sb.append(System.lineSeparator());
		}
		for(TagElement t:taglist) {
			sb.append(t.toXMLString());
		}
		sb.append("</way>");
		sb.append(System.lineSeparator());
		return sb.toString();
	}
}
