package com.iwhere.gisutil.crs;


import com.iwhere.gisutil.model.GPSPoint;
import com.iwhere.gisutil.model.ProjectXYPoint;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.security.cert.CRLSelector;

public class CRSConvertUtil {

    public static DirectPosition transformCRS(GeneralDirectPosition srcPt,String EPSGSource,String EPSGTarget){
        try {
            CoordinateReferenceSystem sourceCRS = CRS.decode(EPSGSource);
            CoordinateReferenceSystem targetCRS = CRS.decode(EPSGTarget);
            MathTransform tr = CRS.findMathTransform(sourceCRS, targetCRS);
            DirectPosition targetPt = tr.transform(srcPt, null);
            return targetPt;
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点坐标转换
     * @param x  如果是平面坐标 x轴向北，如果是经纬度x为维度
     * @param y  如果是平面坐标 y轴向东 如果是经纬度y为经度
     * @param from  EPSG代码
     * @param to    EPSG代码
     * @return
     */
    public static DirectPosition transformCRS(double x,double y,String from,String to){
        GeneralDirectPosition position=new GeneralDirectPosition(x,y);
        return transformCRS(position,from,to);
    }

    /**
     * 坐标转换
     * @param x 如果是平面坐标 x轴向北，如果是经纬度x为维度
     * @param y 如果是平面坐标 y轴向东 如果是经纬度y为经度
     * @param from  枚举  源坐标系
     * @param to    枚举 目标坐标系
     * @return
     */
    public static DirectPosition transformCRS(double x,double y,CRSEnum from,CRSEnum to){
        return transformCRS(x,y,from.getCode(),to.getCode());
    }

    public static DirectPosition transformCRS(GeneralDirectPosition srcPoint, CRSEnum from, CRSEnum to){
        return transformCRS(srcPoint,from.getCode(),to.getCode());
    }

    public static ProjectXYPoint transform84ToMercator(double longitude,double latitude){
        return transform84ToProjectCRS(longitude,latitude,CRSEnum.MERCATOR.getCode());
    }


    public static ProjectXYPoint transform84ToProjectCRS(double longitude,double latitude, String EPSGCode){
        DirectPosition pos=transformCRS(latitude,longitude,CRSEnum.WGS84.getCode(),EPSGCode);
        ProjectXYPoint p=new ProjectXYPoint(pos.getCoordinate()[0],pos.getCoordinate()[1]);
        return p;
    }

    public  static ProjectXYPoint transform84ToBeijing54(double longitude,double latitude,int zoneNum){
        String code="BEIJING54_ZONE"+zoneNum;
        CRSEnum crs=CRSEnum.valueOf(code);
        return  transform84ToProjectCRS(longitude,latitude,crs.getCode());
    }

    public static ProjectXYPoint transform84ToBeijing54(double longitude,double latitude){
        CRSEnum crs=CRSSelector.getBeijing54CRSByGPSLocation(longitude);
        return transform84ToProjectCRS(longitude,latitude,crs.getCode());
    }

    public static ProjectXYPoint transform84ToUTM(double logitude,double latitude){
        CRSEnum crs= CRSSelector.getUTMCRSByGPSLocation(logitude,latitude);
        return transform84ToProjectCRS(logitude,latitude,crs.getCode());
    }

    /**
     * 转换84坐标到UTM坐标（北半球）
     * @param longitude 经度
     * @param latitude 维度
     * @param zoneNum  分带号
     * @return 坐标
     */
    public static ProjectXYPoint transform84ToUTMNorth(double longitude,double latitude,int zoneNum){
        String code="UTM_ZONE"+zoneNum+"N";
        CRSEnum crs=CRSEnum.valueOf(code);
        return transform84ToProjectCRS(longitude,latitude,crs.getCode());
    }

    public static ProjectXYPoint transform84ToUTM(double longitude,double latitude,double clng,double clat){
        CRSEnum crs=CRSSelector.getUTMCRSByGPSLocation(clng,clat);
        return transform84ToProjectCRS(longitude,latitude,crs.getCode());
    }


    public static ProjectXYPoint transform84ToProjectCRS(GPSPoint gpsPoint, String EPSGCode){
        return transform84ToProjectCRS(gpsPoint.getLongitude(),gpsPoint.getLatitude(),EPSGCode);
    }

    public static void main(String[] args) {
        /**
        ProjectXYPoint p=transform84ToMercator(119,29);
        System.out.println(p);

        ProjectXYPoint pn=transform84ToMercator(119,23);
        System.out.println(pn);

        System.out.println(transform84ToUTM(113,23));
        System.out.println(transform84ToUTM(113,23,119,23));
        System.out.println(transform84ToUTM(108,23,113,23));
        ProjectXYPoint p2=transform84ToProjectCRS(119.23,31.23,CRSEnum.BEIJING54_ZONE20.getCode());
        System.out.println(p2);

        ProjectXYPoint p3=transform84ToBeijing54(119.23,31.23);
        System.out.println(p3);
         **/
        //在中间纬度范围，变形小
        //转换UTM 50N分带
        System.out.println(transform84ToUTMNorth(119,23,50));
        //转换UTM 49N分带 因为分带换了，经度减6，小范围内刚好是等比例的，所以坐标一样
        System.out.println(transform84ToUTMNorth(113,23,49));
        //转换UTM 48N分带 因为分带换了，经度减6，小范围内刚好是等比例的，所以坐标一样
        System.out.println(transform84ToUTMNorth(107,23,48));

        //北京54 -20 带
        System.out.println(transform84ToBeijing54(119,24,20));

        //System.out.println(transform84ToProjectCRS(119,23,CRSEnum.CGCS2000_PCS.getCode()));

    }


}
