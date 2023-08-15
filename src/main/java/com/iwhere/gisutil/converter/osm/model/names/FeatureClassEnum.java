package com.iwhere.gisutil.converter.osm.model.names;

/**
 * FeatureClass 要素类型
 * OSM格式文件的类型属性的可用值
 * @author zhouchangjin
 * 
 */
public enum FeatureClassEnum {
	TERTIARY("tertiary"), //三级道路城市支路
    TERTIARY_LINK("tertiary_link"), //三级道路连接
	UNCLASSIFIED("unclassified"), //未分类
    SECONDARY("secondary"), //二级道路
    SECONDARY_LINK("secondary_link"), //二级道路连接
    PRIMARY("primary"), //主要道路
    PRIMARY_LINK("primary_link"),//主要道路连接
    MOTORWAY("motorway"), //高速公路
    MOTORWAY_LINK("motorway_link"),//高速公路连接
    TRUNK("trunk"),//干道
    TRUNK_LINK("trunk_link"),//干道连接
    TRACK("track"),//小路
    TRACK_GRADE1("track_grade1"), //小路级别1
    TRACK_GRADE2("track_grade2"), //小路级别2
    TRACK_GRADE3("track_grade3"), //小路级别3
    TRACK_GRADE4("track_grade4"), //小路级别4
    TRACK_GRADE5("track_grade5"), //小路级别5
    BRIDLEWAY("bridleway"), //马道
    LIVING_STREET("living_street"),//生活街道
    PATH("path"),//小道
    SERVICE("service"),//服务性道路
    FOOTWAY("footway"),//人行道
    PEDESTRAIN("pedestrain"),//步行街道
    STEPS("steps"),//台阶踏步
    CYCLEWAY("cycleway"),//自行车道
    UNKNOWN("unknown"); //未知街道
	private String fclass;
	
	FeatureClassEnum(String string) {
		fclass=string;
	}
	
	public String getFClass() {
		return fclass;
	}
	

}

