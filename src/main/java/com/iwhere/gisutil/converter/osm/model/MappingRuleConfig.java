package com.iwhere.gisutil.converter.osm.model;

import java.util.ArrayList;
import java.util.List;

public class MappingRuleConfig <T extends Comparable<T>>{
	/**
	 * 属性名称（对应shapefile
	 */
	String property;
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
	 * 系数
	 */
	float factor=1.0f;
	
	/**
	 * 规则集合
	 */
	List<GenericRuleConfig<T>> rules;

	public MappingRuleConfig(){
		rules=new ArrayList<>();
	}
	
	public List<GenericRuleConfig<T>> getRules() {
		return rules;
	}
	
	public void addRule(GenericRuleConfig<T> rule) {
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

	public String getTargetTag() {
		return targetTag;
	}
	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
