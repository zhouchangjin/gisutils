package com.iwhere.gisutil.projection;

import com.iwhere.gisutil.model.GPSPoint;

public class ProjectionConvertUtil {

    /**
     * 地球长轴半径
     */
     private  static final double a=6478245.0;

    /**
     * 扁率
     */
    private static final double ee=0.00669342162296594323;

    /**
     *
     */
    private static final double X_PI = Math.PI * 3000.0 / 180.0;

    private static final double PI  = Math.PI;

    /**
     * 判断坐标是否不在中国境内
     * @param wgs84 84坐标
     * @return true 在境外  false 境内
     */
    private static boolean outOfChina(GPSPoint wgs84)
    {
        if (wgs84.getLongitude() < 72.004 || wgs84.getLongitude() > 137.8347)
        {
            return true;
        }
        if (wgs84.getLatitude() < 0.8293 || wgs84.getLatitude() > 55.8271)
        {
            return true;
        }
        return false;
    }

    private static double transformLat(double lng, double lat)
    {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat +
                0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 *
                Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 *
                Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 *
                Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double lng, double lat)
    {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng +
                0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 *
                Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 *
                Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 *
                Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    /**
     * 84坐标系转百度坐标系
     * @param wgs84 84坐标
     * @return 百度坐标
     */
    public static GPSPoint wgs84ToBd09(GPSPoint wgs84)
    {
        // 先从84转为火星坐标
        GPSPoint hx = wgs84ToGcj02(wgs84);
        // 再从火星坐标转为百度坐标
        GPSPoint bd = gcj02ToBd09(hx);
        return bd;
    }

    /**
     * 火星坐标系转百度坐标系
     * @param hxGPSPoint 火星坐标
     * @return 百度坐标
     */
    public static GPSPoint gcj02ToBd09(GPSPoint hxGPSPoint){
        double z = Math.sqrt(hxGPSPoint.getLongitude() * hxGPSPoint.getLongitude()
                + hxGPSPoint.getLatitude() * hxGPSPoint.getLatitude())
                + 0.00002 * Math.sin(hxGPSPoint.getLatitude() * X_PI);
        double theta = Math.atan2(hxGPSPoint.getLatitude(), hxGPSPoint.getLongitude())
                + 0.000003 * Math.cos(hxGPSPoint.getLongitude() * X_PI);
        GPSPoint baiduPoint=new GPSPoint();
        baiduPoint.setLongitude(z * Math.cos(theta) + 0.0065);
        baiduPoint.setLatitude(z * Math.cos(theta) + 0.006);
        return baiduPoint;
    }




    /**
     * 84坐标系转火星坐标（网络通用）
     * 由于火星坐标系是加密坐标系，所以该转换方法是一种近似的坐标转换方法
     * @param wgs84 84坐标
     * @return 火星坐标
     */
    public static GPSPoint wgs84ToGcj02(GPSPoint wgs84){
        if (outOfChina(wgs84)){
            //如果不在国内，不需要转换
            return wgs84;
        }
        double dlat = transformLat(wgs84.getLongitude() - 105.0, wgs84.getLatitude() - 35.0);
        double dlng = transformLng(wgs84.getLongitude() - 105.0, wgs84.getLatitude() - 35.0);
        double radlat = wgs84.getLatitude() / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        GPSPoint hx=new GPSPoint();
        hx.setLatitude( wgs84.getLatitude() + dlat);
        hx.setLongitude( wgs84.getLongitude() + dlng);
        return hx;
    }

    /**
     * 火星坐标转84坐标系
     * @param hx 火星坐标
     * @return 84坐标
     */
    public static GPSPoint gcj02ToWgs84(GPSPoint hx)
    {
        if (outOfChina(hx)){
            return hx;
        }
        double dlat = transformLat(hx.getLongitude() - 105.0, hx.getLatitude() - 35.0);
        double dlng = transformLng(hx.getLongitude() - 105.0, hx.getLatitude() - 35.0);
        double radlat = hx.getLatitude() / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        GPSPoint wgs84=new GPSPoint();
        double mglat = hx.getLatitude() + dlat;
        double mglng = hx.getLongitude() + dlng;
        wgs84.setLatitude(hx.getLatitude() * 2 - mglat);
        wgs84.setLongitude(hx.getLongitude() * 2 - mglng);
        return wgs84;
    }

    /**
     * 百度坐标转火星坐标
     * @param bd 百度坐标
     * @return 火星坐标
     */
    public static GPSPoint bd09ToGcj02(GPSPoint bd)
    {
        double x = bd.getLongitude() - 0.0065;
        double y = bd.getLatitude() - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - .000003 * Math.cos(x * X_PI);
        GPSPoint hx=new GPSPoint();
        hx.setLongitude(z * Math.cos(theta));
        hx.setLatitude(z * Math.sin(theta));
        return hx;
    }

    /**
     * 百度坐标转84坐标
     * @param bd 百度坐标
     * @return 84坐标
     */
    public static GPSPoint bd09ToWgs84(GPSPoint bd)
    {
        GPSPoint hx = bd09ToGcj02(bd);
        GPSPoint wgs84 = gcj02ToWgs84(hx);
        return wgs84;
    }


    public static void main(String[] args) {

    }
}
