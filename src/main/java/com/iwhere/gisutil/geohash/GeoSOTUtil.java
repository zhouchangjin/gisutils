package com.iwhere.gisutil.geohash;

import java.math.BigInteger;
import java.util.BitSet;

public class GeoSOTUtil {
	
	
	
	public static long geoSotCode1D(double angle,int level) {
		if(level<10 && level>=0) {
			long result=(long) Math.floor((angle+256)/Math.pow(2, 9-level));
			return result;
		}else if(level>9 && level<16) {
			long codeL9=geoSotCode1D(angle, 9); //Math.floor(longitude);
			long partDegree=codeL9*(Math.round(64.0/(long)Math.pow(2, 15-level)));
			double minutes=angle+256-codeL9;
			long partMinutes=(long)Math.floor(minutes*Math.round(60.0/Math.pow(2, 15-level)));
			return partDegree+partMinutes;
		}else if(level>15 && level<33) {
			long codeL15=geoSotCode1D(angle, 15);
			long partDegree2Minutes=codeL15*(Math.round(64.0/Math.pow(2, 21-level)));
			double secondsPart=(angle+256)*64-codeL15;
			long partSec=(long)Math.floor(secondsPart*(Math.round(60.0/Math.pow(2, 21-level))));
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

	public static String bitSetToBinaryString(BitSet bitset){
		StringBuffer sb=new StringBuffer();
		for(int i=bitset.length()-1;i>=0;i--){
			sb.append(bitset.get(i)?1:0);
		}
		return sb.toString();
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

	public static byte[] toByteArray(BitSet bits) {
		byte[] bytes = new byte[bits.length() / 8 + 1];
		for (int i = 0; i < bits.length(); i++) {
			if (bits.get(i)) {
				bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
			}
		}
		return bytes;
	}

	public static BigInteger geosotBigIntegerCode2D(double longitude,double latitude,int level,int bitCnt){
		BitSet bitsetCode=geoSotCode2D(longitude,latitude,level,bitCnt);
		BigInteger bigInteger=new BigInteger(toByteArray(bitsetCode));
		return bigInteger;
	}

	public  static long geosotLongCode2D(double longitude,double latitude,int level,int bitCnt){
		if(level>31){
			System.out.println("超出long表示范围");
			return -1;
		}
		BitSet bitsetCode=geoSotCode2D(longitude,latitude,level,bitCnt);
		long result[]=bitsetCode.toLongArray();
		return result[0];

	}
	
	public static BitSet geoSotCode2D(double longitude,double latitude,int level,int bitCnt) {
		
		long codeL=geoSotCode1D(longitude, level);
		long codeB=geoSotCode1D(latitude, level);
		
		BitSet bitsetL=longToBitSet(codeL);
		BitSet bitsetB=longToBitSet(codeB);

		BitSet combine=mortonCode(bitsetL, bitsetB, bitCnt);
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
		return combine;
	}

	public static void main(String[] args) {
		long code=geosotLongCode2D(116.48299, 39.99451, 20,4);
		long code2=geosotLongCode2D(116.48153, 39.99407, 20,4);
		long code3=geosotLongCode2D(116.48251, 39.99354, 20,4);
		System.out.println(Long.toHexString(code)+"--"+code);
		System.out.println(Long.toHexString(code2)+"--"+code2);
		System.out.println(Long.toHexString(code3)+"--"+code3);

		long code1=geosotLongCode2D(116.48299, 39.99451, 31,4);
		BitSet bs=longToBitSet(code1);
		String out=bitSetToBinaryString(bs);
		System.out.println(code1);
		System.out.println(out);
		System.out.println(Long.MAX_VALUE);

		BigInteger bigInteger=geosotBigIntegerCode2D(116.48299, 39.99451, 32,4);
		System.out.println(bigInteger);
	}

}
