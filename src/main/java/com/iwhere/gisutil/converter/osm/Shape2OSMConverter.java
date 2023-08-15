package com.iwhere.gisutil.converter.osm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.iwhere.gisutil.converter.osm.model.WayElement;
import com.iwhere.gisutil.converter.osm.model.names.FeatureClassEnum;
import com.iwhere.gisutil.converter.osm.model.names.OnewayEnum;

public class Shape2OSMConverter {
	
	public static void buildXML(String path,String fileName) {
		 Document document = DocumentHelper.createDocument();
		 Element head = document.addElement("osm");
		 head.addAttribute("version", "0.6");
		 OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try {
	            XMLWriter writer = new XMLWriter(out, format);
	            writer.write(document);
	            writer.close();
	        } catch (IOException e) {
	            System.out.println("生成文件失败。文件名【" + fileName + "】");
	        }
	        
	        try (FileOutputStream fos = new FileOutputStream(path+fileName + ".osm")) {
	            fos.write(out.toByteArray());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public static void main(String args[]) {
		
		// 15万
		/**
		int featureBatchCount=150000;
		int featureStart=0;
		int startNodeId=1;
		int startWayId=1;
		**/
		/**
		int featureBatchCount=150000;
		int featureStart=150000;
		int startNodeId=2007281;
		int startWayId=150001;
		**/
		
		int featureBatchCount=150000;
		int featureStart=300000;
		int startNodeId=3121541;
		int startWayId=300001;
		
		//2559170
		//2827434
		//3139480
		
		String part="s"+featureStart+"t"+(featureStart+featureBatchCount);
		
		OSMDocument osmdoc=new OSMDocument();
		osmdoc.setStartNodeId(startNodeId);
		osmdoc.setStartWayId(startWayId);
		
		File shpFile = new File("D:/LRDL_道路.shp");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("url", shpFile.toURI().toURL());
			DataStore dataStore = DataStoreFinder.getDataStore(params);
			String typeName = dataStore.getTypeNames()[0];
			((ShapefileDataStore)dataStore).setCharset(Charset.forName("GBK"));
			System.out.println(typeName);
			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
			System.out.println("总feature数"+collection.size());
			FeatureIterator<SimpleFeature> fit=collection.features();

			int counter=-1;
			while(fit.hasNext()) {
				SimpleFeature feature = fit.next();
				counter++;
				if(counter<featureStart) {
					continue;
				}else if(counter==featureStart+featureBatchCount){
					break;
				}
				
		        Geometry geometry = (Geometry) feature.getDefaultGeometry();
		        Coordinate[] coors= geometry.getCoordinates();
		        WayElement way=new WayElement();
		        for(Coordinate coor:coors) {
		        	double lon=coor.getX();
		        	double lat=coor.getY();
			        int node_id=osmdoc.addNode(lon, lat);
			        way.AddRefNode(node_id);
		        }
		        //路名
		        String name=feature.getAttribute("CNAME").toString();
		        if(!name.equals("")) {
		        	way.addNameTag(name);
		        }
		        //路宽转换  55 一级道路 30 二级道路 15 三级道路
		        Object widthStr=feature.getAttribute("WIDTH");
		        if(widthStr!=null && !widthStr.toString().equals("")) {
		        	Integer width=Integer.parseInt(widthStr.toString());
		        	//System.out.println(width);
		        	if(width>50) {
		        		way.addHighwayClassTag(FeatureClassEnum.PRIMARY);
		        	}else if(width>20) {
		        		way.addHighwayClassTag(FeatureClassEnum.PRIMARY);
		        	}else if(width>10) {
		        		way.addHighwayClassTag(FeatureClassEnum.SECONDARY);
		        	}else {
		        		way.addHighwayClassTag(FeatureClassEnum.TERTIARY);
		        	}
		        }
		        //道路方向
		        Object direction=feature.getAttribute("DIRECTION");
		        if(direction!=null && !direction.toString().equals("")) {
		        	Integer dir=Integer.parseInt(direction.toString());
		        	if(dir==1) {
		        		//双向
		        		way.addOnewayTag(OnewayEnum.B); 
		        	}else if(dir==2) {
		        		way.addOnewayTag(OnewayEnum.F);
		        	}else if(dir==3) {
		        		way.addOnewayTag(OnewayEnum.T);
		        	}
		        }
		        osmdoc.addWay(way);
		        

			}
			fit.close();
			dataStore.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		osmdoc.processNode();
		//osmdoc.clearNode();
		osmdoc.processWay();
		
		osmdoc.WriteFile("d:/","fujian_road_part_"+part);
		String id=osmdoc.getLastNodeId();
		String wayid=osmdoc.getLastWayId();
		System.out.println("wayid="+wayid+"_nodeid="+id);
		//buildXML("d:/", "path");


	}

}
