package com.iwhere.gisutil.converter.osm.model.names;


public enum DataType {
    String("java.lang.String"),
    Integer("java.lang.Integer"),

    Double("java.lang.Double");

    DataType(String classType){
        this.classType=classType;
    }
    String classType;

    String getClassType(){
        return classType;
    }
}
