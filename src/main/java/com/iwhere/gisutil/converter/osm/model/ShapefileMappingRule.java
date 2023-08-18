package com.iwhere.gisutil.converter.osm.model;

import java.util.ArrayList;
import java.util.List;

import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;
/**
 * shapefile和osm文件字段的映射规则，其中osm必须的字段为<br/>
 * name 道路或对象名称 <br/>
 * highway 如果是路网，必须有此字段表示道路类型 <br/>
 * oneway 如果是路网 建议有此字段 表示道路是双向 正向 逆向（Linestring方向）<br/>
 * 其他字段可选 <br/>
 * @author zhouchangjin
 *
 */
public class ShapefileMappingRule {
	
	String nameProperty;
	
	String onewayProperty;
	
	String highwayProperty;
	
	List<RuleConfig> highwayRules;
	
	List<RuleConfig> onewayRules;
	
	FeatureClassEnum defaultClass;
	
	OnewayEnum defaultDirection; 
	
	public ShapefileMappingRule() {
		highwayRules=new ArrayList<RuleConfig>();
		onewayRules=new ArrayList<RuleConfig>();
	}
	
	
	public FeatureClassEnum getDefaultClass() {
		return defaultClass;
	}


	public void setDefaultClass(FeatureClassEnum defaultClass) {
		this.defaultClass = defaultClass;
	}


	public OnewayEnum getDefaultDirection() {
		return defaultDirection;
	}


	public void setDefaultDirection(OnewayEnum defaultDirection) {
		this.defaultDirection = defaultDirection;
	}


	public void addHighwayRule(RuleConfig config) {
		highwayRules.add(config);
	}
	

	public void addOnewayRule(RuleConfig config) {
		onewayRules.add(config);
	}
	
	public List<RuleConfig> getHighwayRules(){
		return highwayRules;
	}
	
	public List<RuleConfig> getOnewayRules(){
		return onewayRules;
	}

	public String getNameProperty() {
		return nameProperty;
	}

	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}

	public String getOnewayProperty() {
		return onewayProperty;
	}

	public void setOnewayProperty(String onewayProperty) {
		this.onewayProperty = onewayProperty;
	}

	public String getHighwayProperty() {
		return highwayProperty;
	}

	public void setHighwayProperty(String highwayProperty) {
		this.highwayProperty = highwayProperty;
	}
	
	
	

}
