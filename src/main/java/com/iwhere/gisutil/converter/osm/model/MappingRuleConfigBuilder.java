package com.iwhere.gisutil.converter.osm.model;


import com.iwhere.gisutil.converter.osm.model.names.CommonTagEnum;
import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;

public class MappingRuleConfigBuilder {
	

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
	
	public MappingRuleConfigBuilder(String prop) {
		properties=prop;
		hasDefaultValue=false;
	}
	
	public MappingRuleConfigBuilder setTargetTag(CommonTagEnum tag) {
		targetTag=tag.getTag();
		return this;
	}
	
	public MappingRuleConfigBuilder withDefaultValue(String val) {
		defaultValue=val;
		hasDefaultValue=true;
		return this;
	}
	
	public MappingRuleConfigBuilder withDefaultValue(OnewayEnum oneway) {
		targetTag = CommonTagEnum.ONEWAY_TAG.getTag();
		defaultValue= oneway.getType();
		hasDefaultValue=true;
		return this;
	}
	
	public MappingRuleConfigBuilder withDefaultValue(FeatureClassEnum fclass) {
		targetTag = CommonTagEnum.HIGHWAY_TAG.getTag();
		defaultValue = fclass.getFClass();
		hasDefaultValue = true;
		return this;
	}
	
	private <T extends Comparable<T>>void buildData(MappingRuleConfig<T> config) {
		config.setDefaultValue(defaultValue);
		config.setHasDefaultValue(hasDefaultValue);
		config.setProperties(properties);
		config.setTargetTag(targetTag);
	}
	
	public <T extends Comparable<T>> MappingRuleConfig<T> build(T type){
		MappingRuleConfig<T> mappingRuleConfig=new MappingRuleConfig<T>();
		buildData(mappingRuleConfig);
		return mappingRuleConfig;
	}
	
	public static void main(String args[]) {
	}

}
