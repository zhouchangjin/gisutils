package com.iwhere.gisutil.converter.osm.model.names;
/**
 * osm中固定的属性字段<br/>
 * fclass代表道路类型（旧版）<br/>
 * highway 道路类型 <br/>
 * name 道路名称   <br/>
 * oneway 单向双向 <br/>
 * @author zhouchangjin
 *
 */
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
