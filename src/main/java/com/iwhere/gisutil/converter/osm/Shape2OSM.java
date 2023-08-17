package com.iwhere.gisutil.converter.osm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
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

import com.iwhere.gisutil.converter.osm.model.OSMDocument;
import com.iwhere.gisutil.converter.osm.model.RuleConfig;
import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;
import com.iwhere.gisutil.converter.osm.model.WayElement;

public class Shape2OSM {
	
	private static boolean CheckRules(RuleConfig config,String value) {
		if(config.getValue().equals(value)) {
			return true;
		}else {
			return false;
		}
	}
	
	private static boolean CheckRules(RuleConfig config,int value) {
	    if(config.getType().equals("EQ")) {
	    	if(config.getValueA().equals(value)) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }else if(config.getType().equals("GT")) {
	    	if(value>config.getValueA()) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }else if(config.getType().equals("LT")) {
	    	if(value<config.getValueA()) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }else {
	    	return false;
	    }
	}
	
	private static boolean CheckRules(RuleConfig config,double value) {
		    if(config.getType().equals("EQ")) {
		    	if(config.getDvalueA().equals(value)) {
		    		return true;
		    	}else {
		    		return false;
		    	}
		    }else if(config.getType().equals("GT")) {
		    	if(value>config.getDvalueA()) {
		    		return true;
		    	}else {
		    		return false;
		    	}
		    }else if(config.getType().equals("LT")) {
		    	if(value<config.getDvalueA()) {
		    		return true;
		    	}else {
		    		return false;
		    	}
		    }else {
		    	return false;
		    }
	}
	
	private static RuleConfig parseRules(List<RuleConfig> rules,String strValue) {
		   for(RuleConfig config:rules) {
			     if(config.getDataType().equals("Double")) {
			    	  double v=Double.parseDouble(strValue);
			    	  boolean successFlag=CheckRules(config, v);
			    	  if(successFlag) {
			    		 return config;
			    	  }else {
			    		  return null;
			    	  }
			     }else if(config.getDataType().equals("Integer")) {
			    	 int v=Integer.parseInt(strValue);
			    	 boolean successFlag=CheckRules(config, v);
			    	  if(successFlag) {
			    		 return config;
			    	  }else {
			    		  return null;
			    	  }
			     }else if(config.getDataType().equals("String")) {
			    	 boolean successFlag=CheckRules(config, strValue);
			    	 if(successFlag) {
			    		 return config;
			    	 }else {
			    		 return null;
			    	 }
			     }
		   }
		   return null;
	}

	public static void loadShapefile2OSM(ShapefilePropeties prop,ShapefileMappingRule rule, OSMDocument osm) {
		String shapefilename = prop.getFilePath() + "" + prop.getFileName() + ".shp";
		File shpFile = new File(shapefilename);
		Map<String, Object> params = new HashMap<String, Object>();
		DataStore dataStore = null;
		FeatureIterator<SimpleFeature> fit = null;
		try {
			params.put("url", shpFile.toURI().toURL());
			dataStore = DataStoreFinder.getDataStore(params);
			String typeName = dataStore.getTypeNames()[0];
			((ShapefileDataStore) dataStore).setCharset(Charset.forName(prop.getCharset()));
			System.out.println(typeName);
			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
			System.out.println("总feature数" + collection.size());
			fit = collection.features();
			while (fit.hasNext()) {
				SimpleFeature feature = fit.next();

				Geometry geometry = (Geometry) feature.getDefaultGeometry();
				Coordinate[] coors = geometry.getCoordinates();
				WayElement way = new WayElement();
				for (Coordinate coor : coors) {
					double lon = coor.getX();
					double lat = coor.getY();
					int node_id = osm.addNode(lon, lat);
					way.AddRefNode(node_id);
				}
				
				//增加OSM Feature 名称字段
				String nameProp=rule.getNameProperty();
				String name=feature.getAttribute(nameProp).toString();
		        if(!name.equals("")) {
		        	way.addNameTag(name);
		        }
		        
		        //增加 OSM 的highway字段
		        String hiProString=rule.getHighwayProperty();
		        Object widthStr=feature.getAttribute(hiProString);
		        List<RuleConfig> hrules=rule.getHighwayRules();
		        if(widthStr!=null && !widthStr.toString().equals("")) {
		        	  RuleConfig config=  parseRules(hrules, widthStr.toString());
		        	  if(config==null) {
		        		  way.addHighwayClassTag(rule.getDefaultClass());
		        	  }else {
		        		  //System.out.println(config.getFclass());
			        	  way.addHighwayClassTag(config.getFclass());  
		        	  }
		        }
		        
		        //增加OSM的oneway字段
		        String onProString=rule.getOnewayProperty();
		        Object direction=feature.getAttribute(onProString);
		        List<RuleConfig> orules=rule.getOnewayRules();
		        if(direction!=null && !direction.toString().equals("")) {
		        	   RuleConfig config=parseRules(orules, direction.toString());
		        	   if(config==null) {
		        		   way.addOnewayTag(rule.getDefaultDirection());
		        	   }else {
			        	   way.addOnewayTag(config.getOneway());
		        	   }
		        }
				osm.addWay(way);

			}
			fit.close();
			dataStore.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fit.close();
			dataStore.dispose();
		}

	}

}
