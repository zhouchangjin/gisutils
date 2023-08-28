package com.iwhere.gisutil.converter.osm.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwhere.gisutil.converter.osm.model.GenericRuleConfig;
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


		/**
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
		 **/
		/**
		String code="123";
		System.out.println(code.compareTo("121"));
		 **/

		System.out.println(Integer.valueOf(-1).compareTo(1));
		System.out.println(Double.valueOf(10.02).compareTo(10.21));
		System.out.println(Integer.class.getName());

		List<String> typename=new ArrayList<>();
		Type t=typename.getClass();
		System.out.println(t.getTypeName());

		GenericRuleConfig<Integer> c=new GenericRuleConfig<>();
		try {
			Method m=c.getClass().getMethod("getCompareValue");
			c.setCompareValue(1);

			System.out.println("==="+m.getGenericReturnType().getTypeName());
			Object returnObj=m.invoke(c);

			System.out.println(returnObj.getClass().getName());
			System.out.println(m.getGenericReturnType().getTypeName());

		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

}
