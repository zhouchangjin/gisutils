package com.iwhere.gisutil.converter.osm.model;

import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;
/**
 * 规则属性映射<br/>
 * 比如shapefile 里   width>10  osm里的字段值 highway=PRIMARY
 * @author zhouchangjin
 *
 */
public class RuleConfig {
	
	String type;
	
	String dataType;
	
	Integer valueA;
	
	Integer valueB;
	
	Double dvalueA;
	
	Double dvalueB;
	
	String value;
	
	String mapValue;
	
	FeatureClassEnum fclass;
	
	OnewayEnum oneway;

	public OnewayEnum getOneway() {
		return oneway;
	}

	public void setOneway(OnewayEnum oneway) {
		this.oneway = oneway;
	}

	public FeatureClassEnum getFclass() {
		return fclass;
	}

	public void setFclass(FeatureClassEnum fclass) {
		this.fclass = fclass;
	}

	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getValueA() {
		return valueA;
	}

	public void setValueA(Integer valueA) {
		this.valueA = valueA;
	}

	public Integer getValueB() {
		return valueB;
	}

	public void setValueB(Integer valueB) {
		this.valueB = valueB;
	}

	public Double getDvalueA() {
		return dvalueA;
	}

	public void setDvalueA(Double dvalueA) {
		this.dvalueA = dvalueA;
	}

	public Double getDvalueB() {
		return dvalueB;
	}

	public void setDvalueB(Double dvalueB) {
		this.dvalueB = dvalueB;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
