package com.iwhere.gisutil.converter.osm.model;

import java.util.List;

public class MappingRuleConfig <T extends Comparable<T>>{
	/**
	 * 属性名称（对应shapefile
	 */
	String properties;
	/**
	 * 目标tag
	 */
	String targetTag;
	
	/**
	 * 空值是否设置default 
	 */
	boolean hasDefaultValue;
	
	/**
	 * 默认值
	 */
	String defaultValue;
	
	/**
	 * 规则集合
	 */
	List<GenericRuleConfig<T>> rules;
	
	public List<GenericRuleConfig<T>> getRules() {
		return rules;
	}
	
	public void addRules(GenericRuleConfig<T> rule) {
		rules.add(rule);
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isHasDefaultValue() {
		return hasDefaultValue;
	}
	public void setHasDefaultValue(boolean hasDefaultValue) {
		this.hasDefaultValue = hasDefaultValue;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getTargetTag() {
		return targetTag;
	}
	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}
	

}
