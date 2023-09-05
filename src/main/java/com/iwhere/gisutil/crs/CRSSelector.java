package com.iwhere.gisutil.crs;

import org.geotools.referencing.CRS;

public class CRSSelector {
    /**
     * 根据经纬度选择北京54分带
     * 北京54坐标系为0度精度为ZONE1，每隔6度为一带，所以例如 120E 代表的是ZONE21
     * 但北京54坐标系只能覆盖ZONE13~ZONE20
     * @param longitude
     * @return 坐标系的EPSGCode
     */
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

    /**
     * UTM分带选择，和北京54一样也是6度一带，起始0度带是E180，所以E120 就是50带
     * @param longitude
     * @param latitude
     * @return 坐标系的EPSGCode
     */
    public static CRSEnum getUTMCRSByGPSLocation(double longitude,double latitude){
        double offset=longitude+180;
        String halfStr="N";
        if(latitude<0){
            halfStr="S";
        }
        int intLng=(int)Math.floor(offset);
        int zone_num=intLng/6+1;
        String epsgCodeStr="UTM_ZONE"+zone_num+halfStr;
        try{
            CRSEnum en=CRSEnum.valueOf(epsgCodeStr);
            return en;
        }catch (IllegalArgumentException e){
            return CRSEnum.UNKNOWN;
        }

    }
}
