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
		rule.setDefaultClass(FeatureClassEnum.PRIMARY);
		rule.setDefaultDirection(OnewayEnum.B);
		
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
		back.setOneway(OnewayEnum.F);
		
		rule.addHighwayRule(primary);
		rule.addHighwayRule(secondary);
		rule.addHighwayRule(third);
		rule.addOnewayRule(both);
		rule.addOnewayRule(forward);
		rule.addOnewayRule(back);
		
		prop.addImportAttribute("OBJECTID",
				"KINDNUM","KIND","WIDTH",
				"DIRECTION","FUNCCLASS","LENGTH",
				"SPEEDCLASS","TOLL","OWNERSHIP",
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
