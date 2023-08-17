package com.iwhere.gisutil.converter.osm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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
import com.iwhere.gisutil.converter.osm.writer.SimpleXMLOSMDocument;

public class Shapefile2OSMConverter {
	
	public static void main(String args[]) {
		String osmfilePath="d:/";
		String osmName="Fujian_Road_Network";
	   SimpleXMLOSMDocument osmdoc=new SimpleXMLOSMDocument(osmfilePath,osmName);
	   osmdoc.initialize();
	   osmdoc.open();
	   osmdoc.writeHeader();
		String shapefilename="D:/LRDL_道路.shp";
		File shpFile = new File(shapefilename);
		Map<String, Object> params = new HashMap<String, Object>();
		DataStore dataStore =null;
		FeatureIterator<SimpleFeature> fit=null;
		try {
			params.put("url", shpFile.toURI().toURL());
			dataStore= DataStoreFinder.getDataStore(params);
			String typeName = dataStore.getTypeNames()[0];
			((ShapefileDataStore)dataStore).setCharset(Charset.forName("GBK"));
			System.out.println(typeName);
			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
			System.out.println("总feature数"+collection.size());
		    fit=collection.features();
			//int counter=-1;
			while(fit.hasNext()) {
				SimpleFeature feature = fit.next();
				//counter++;
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
		        		way.addHighwayClassTag(FeatureClassEnum.SECONDARY);
		        	}else if(width>10) {
		        		way.addHighwayClassTag(FeatureClassEnum.TERTIARY);
		        	}else {
		        		way.addHighwayClassTag(FeatureClassEnum.LIVING_STREET);
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
		} finally{
			fit.close();
			dataStore.dispose();
		}
		
		for(int i=0;i<osmdoc.nodeCnt();i++) {
			osmdoc.writeNode(i);
		}
		
		for(int i=0;i<osmdoc.wayCnt();i++) {
			osmdoc.writeWay(i);
		}
		System.out.println("到这里");
		osmdoc.writeDocEnd();
		osmdoc.close();
	}

}
