package com.iwhere.gisutil.converter.osm.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	static int MAX_RULE_CNT=20;
	
	/**
	 * 要素名称
	 * name 对应shapefile属性
	 */
	String nameProperty;
	/**
	 * 行车方向
	 * oneway 对应shapefile属性
	 */
	String onewayProperty;
	/**
	 * 道路类型
	 * highway 对应shapefile属性
	 */
	String highwayProperty;
	
	/**
	 * 最大速度
	 * maxspeed 对应shapefile属性
	 */
	String maxspeedProperty;
	
	/**
	 * 收费类型 可以没有默认值，不收费则不加tag
	 * toll 对应的shapefile属性 
	 */
	String tollProperty;
	
	/**
	 * 车道数量
	 * lane 对应shapefile属性
	 */
	String laneProperty;
	
	List<RuleConfig> highwayRules;
	
	List<RuleConfig> onewayRules;
	
	List<RuleConfig> maxspeedRules;
	
	List<RuleConfig> tollRules;
	
	List<RuleConfig> laneRules;

	String defaultMaxSpeed;
	
	FeatureClassEnum defaultClass;
	
	OnewayEnum defaultDirection; 
	
	List<String> importAttributes;
	
	public ShapefileMappingRule() {
		highwayRules=new ArrayList<RuleConfig>();
		onewayRules=new ArrayList<RuleConfig>();
		maxspeedRules=new ArrayList<RuleConfig>();
		tollRules=new ArrayList<RuleConfig>();
		laneRules=new ArrayList<RuleConfig>();
		importAttributes=new ArrayList<String>();
	}

	public void addImportAttribute(String attributeName) {
		importAttributes.add(attributeName);
	}
	
	public void addImportAttributes(String... attributes) {
		importAttributes.addAll(Arrays.asList(attributes));
	}
	
	public List<String> getAttributes(){
		return importAttributes;
	}
	
	public String getDefaultMaxSpeed() {
		return defaultMaxSpeed;
	}


	public void setDefaultMaxSpeed(String defaultMaxSpeed) {
		this.defaultMaxSpeed = defaultMaxSpeed;
	}
	
	


	public String getLaneProperty() {
		return laneProperty;
	}

	public void setLaneProperty(String laneProperty) {
		this.laneProperty = laneProperty;
	}

	public String getMaxspeedProperty() {
		return maxspeedProperty;
	}


	public void setMaxspeedProperty(String maxspeedProperty) {
		this.maxspeedProperty = maxspeedProperty;
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
	
	public void addLaneRule(RuleConfig config) {
		laneRules.add(config);
	}
	
	public void addMaxSpeedRule(RuleConfig config) {
		maxspeedRules.add(config);
	}


	public void addHighwayRule(RuleConfig config) {
		highwayRules.add(config);
	}
	

	public void addOnewayRule(RuleConfig config) {
		onewayRules.add(config);
	}
	
	public void addTollRule(RuleConfig config) {
		tollRules.add(config);
	}
	
	public List<RuleConfig> getTollRules(){
		return tollRules;
	}
	
	public List<RuleConfig> getHighwayRules(){
		return highwayRules;
	}
	
	public List<RuleConfig> getOnewayRules(){
		return onewayRules;
	}
	
	public List<RuleConfig> getMaxspeedRules(){
		return maxspeedRules;
	}
	
	public List<RuleConfig> getLaneRules(){
		return laneRules;
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
	
	
	
	public String getTollProperty() {
		return tollProperty;
	}

	public void setTollProperty(String tollProperty) {
		this.tollProperty = tollProperty;
	}

	private static void parseRule(String ruleType,ShapefileMappingRule rule,Properties prop) {
		for(int i=0;i<MAX_RULE_CNT;i++) {
			String propName=ruleType+".rule["+i+"]";
			String typeProp=propName+".type";
			String expProp=propName+".exp";
			String valProp=propName+".val";
			if(prop.containsKey(typeProp)) {
				String ruledatatype=prop.getProperty(typeProp);
				String exp=prop.getProperty(expProp);
				String val=prop.getProperty(valProp);
				RuleConfig config=new RuleConfig();
				config.setDataType(ruledatatype);
				if(ruleType.equals("roadclass")) {
					config.setFclass(FeatureClassEnum.valueOf(val));
				}else if(ruleType.equals("oneway")) {
					config.setOneway(OnewayEnum.valueOf(val));
				}else {
					config.setMapValue(val);
				}
				parseRuleExp(exp, config);
				if(ruleType.equals("roadclass")) {
					rule.addHighwayRule(config);
				}else if(ruleType.equals("oneway")) {
					rule.addOnewayRule(config);
				}else if(ruleType.equals("maxspeed")){
					rule.addMaxSpeedRule(config);
				}else if(ruleType.equals("toll")){
					rule.addTollRule(config);
				}else if(ruleType.equals("lane")) {
					rule.addLaneRule(config);
				}
				
			}else {
				break;
			}
		}
	}
	
	
	private static void parseRuleExp(String exp,RuleConfig config) {
		String patternExp="val([^0-9]*)([0-9]+)";
		Pattern p=Pattern.compile(patternExp);
		Matcher m=p.matcher(exp);
		if(m.find()) {
			String oper=m.group(1).trim();
			String val=m.group(2);
			if(oper.equals("=")) {
				config.setType("EQ");
			}else if(oper.equals(">")) {
				config.setType("GT");
			}else if(oper.equals("<")) {
				config.setType("LT");
			}
			if(config.getDataType().equals("Integer")) {
				config.setValueA(Integer.parseInt(val));
			}else if(config.getDataType().equals("Double")) {
				config.setDvalueA(Double.parseDouble(val));
			}else if(config.getDataType().equals("String")) {
				config.setValue(val);
			}
		}else {
			//System.out.println("不合法的规则");
		}

	}
	
	public static ShapefileMappingRule buildFromProp(InputStream is) {
		Properties prop=new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ShapefileMappingRule rule=new ShapefileMappingRule();
		String name=prop.getProperty("name_prop");
		rule.setNameProperty(name);
		String roadClass=prop.getProperty("roadclass_prop");
		rule.setHighwayProperty(roadClass);
		
		String onewayProp=prop.getProperty("oneway_prop");
		rule.setOnewayProperty(onewayProp);
		
		String maxspeedProp=prop.getProperty("maxspeed_prop");
		rule.setMaxspeedProperty(maxspeedProp);
		
		String tollProp=prop.getProperty("toll_prop");
		rule.setTollProperty(tollProp);
		
		String laneProp=prop.getProperty("lane_prop");
		rule.setLaneProperty(laneProp);
		
		String defaultRoadClass=prop.getProperty("default_roadclass");
		rule.setDefaultClass(FeatureClassEnum.valueOf(defaultRoadClass));
		
		String defaultDirection=prop.getProperty("default_direction");
		rule.setDefaultDirection(OnewayEnum.valueOf(defaultDirection));
		
		String defaultSpeed=prop.getProperty("default_maxspeed");
		rule.setDefaultMaxSpeed(defaultSpeed);
		
		parseRule("roadclass", rule, prop);
		parseRule("oneway", rule, prop);
		parseRule("maxspeed", rule, prop);
		parseRule("toll", rule, prop);
		parseRule("lane",rule, prop);
		
		String arrayStr=prop.getProperty("import_attributes");
		String[] attrs=arrayStr.split(",");
		rule.addImportAttributes(attrs);
		
		System.out.println(rule);
		return rule;
	}
	
	public static ShapefileMappingRule buildFromPropFile(String proptiesFile) {
		
		try {
			return buildFromProp(new FileInputStream(proptiesFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("{").append(System.lineSeparator());
		sb.append(" name='"+nameProperty);
		sb.append("'");
		sb.append(System.lineSeparator());
		sb.append(" roadclass='"+highwayProperty+"'");
		sb.append(System.lineSeparator());
		sb.append(" oneway='"+onewayProperty+"'");
		sb.append(System.lineSeparator());
		sb.append(" maxspeed='"+maxspeedProperty+"'");
		sb.append(System.lineSeparator());
		sb.append(" default_roadclass='"+defaultClass.getFClass()+"'");
		sb.append(System.lineSeparator());
		sb.append(" default_direction='"+defaultDirection.getType()+"'");
		sb.append(System.lineSeparator());
		sb.append(" default_maxspeed='"+defaultMaxSpeed+"'");
		sb.append(System.lineSeparator());
		sb.append(" import_attributes='"+importAttributes.toString()+"'");
		sb.append(System.lineSeparator());
		sb.append(" roadRule="+highwayRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" onewayRule="+onewayRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" speedRule="+maxspeedRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" tollRule="+tollRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" laneRule="+laneRules.toString());
		sb.append(System.lineSeparator());
		sb.append("}");
		return sb.toString();
	}
	
	
	

}
