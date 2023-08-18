package com.iwhere.gisutil.converter.osm.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.iwhere.gisutil.converter.osm.model.NodeElement;
import com.iwhere.gisutil.converter.osm.model.OSMDocument;
import com.iwhere.gisutil.converter.osm.model.WayElement;

/**
 * 节省内存的拼写xml的文件类
 * @author zhouchangjin
 *
 */
public class SimpleOSMDocumentWriter implements IOSMDocumentWriter {
	

	OSMDocument osmDocument;
	
	String filePath;
	String fileName;
	
	BufferedWriter docWriter=null;
	FileWriter fileWriter=null;
	
	public OSMDocument getOSM() {
		return osmDocument;
	}

	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public SimpleOSMDocumentWriter(String path,String file) {
		this.fileName=file;
		this.filePath=path;
	}

	public SimpleOSMDocumentWriter() {
		initialize();
	}
	
	public void close() {
		try {
			
			if(fileWriter!=null) {
				fileWriter.close();
			}
			if(docWriter!=null) {
				docWriter.close();
			}
		}catch(IOException e) {
			
		}		
	}
	
	public void open() {
		if(docWriter!=null) {
			
		}else {
			try {
				fileWriter =new FileWriter(new File(filePath+fileName+".osm"));
				docWriter=new BufferedWriter(fileWriter);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initialize() {
		osmDocument=new OSMDocument();
	}

	@Override
	public int addWay(WayElement way) {
		return osmDocument.addWay(way);
	}

	@Override
	public int addNode(double longitude, double latitude) {
		return osmDocument.addNode(longitude, latitude);
	}
	
	private void nextLine(StringBuilder sb) {
		sb.append(System.getProperty("line.separator"));
	}

	@Override
	public void writeHeader() {
		StringBuilder sb=new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		nextLine(sb);
		sb.append("<osm version='0.6' generator='IWHEREOSM'>");
		nextLine(sb);
		try {
			docWriter.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeNode(int nodeindex) {
		
		NodeElement el=osmDocument.getNode(nodeindex);
		try {
			docWriter.append(el.toXMLString());
			docWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int nodeCnt() {
		return osmDocument.nodeCnt();
	}

	@Override
	public int wayCnt() {
		return osmDocument.wayCnt();
	}

	@Override
	public void writeWay(int wayindex) {
		WayElement way=osmDocument.getWay(wayindex);
		try {
			docWriter.append(way.toXMLString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flushDoc() {
		try {
			docWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeDocEnd() {
		try {
			docWriter.append("</osm>");
			docWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
