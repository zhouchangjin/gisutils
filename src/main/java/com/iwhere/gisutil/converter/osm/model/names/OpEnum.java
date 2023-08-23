package com.iwhere.gisutil.converter.osm.model.names;

public enum OpEnum {
	
	EQ("="),
	GT(">"),
	LT("<"),
	GTE(">="),
	LTE("<="),
	NEQ("!=");
	
	OpEnum(String name) {
		op=name;
	}
	
	public String GetOp() {
		return op;
	}
	
	String op;

}
