package com.iwhere.gisutil.converter.osm.model;

public class TagElement implements ISerializedString {
	
	String k; //key
	String v; //value;
	
	public TagElement() {
	}
	
	public TagElement(String key,String value) {
		k=key;
		v=value;
	}
	
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}

	@Override
	public String toXMLString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<tag k='").append(k).append("' v='").append(v).append("' />");
		sb.append(System.lineSeparator());
		return sb.toString();
	}

}
