package com.iwhere.gisutil.converter.osm.model;
/**
 * 地图的范围属性
 * @author zhouchangjin
 *
 */
public class BoundsElement implements ISerializedString{
	
	double minlat;
	double maxlat;
	double minlon;
	double maxlon;
	
	public double getMinlat() {
		return minlat;
	}
	public void setMinlat(double minlat) {
		this.minlat = minlat;
	}
	public double getMaxlat() {
		return maxlat;
	}
	public void setMaxlat(double maxlat) {
		this.maxlat = maxlat;
	}
	public double getMinlon() {
		return minlon;
	}
	public void setMinlon(double minlon) {
		this.minlon = minlon;
	}
	public double getMaxlon() {
		return maxlon;
	}
	public void setMaxlon(double maxlon) {
		this.maxlon = maxlon;
	}
	@Override
	public String toXMLString() {
		//TODO 未完成
		return "";
	}
	
	

}
