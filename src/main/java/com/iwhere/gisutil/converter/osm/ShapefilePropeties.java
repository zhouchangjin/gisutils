package com.iwhere.gisutil.converter.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Shapefile文件的配置
 * @author zhouchangjin
 *
 */
public class ShapefilePropeties {
	
	String fileName;
	String filePath;
	String charset="GBK";
	
	public ShapefilePropeties() {
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


}
