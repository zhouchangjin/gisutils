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

    BEIJING54_ZONE13("EPSG:21413","北京54-13带","PCS"),
    BEIJING54_ZONE14("EPSG:21414","北京54-14带","PCS"),
    BEIJING54_ZONE15("EPSG:21415","北京54-15带","PCS"),
    BEIJING54_ZONE16("EPSG:21416","北京54-16带","PCS"),
    BEIJING54_ZONE17("EPSG:21417","北京54-17带","PCS"),
    BEIJING54_ZONE18("EPSG:21418","北京54-18带","PCS"),
    BEIJING54_ZONE19("EPSG:21419","北京54-19带","PCS"),
    BEIJING54_ZONE20("EPSG:21420","北京54-20带","PCS"),

    UTM_ZONE43N("EPSG:32643","UTMZONE-43带","PCS"),

    UTM_ZONE44N("EPSG:32644","UTMZONE-44带","PCS"),

    UTM_ZONE45N("EPSG:32645","UTMZONE-45带","PCS"),

    UTM_ZONE46N("EPSG:32646","UTMZONE-46带","PCS"),

    UTM_ZONE47N("EPSG:32647","UTMZONE-47带","PCS"),


    UTM_ZONE48N("EPSG:32648","UTMZONE-48带","PCS"),


    UTM_ZONE49N("EPSG:32649","UTMZONE-49带","PCS"),


    UTM_ZONE50N("EPSG:32650","UTMZONE-50带","PCS"),

    UNKNOWN("","未知","PCS");

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

    public String getType(){return type;}

}
