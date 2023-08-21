package com.iwhere.gisutil.converter.osm.test;

import com.iwhere.gisutil.converter.osm.Shape2OSM;
import com.iwhere.gisutil.converter.osm.ShapefilePropeties;
import com.iwhere.gisutil.converter.osm.model.OSMDocument;
import com.iwhere.gisutil.converter.osm.model.RuleConfig;
import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;
import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;
import com.iwhere.gisutil.converter.osm.writer.SimpleOSMDocumentWriter;

public class TestShapefile2OSM {
	
	public static void main(String args[]) {
		ShapefilePropeties prop=new ShapefilePropeties();
		prop.setCharset("GBK");
		prop.setFileName("LRDL_道路");
		prop.setFilePath("d:/");
		ShapefileMappingRule rule=new ShapefileMappingRule();
		rule.setNameProperty("CNAME");
		rule.setHighwayProperty("WIDTH");
		rule.setOnewayProperty("DIRECTION");
		rule.setMaxspeedProperty("SPEEDCLASS");
		
		rule.setDefaultClass(FeatureClassEnum.PRIMARY);
		rule.setDefaultDirection(OnewayEnum.B);
		rule.setDefaultMaxSpeed("5km/h");
		
		RuleConfig primary=new RuleConfig();
		primary.setDataType("Integer");
		primary.setValueA(50);
		primary.setType("GT");
		primary.setFclass(FeatureClassEnum.PRIMARY);
		
		RuleConfig secondary=new RuleConfig();
		secondary.setDataType("Integer");
		secondary.setValueA(20);
		secondary.setType("GT");
		secondary.setFclass(FeatureClassEnum.SECONDARY);
		
		RuleConfig third=new RuleConfig();
		third.setDataType("Integer");
		third.setValueA(10);
		third.setType("GT");
		third.setFclass(FeatureClassEnum.TERTIARY);
		
		RuleConfig both=new RuleConfig();
		both.setDataType("Integer");
		both.setValueA(1);
		both.setType("EQ");
		both.setOneway(OnewayEnum.B);
		
		RuleConfig forward=new RuleConfig();
		forward.setDataType("Integer");
		forward.setValueA(2);
		forward.setType("EQ");
		forward.setOneway(OnewayEnum.F);
		
		RuleConfig back=new RuleConfig();
		back.setDataType("Integer");
		back.setValueA(3);
		back.setType("EQ");
		back.setOneway(OnewayEnum.T);
		
		RuleConfig speedClass1=new RuleConfig();
		speedClass1.setDataType("Integer");
		speedClass1.setValueA(1);
		speedClass1.setType("EQ");
		speedClass1.setMapValue("200km/h");
		
		
		RuleConfig speedClass2=new RuleConfig();
		speedClass2.setDataType("Integer");
		speedClass2.setValueA(2);
		speedClass2.setType("EQ");
		speedClass2.setMapValue("130km/h");
		
		RuleConfig speedClass3=new RuleConfig();
		speedClass3.setDataType("Integer");
		speedClass3.setValueA(3);
		speedClass3.setType("EQ");
		speedClass3.setMapValue("100km/h");
		
		RuleConfig speedClass4=new RuleConfig();
		speedClass4.setDataType("Integer");
		speedClass4.setValueA(4);
		speedClass4.setType("EQ");
		speedClass4.setMapValue("90km/h");
		
		RuleConfig speedClass5=new RuleConfig();
		speedClass5.setDataType("Integer");
		speedClass5.setValueA(5);
		speedClass5.setType("EQ");
		speedClass5.setMapValue("70km/h");
		
		RuleConfig speedClass6=new RuleConfig();
		speedClass6.setDataType("Integer");
		speedClass6.setValueA(6);
		speedClass6.setType("EQ");
		speedClass6.setMapValue("50km/h");
		
		RuleConfig speedClass7=new RuleConfig();
		speedClass7.setDataType("Integer");
		speedClass7.setValueA(7);
		speedClass7.setType("EQ");
		speedClass7.setMapValue("30km/h");
		
		RuleConfig speedClass8=new RuleConfig();
		speedClass8.setDataType("Integer");
		speedClass8.setValueA(8);
		speedClass8.setType("EQ");
		speedClass8.setMapValue("11km/h");
		
		rule.addHighwayRule(primary);
		rule.addHighwayRule(secondary);
		rule.addHighwayRule(third);
		rule.addOnewayRule(both);
		rule.addOnewayRule(forward);
		rule.addOnewayRule(back);
		
		rule.addMaxSpeedRule(speedClass1);
		rule.addMaxSpeedRule(speedClass2);
		rule.addMaxSpeedRule(speedClass3);
		rule.addMaxSpeedRule(speedClass4);
		rule.addMaxSpeedRule(speedClass5);
		rule.addMaxSpeedRule(speedClass6);
		rule.addMaxSpeedRule(speedClass7);
		rule.addMaxSpeedRule(speedClass8);
		
		prop.addImportAttributes("OBJECTID",
				"KINDNUM","KIND","WIDTH",
				"FUNCCLASS","LENGTH",
				"TOLL","OWNERSHIP",
				"FORM","ELEVATED","UFLAG",
				"CLASID");
		
	   String osmfilePath="d:/";
	   String osmName="Fujian_Road_Network";
	   SimpleOSMDocumentWriter osmdocWriter=new SimpleOSMDocumentWriter(osmfilePath,osmName);
	   osmdocWriter.initialize();
	   osmdocWriter.open();
	   osmdocWriter.writeHeader();
	   OSMDocument doc=osmdocWriter.getOSM();
	   
	   Shape2OSM.loadShapefile2OSM(prop, rule, doc);
	   
	   for(int i=0;i<osmdocWriter.nodeCnt();i++) {
			osmdocWriter.writeNode(i);
	   }
	   
		for(int i=0;i<osmdocWriter.wayCnt();i++) {
			osmdocWriter.writeWay(i);
		}
		osmdocWriter.writeDocEnd();
		osmdocWriter.close();
		
	}
}
