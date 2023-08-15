package com.iwhere.gisutil.converter.osm.model;

public class TagElement {
	
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

}
