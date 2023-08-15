package com.iwhere.gisutil.converter.osm.model.names;

public enum OnewayEnum {

	F("1"),      // 单行线 与LineString 顺序相一致，
	T("-1"),    // 单行线  与LineString 逆向顺序一致 
	B("yes");  // 双向车道
	
	String type;
	private OnewayEnum(String tyStr) {
		type=tyStr;
	}

	public String getType() {
		return type;
	}
}
