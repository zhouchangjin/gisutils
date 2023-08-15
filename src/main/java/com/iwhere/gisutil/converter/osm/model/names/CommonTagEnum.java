package com.iwhere.gisutil.converter.osm.model.names;

public enum CommonTagEnum {
	
	CLASS_TAG("fclass"),
	MAXSPEED_TAG("maxspeed"),
	HIGHWAY_TAG("highway"),
	NAME_TAG("name"),
	ONEWAY_TAG("oneway");
	
	private String tag;
	
	CommonTagEnum(String string) {
		tag=string;
	}
	
	public String getTag() {
		return tag;
	}

}
