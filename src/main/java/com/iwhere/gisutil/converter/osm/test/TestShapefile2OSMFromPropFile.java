package com.iwhere.gisutil.converter.osm.test;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.iwhere.gisutil.converter.osm.Shape2OSM;
import com.iwhere.gisutil.converter.osm.ShapefilePropeties;
import com.iwhere.gisutil.converter.osm.model.OSMDocument;
import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;
import com.iwhere.gisutil.converter.osm.writer.SimpleOSMDocumentWriter;

public class TestShapefile2OSMFromPropFile {
	public static void main(String args[]) {
		Path resourceDirectory = Paths.get("src","main","resources");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		System.out.println(absolutePath);
		String filename=absolutePath+"/example_config.properties";
		ShapefileMappingRule rule=ShapefileMappingRule.buildFromPropFile(filename);
		ShapefilePropeties prop=new ShapefilePropeties();
		prop.setCharset("GBK");
		prop.setFileName("LRDL_道路");
		prop.setFilePath("d:/地图数据/road/");
		
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
