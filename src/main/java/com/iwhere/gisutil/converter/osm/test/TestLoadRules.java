package com.iwhere.gisutil.converter.osm.test;

import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;

public class TestLoadRules {

	public static void main(String[] args) {
			ShapefileMappingRule.buildFromPropFile("d:/rule.properties");
	}

}
