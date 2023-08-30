package com.iwhere.gisutil.crs;

/**
 * 常用坐标系的枚举类型
 */
public enum CRSEnum {
    //84 大地坐标系
    WGS84("EPSG:4326","WGS84","GCS"),

    //国家2000大地坐标系 经纬度
    CGCS2000_GCS("EPSG:4490","国家2000大地坐标系","GCS"),
    //国家2000投影坐标系 单位为米
    CGCS2000_PCS("EPSG:4479","国家2000投影坐标系","PCS"),

    //墨卡托
    MERCATOR("EPSG:3857","墨卡托投影","PCS"),

    BEIJING54("EPSG:21420","北京54-1","PCS");

    CRSEnum(String code,String cName,String type){
        this.code=code;
        this.cname =cName;
        this.type=type;

    }

    /**
     * EPSG编码
     */
    String code;
    /**
     * 坐标系名称
     */
    String cname;
    /**
     * 坐标系类型
     */
    String type;

    public String getCode() {
        return code;
    }

    public String getCname() {
        return cname;
    }
}
