package com.iwhere.gisutil.converter.osm.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwhere.gisutil.converter.osm.model.ShapefileMappingRule;

public class TestLoadRules {

	public static void main(String[] args) {
		/**
		Path resourceDirectory = Paths.get("src","main","resources");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		System.out.println(absolutePath);
		String filename=absolutePath+"/example_config.properties";
		ShapefileMappingRule.buildFromPropFile(filename);
		 **/
		/**
		String intExp="[+-]?[0-9]+";
		String floatExp="[+-]?[0-9]+\\.[0-9]+";

		System.out.println("10".matches(intExp));
		System.out.println("10.22".matches(intExp));
		System.out.println("10.22".matches(floatExp));
		System.out.println("10+22".matches(floatExp));
		System.out.println("-10.22".matches(floatExp));
		**/
		String reg="\\s*";
		System.out.println(" ".matches(reg));
		String exp=" val >= 12.3 ";
		String patter="\\s*val\\s*([><=][=]?)\\s*([0-9]+\\.?[0-9]*)\\s*";
		System.out.println(exp.matches(patter));
		Pattern p=Pattern.compile(patter);
		Matcher m=p.matcher(exp);
		if(m.find()) {
			String oper=m.group(1).trim();
			String val=m.group(2);
			System.out.println(oper);
			System.out.println(val);
		}
	}

}
