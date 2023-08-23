package com.iwhere.gisutil.converter.osm.model;

import com.iwhere.gisutil.converter.osm.model.names.OpEnum;

public class GenericRuleConfig<T extends Comparable<T>> {
	
	
	
	/**
	 * 属性比较运算
	 */
	OpEnum operation;
	
	/**
	 * 映射目标tag值
	 */
	String mapValue;
	
	/**
	 * 比较值
	 */
	T compareValue;
	
	/**
	 * 比较值2，双目操作符
	 */
	T compareValue2;

	public OpEnum getOperation() {
		return operation;
	}

	public void setOperation(OpEnum operation) {
		this.operation = operation;
	}

	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}

	public T getCompareValue() {
		return compareValue;
	}

	public void setCompareValue(T compareValue) {
		this.compareValue = compareValue;
	}

	public T getCompareValue2() {
		return compareValue2;
	}

	public void setCompareValue2(T compareValue2) {
		this.compareValue2 = compareValue2;
	}	

}
