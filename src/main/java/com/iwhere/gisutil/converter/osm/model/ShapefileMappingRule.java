package com.iwhere.gisutil.converter.osm.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;
import com.iwhere.gisutil.converter.osm.model.names.OpEnum;

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
	/**
	 * 最多单字段规则数量
	 */
	static int MAX_RULE_CNT=20;

	/**
	 * 最多通用规则数量
	 */
	static int MAX_GENERIC_RULE_CNT=1000;
	
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

	List<MappingRuleConfig<Integer>> intRules;

	List<MappingRuleConfig<Double>> doubleRules;

	List<MappingRuleConfig<String>> stringRules;

	String defaultMaxSpeed;
	
	FeatureClassEnum defaultClass;
	
	OnewayEnum defaultDirection; 
	
	List<String> importAttributes;


	
	public ShapefileMappingRule() {
		highwayRules=new ArrayList<>();
		onewayRules=new ArrayList<>();
		maxspeedRules=new ArrayList<>();
		tollRules=new ArrayList<>();
		laneRules=new ArrayList<>();
		importAttributes=new ArrayList<>();
		intRules=new ArrayList<>();
		doubleRules=new ArrayList<>();
		stringRules=new ArrayList<>();
	}

	public void addIntRule(MappingRuleConfig<Integer> config){
		intRules.add(config);
	}

	public void addDblRule(MappingRuleConfig<Double> config){
		doubleRules.add(config);
	}

	public void addStrRules(MappingRuleConfig<String> config){
		stringRules.add(config);
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

	public List<MappingRuleConfig<Integer>> getIntRules() {
		return intRules;
	}

	public List<MappingRuleConfig<Double>> getDoubleRules() {
		return doubleRules;
	}

	public List<MappingRuleConfig<String>> getStringRules() {
		return stringRules;
	}

	private  static <T extends Comparable<T>>void loadRules(MappingRuleConfig<T> config, Properties prop){
		 String rulePrefix=config.getTargetTag();
		for(int i=0;i<MAX_RULE_CNT;i++) {
				String propName=rulePrefix+".rule["+i+"]";
				String expProp=propName+".exp";
				String valProp=propName+".val";
				if(prop.containsKey(expProp)) {
					String exp=prop.getProperty(expProp);
					String val=prop.getProperty(valProp);
					GenericRuleConfig<T> gRuleConfig=new GenericRuleConfig<>();
					gRuleConfig.setMapValue(val);
					parseRuleExp(exp,gRuleConfig);
					config.addRule(gRuleConfig);

				}else{
					break;
				}
		}
	}

	private static void parseMappingRule(ShapefileMappingRule rule,Properties prop){

		for(int i=0;i<MAX_GENERIC_RULE_CNT;i++){
			String rulePre="rule["+i+"].";
			String propNameKey=rulePre+"prop";
			String tagNameKey=rulePre+"tag";
			String defaultValueKey=rulePre+"defaultValue";
			String factorKey=rulePre+"factor";
			String dataTypeKey=rulePre+"dataType";

			if(!prop.containsKey(propNameKey)){
				break;
			}

			String propName=prop.getProperty(propNameKey);
			String tagName=prop.getProperty(tagNameKey);
			String defaultValue=prop.getProperty(defaultValueKey);
			String factorStr=prop.getProperty(factorKey);
			String dataType=prop.getProperty(dataTypeKey);

			float factor=1.0f;
			if(factorStr!=null && !factorStr.equals("")){
				factor=Float.parseFloat(factorStr);
			}

			MappingRuleConfigBuilder builder=new MappingRuleConfigBuilder(propName)
					.setTargetTag(tagName)
					.withDefaultValue(defaultValue)
					.multiBy(factor);
			if(dataType.equals("Integer")){
				MappingRuleConfig<Integer> ruleConfig=builder.build(Integer.valueOf(0));
				loadRules(ruleConfig,prop);
				rule.addIntRule(ruleConfig);

			}else if(dataType.equals("Double") || dataType.equals("Float")){
				MappingRuleConfig<Double> ruleConfig=builder.build(Double.valueOf(0.0));
				loadRules(ruleConfig,prop);
				rule.addDblRule(ruleConfig);
			}else{
				MappingRuleConfig<String> ruleConfig=builder.build("");
				loadRules(ruleConfig,prop);
				rule.addStrRules(ruleConfig);
			}

		}

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

	private static <T extends Comparable<T>>void parseRuleExp(String exp,GenericRuleConfig<T> config){
		String patternExp="\\s*val\\s*([><=]=?)\\s*([0-9]+\\.?[0-9]*)\\s*";
		Pattern p=Pattern.compile(patternExp);
		Matcher m=p.matcher(exp);
		if(m.find()){
			String oper=m.group(1).trim();
			String val=m.group(2).trim();
			if(oper.equals("=")){
				config.setOperation(OpEnum.EQ);
			}else if(oper.equals("<")){
				config.setOperation(OpEnum.LT);
			}else if(oper.equals(">")){
				config.setOperation(OpEnum.GT);
			}else if(oper.equals(">=")){
				config.setOperation(OpEnum.GTE);
			}else if(oper.equals("<=")){
				config.setOperation(OpEnum.LTE);
			}
			String intExp="[+-]?[0-9]+";
			String floatExp="[+-]?[0-9]+\\.[0-9]+";
			if(val.matches(intExp)){
				config.setCompareValue((T)Integer.valueOf(Integer.parseInt(val)));
			}else if(val.matches(floatExp)){
				config.setCompareValue((T)Double.valueOf(Double.parseDouble(val)));
			}else{
				config.setCompareValue((T)val);
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

		parseMappingRule(rule,prop);
		
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
		sb.append(" name='").append(nameProperty);
		sb.append("'");
		sb.append(System.lineSeparator());
		sb.append(" roadclass='").append(highwayProperty).append("'");
		sb.append(System.lineSeparator());
		sb.append(" oneway='").append(onewayProperty).append("'");
		sb.append(System.lineSeparator());
		sb.append(" maxspeed='").append(maxspeedProperty).append("'");
		sb.append(System.lineSeparator());
		sb.append(" tolls='").append(tollProperty).append("'");
		sb.append(System.lineSeparator());
		sb.append(" lanes='").append(laneProperty).append("'");
		sb.append(System.lineSeparator());
		sb.append(" default_roadclass='").append(defaultClass.getFClass()).append("'");
		sb.append(System.lineSeparator());
		sb.append(" default_direction='").append(defaultDirection.getType()).append("'");
		sb.append(System.lineSeparator());
		sb.append(" default_maxspeed='").append(defaultMaxSpeed).append("'");
		sb.append(System.lineSeparator());
		sb.append(" import_attributes='").append(importAttributes.toString()).append("'");
		sb.append(System.lineSeparator());
		sb.append(" roadRule=").append(highwayRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" onewayRule=").append(onewayRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" speedRule=").append(maxspeedRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" tollRule=").append(tollRules.toString());
		sb.append(System.lineSeparator());
		sb.append(" laneRule=").append(laneRules.toString());
		sb.append(System.lineSeparator());
		sb.append("}");
		return sb.toString();
	}
	
	
	

}
