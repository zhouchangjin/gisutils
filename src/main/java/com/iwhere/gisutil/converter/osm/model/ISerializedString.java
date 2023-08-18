package com.iwhere.gisutil.converter.osm.model;
/**
 * 
 * @author zhouchangjin
 *
 */
public interface ISerializedString {
	/**
	 * 使用字符串拼接的方式生成xml，
	 * 由于dom4j或sax等接口，对于大文件来说容易内存溢出
	 * 所以可以直接使用拼接的方式产生xml
	 * @return
	 */
	public String toXMLString();

}
