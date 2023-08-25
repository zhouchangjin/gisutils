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

	/**
	 * 系数
	 */
	float factor=1.0f;

	public MappingRuleConfigBuilder(String prop) {
		properties=prop;
		hasDefaultValue=false;
	}

	public MappingRuleConfigBuilder setTargetTag(String tag) {
		targetTag=tag;
		return this;
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

	public MappingRuleConfigBuilder multiBy(float coef){
		this.factor=coef;
		return this;
	}
	
	private <T extends Comparable<T>>void buildData(MappingRuleConfig<T> config) {
		config.setDefaultValue(defaultValue);
		config.setHasDefaultValue(hasDefaultValue);
		config.setProperty(properties);
		config.setTargetTag(targetTag);
		config.setFactor(factor);
	}
	
	public <T extends Comparable<T>> MappingRuleConfig<T> build(T type){
		MappingRuleConfig<T> mappingRuleConfig=new MappingRuleConfig<T>();
		buildData(mappingRuleConfig);
		return mappingRuleConfig;
	}
	
	public static void main(String args[]) {
	}

}
