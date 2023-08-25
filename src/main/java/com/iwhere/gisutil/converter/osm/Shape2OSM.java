package com.iwhere.gisutil.converter.osm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iwhere.gisutil.converter.osm.model.*;
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

/**
 * Shapefile 路网转换为OSM路网
 * @author zhouchangjin
 *
 */
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
			    	  }
			     }else if(config.getDataType().equals("Integer")) {
			    	 int v=Integer.parseInt(strValue);
			    	 boolean successFlag=CheckRules(config, v);
			    	  if(successFlag) {
			    		 return config;
			    	  }
			     }else if(config.getDataType().equals("String")) {
			    	 boolean successFlag=CheckRules(config, strValue);
			    	 if(successFlag) {
			    		 return config;
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
						  way.addOrUpdateHighwayTag(rule.getDefaultClass());
		        	  }else {
						  way.addOrUpdateHighwayTag(config.getFclass());
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
				
				//映射maxspeed
				String maxSpeedPro=rule.getMaxspeedProperty();
				Object maxSpeedValue=feature.getAttribute(maxSpeedPro);
				List<RuleConfig> speedRuleConfigs=rule.getMaxspeedRules();
				if(maxSpeedValue!=null && !maxSpeedValue.toString().equals("")) {
					RuleConfig config=parseRules(speedRuleConfigs, maxSpeedValue.toString());
					if(config==null) {
						way.addMaxSpeedTag(rule.getDefaultMaxSpeed());
					}else {
					    way.addMaxSpeedTag(config.getMapValue());
					}
				}
				
				//映射toll 收费
				String tollProp=rule.getTollProperty();
				Object tollValue=feature.getAttribute(tollProp);
				List<RuleConfig> tollConfigs=rule.getTollRules();
				if(tollValue!=null && !tollValue.toString().equals("")) {
					RuleConfig config=parseRules(tollConfigs, tollValue.toString());
					if(config!=null) {
						way.addTollTag(config.getMapValue());
					}
				}
				
				//映射lane 车道数量
				String laneProp=rule.getLaneProperty();
				Object laneValue=feature.getAttribute(laneProp);
				List<RuleConfig> laneConfig=rule.getLaneRules();
				if(laneValue!=null && !laneValue.toString().equals("")) {
					RuleConfig config=parseRules(laneConfig, laneValue.toString());
					if(config!=null) {
						way.addLaneTag(config.getMapValue());
					}
				}
				
				List<String> attributes=rule.getAttributes();
				for(String attrName:attributes) {
					  Object attrVal=feature.getAttribute(attrName);
					  if(attrVal!=null) {
						  way.addTag(attrName, attrVal.toString());
					  }
				      //osm为可变字段，空值可以不要
				}

				for(MappingRuleConfig<Integer> intRule:rule.getIntRules()){
					if(intRule.getRules().size()==0){
						String tagName=intRule.getTargetTag();
						String shpProp=intRule.getProperty();
						Object attrVal=feature.getAttribute(shpProp);
						if(attrVal!=null && !attrVal.toString().equals("")){
							int intValue=Integer.parseInt(attrVal.toString());
							intValue=Math.round(intValue*intRule.getFactor());
							way.addTag(tagName,intValue+"");
						}else{
							way.addTag(tagName,intRule.getDefaultValue());
						}
					}
				}

				for(MappingRuleConfig<Double> dblRule:rule.getDoubleRules()){
					if(dblRule.getRules().size()==0){
						String tagName=dblRule.getTargetTag();
						String shpProp=dblRule.getProperty();
						Object attrVal=feature.getAttribute(shpProp);
						if(attrVal!=null && !attrVal.toString().equals("")){
							double dblValue=Double.parseDouble(attrVal.toString());
							dblValue=dblValue*dblRule.getFactor();
							DecimalFormat decimalFormat = new DecimalFormat("#.00");
							String dblStr = decimalFormat.format(dblValue);
							way.addTag(tagName,dblStr);
						}else{
							way.addTag(tagName,dblRule.getDefaultValue());
						}
					}
				}

				for(MappingRuleConfig<String> strRule:rule.getStringRules()){
					if(strRule.getRules().size()==0){
						String tagName=strRule.getTargetTag();
						String shpProp=strRule.getProperty();
						Object attrVal=feature.getAttribute(shpProp);
						if(attrVal!=null && !attrVal.toString().equals("")){
							way.addTag(tagName,attrVal.toString());
						}else{
							way.addTag(tagName,strRule.getDefaultValue());
						}
					}
				}

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
