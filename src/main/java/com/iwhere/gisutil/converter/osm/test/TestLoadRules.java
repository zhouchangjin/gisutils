package com.iwhere.gisutil.converter.osm.test;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;

public class TestLoadRules {

	public static void main(String[] args) {
		Path resourceDirectory = Paths.get("src","main","resources");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		System.out.println(absolutePath);
		String filename=absolutePath+"/example_config.properties";
		ShapefileMappingRule.buildFromPropFile(filename);
	}

}
