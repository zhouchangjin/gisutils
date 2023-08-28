package com.iwhere.gisutil.converter.osm.model.names;


public enum DataTypeEnum {
    String("java.lang.String"),
    Integer("java.lang.Integer"),

    Double("java.lang.Double");

    DataTypeEnum(String classType){
        this.classType=classType;
    }
    String classType;

    String getClassType(){
        return classType;
    }
}
