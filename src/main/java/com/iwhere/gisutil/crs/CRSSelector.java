package com.iwhere.gisutil.crs;

import org.geotools.referencing.CRS;

public class CRSSelector {

    public static CRSEnum getBeijing54CRSByGPSLocation(double longitude){
        if(longitude<72){
            return CRSEnum.BEIJING54_ZONE13;
        }else if(longitude>125){
            return CRSEnum.BEIJING54_ZONE20;
        }else{
            int intLng=(int)Math.floor(longitude);
            int zone_num=intLng/6+1;
            String epsgCodeStr="BEIJING54_ZONE"+zone_num;
            return CRSEnum.valueOf(epsgCodeStr);
        }
    }
}
