package com.iwhere.gisutil.geohash;

import java.util.BitSet;

public class GeoSOTUtil {
	
	
	
	public static long GeoSotCode1D(double angle,int level) {
		if(level<10 && level>=0) {
			long result=(long) Math.floor((angle+256)/Math.pow(2, 9-level));
			return result;
		}else if(level>9 && level<16) {
			long codeL9=GeoSotCode1D(angle, 9); //Math.floor(longitude);
			long partDegree=codeL9*(Math.round(64.0/(long)Math.pow(2, 15-level)));
			double minutes=angle+256-codeL9;
			//System.out.println("minutes "+minutes+"  "+minutes*60);
			long partMinutes=(long)Math.floor(minutes*Math.round(60.0/Math.pow(2, 15-level)));
			return partDegree+partMinutes;
		}else if(level>15 && level<33) {
			long codeL15=GeoSotCode1D(angle, 15);
			long partDegree2Minutes=codeL15*(Math.round(64.0/(long)Math.pow(2, 21-level)));
			double secondsPart=(angle+256)*64-codeL15;
			long codeL9=GeoSotCode1D(angle, 9); 
			//System.out.println("codeL9 ="+codeL9+" ");
			//System.out.println("codeL15 ="+codeL15+" sec "+secondsPart);
			long partSec=(long)Math.floor(secondsPart*(Math.round(60.0/(long)Math.pow(2, 21-level))));
			//System.out.println(partSec);
			return partDegree2Minutes+partSec;
		}else {
			return -1;
		}
	}
	
	public static BitSet longToBitSet(long value) {
		
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
	}
	
	public static BitSet mortonCode(BitSet x,BitSet y,int bitsCnt) {
		BitSet combine=new BitSet();
		int global=0;
		for(int i=0;i<x.length();i+=bitsCnt) {
			for(int j=0;j<bitsCnt;j++) {
				int current=i+j;
				if(y.get(current)) {
					combine.set(global);
				}
				global++;
			}
			
			for(int j=0;j<bitsCnt;j++) {
				int current=i+j;
				if(x.get(current)) {
					combine.set(global);
				}
				global++;
			}
		}
		return combine;
	}
	
	public static long GeoSotCode2D(double longitude,double latitude,int level) {
		
		long codeL=GeoSotCode1D(longitude, level);
		long codeB=GeoSotCode1D(latitude, level);
		
		BitSet bitsetL=longToBitSet(codeL);
		BitSet bitsetB=longToBitSet(codeB);
		
		BitSet combine=mortonCode(bitsetL, bitsetB, 4);

		//System.out.println(currentIndex+"_"+bitsetB.length()+"_"+combine.length());
		long result[]=combine.toLongArray();
		/**
		System.out.println("==============Lat=======================");
		System.out.println(bitsetB);
		System.out.println(bitsetB.toLongArray()[0]);
		System.out.println("==============Long=======================");
		System.out.println(bitsetL);
		System.out.println(bitsetL.toLongArray()[0]);
		System.out.println("================Combine=============================");
		System.out.println(combine);
		**/
		return result[0];
	}

	public static void main(String[] args) {
		long code=GeoSotCode2D(116.48299, 39.99451, 20);
		System.out.println(Long.toHexString(code));
		//System.out.println(Long.MAX_VALUE);
	}

}
