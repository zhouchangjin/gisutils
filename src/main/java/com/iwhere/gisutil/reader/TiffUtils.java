package com.iwhere.gisutil.reader;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TiffUtils {

    public static final double scalefactor=1.0;

    public static BufferedImage CalcSlop3Band(RenderedImage image, double cellsize){

        Raster data=image.getData();
        int width=data.getWidth();
        int height=data.getHeight();
        BufferedImage out=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                double[] pixel=new double[1];
                double rgb[]=data.getPixel(j,i,pixel);
                double[] pixelWindow=new double[9];
                int[] pixelWindowWeight=new int[9]; //用来计算权重
                pixelWindow[4]=rgb[0];
                pixelWindowWeight[4]=0;
                if(j>0 && i>0){  //左上角
                    double rgb_ul[]=data.getPixel(j-1,i-1,pixel);
                    pixelWindow[0]=rgb_ul[0];
                    if(rgb_ul[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[0]=1;
                    }else{
                        pixelWindowWeight[0]=0;
                    }
                }else{
                    //处于边缘位置不记录
                    pixelWindow[0]=0;
                    pixelWindowWeight[0]=0;
                }
                if(i>0){  //正上方
                    double rgb_u[]=data.getPixel(j,i-1,pixel);
                    pixelWindow[1]=rgb_u[0];
                    if(rgb_u[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[1]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[1]=0;
                    }
                }else{
                    pixelWindow[1]=0;
                    pixelWindowWeight[1]=0;
                }
                if(j<width-1 && i>0){ //右上方
                    double rgb_ur[]=data.getPixel(j+1,i-1,pixel);
                    pixelWindow[2]=rgb_ur[0];
                    if(rgb_ur[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[2]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[2]=0;
                    }
                }else {
                    pixelWindow[2]=0;
                    pixelWindowWeight[2]=0;
                }

                if(j>0){ //正西
                    double rgb_l[]=data.getPixel(j-1,i,pixel);
                    pixelWindow[3]=rgb_l[0];
                    if(rgb_l[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[3]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[3]=0;
                    }
                }else{
                    pixelWindow[3]=0;
                    pixelWindowWeight[3]=0;
                }

                if(j<width-1){ //正东
                    double rgb_r[]=data.getPixel(j+1,i,pixel);
                    pixelWindow[5]=rgb_r[0];
                    if(rgb_r[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[5]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[5]=0;
                    }
                }else{
                    pixelWindow[5]=0;
                    pixelWindowWeight[5]=0;
                }

                if(j>0 && i<height-1){ //左下
                    double rgb_bl[]=data.getPixel(j-1,i+1,pixel);
                    pixelWindow[6]=rgb_bl[0];
                    if(rgb_bl[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[6]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[6]=0;
                    }
                }else{
                    pixelWindow[6]=0;
                    pixelWindowWeight[6]=0;
                }

                if(i<height-1){ //正下
                    double rgb_b[]=data.getPixel(j,i+1,pixel);
                    pixelWindow[7]=rgb_b[0];
                    if(rgb_b[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[7]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[7]=0;
                    }
                }else{
                    pixelWindow[7]=0;
                    pixelWindowWeight[7]=0;
                }

                if(i<height-1 && j<width-1){
                    double rgb_br[]=data.getPixel(j+1,i+1,pixel);
                    pixelWindow[8]=rgb_br[0];
                    if(rgb_br[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[8]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[8]=0;
                    }
                }else{
                    pixelWindow[8]=0;
                    pixelWindowWeight[8]=0;
                }

                double slopeHorizontal=calcPixelWindowSlopeHorizontal(pixelWindow,pixelWindowWeight,cellsize);
                double slopeAngle=calcPixelWindowSlopeMethod1(pixelWindow,pixelWindowWeight,cellsize);
                double slopeVertical=calcPixelWindowSlopeVertical(pixelWindow,pixelWindowWeight,cellsize);
                int rValue=(int)Math.round(Math.toDegrees(slopeAngle)*scalefactor);
                int gValue=(int)Math.round(Math.toDegrees(slopeHorizontal)*scalefactor);
                int bValue=(int)Math.round(Math.toDegrees(slopeVertical)*scalefactor);
                out.getRaster().setPixel(j,i,new int[]{rValue,gValue,bValue});
            }

        }
        return out;
    }


    private static double calcPixelWindowSlopeMethod1(double[] pixelWindow,int[] pixelWindowWeight,double cellsize) {

        double hw=pixelWindow[0]*pixelWindowWeight[0]
                +pixelWindow[3]*pixelWindowWeight[3]
                +pixelWindow[6]*pixelWindowWeight[6];
        double he=pixelWindow[2]*pixelWindowWeight[2]
                +pixelWindow[5]*pixelWindowWeight[5]
                +pixelWindow[8]*pixelWindowWeight[8];

        double hn=pixelWindow[0]*pixelWindowWeight[0]
                +pixelWindow[1]*pixelWindowWeight[1]
                +pixelWindow[2]*pixelWindowWeight[2];
        double hs=pixelWindow[6]*pixelWindowWeight[6]
                +pixelWindow[7]*pixelWindowWeight[7]
                +pixelWindow[8]*pixelWindowWeight[8];

        double availableCellCntWE=pixelWindowWeight[0]+pixelWindowWeight[3]+pixelWindowWeight[6]
                +pixelWindowWeight[2]+pixelWindowWeight[5]+pixelWindowWeight[8];
        double availableCellCntSN=pixelWindowWeight[0]+pixelWindowWeight[1]+pixelWindowWeight[2]
                +pixelWindowWeight[6]+pixelWindowWeight[7]+pixelWindowWeight[8];
        if(availableCellCntSN==0 || availableCellCntWE==0){
            return -32767;
        }
        double dx=(hw-he)/(availableCellCntWE*cellsize);  // dz=hw-he  dx=cellnum*cellsize

        double dy=(hs-hn)/(availableCellCntSN*cellsize);
        double slope=Math.sqrt(dx*dx+dy*dy);  //坡度，勾股定理

        double slopeAngle=Math.atan(slope);
        return slopeAngle;
    }

    private static double calcPixelWindowSlopeVertical(double[] pixelWindow, int[] pixelWindowWeight, double cellsize) {


        double hn=pixelWindow[0]*pixelWindowWeight[0]
                +pixelWindow[1]*pixelWindowWeight[1]
                +pixelWindow[2]*pixelWindowWeight[2];
        double hs=pixelWindow[6]*pixelWindowWeight[6]
                +pixelWindow[7]*pixelWindowWeight[7]
                +pixelWindow[8]+pixelWindowWeight[8];

        double availableCellCntSN=pixelWindowWeight[0]+pixelWindowWeight[1]+pixelWindowWeight[2]
                +pixelWindowWeight[6]+pixelWindowWeight[7]+pixelWindowWeight[8];
        if(availableCellCntSN==0){
            return -32767;
        }

        double dy=(hs-hn)/(availableCellCntSN*cellsize);

        double slope=Math.sqrt(dy*dy);  //坡度y
        double slopeAngle=Math.atan(slope);
        return slopeAngle;

    }

    private static double calcPixelWindowSlopeHorizontal(double[] pixelWindow, int[] pixelWindowWeight, double cellsize) {
        double hw=pixelWindow[0]*pixelWindowWeight[0]
                +pixelWindow[3]*pixelWindowWeight[3]
                +pixelWindow[6]*pixelWindowWeight[6];
        double he=pixelWindow[2]*pixelWindowWeight[2]
                +pixelWindow[5]*pixelWindowWeight[5]
                +pixelWindow[8]*pixelWindowWeight[8];

        double availableCellCntWE=pixelWindowWeight[0]+pixelWindowWeight[3]+pixelWindowWeight[6]
                +pixelWindowWeight[2]+pixelWindowWeight[5]+pixelWindowWeight[8];
        if(availableCellCntWE==0){
            return -32767;
        }
        double dx=(hw-he)/(availableCellCntWE*cellsize);  // dz=hw-he  dx=cellnum*cellsize
        double slope=Math.sqrt(dx*dx);  //坡度x
        double slopeAngle=Math.atan(slope);
        return slopeAngle;
    }

    public static BufferedImage CalcSlop(RenderedImage image, double cellsize){

        Raster data=image.getData();
        int width=data.getWidth();
        int height=data.getHeight();
        BufferedImage out=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                double[] pixel=new double[1];
                double rgb[]=data.getPixel(j,i,pixel);
                double[] pixelWindow=new double[9];
                int[] pixelWindowWeight=new int[9]; //用来计算权重
                pixelWindow[4]=rgb[0];
                pixelWindowWeight[4]=0;
                if(j>0 && i>0){  //左上角
                    double rgb_ul[]=data.getPixel(j-1,i-1,pixel);
                    pixelWindow[0]=rgb_ul[0];
                    if(rgb_ul[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[0]=1;
                    }else{
                        pixelWindowWeight[0]=0;
                    }
                }else{
                    //处于边缘位置不记录
                    pixelWindow[0]=0;
                    pixelWindowWeight[0]=0;
                }
                if(i>0){  //正上方
                    double rgb_u[]=data.getPixel(j,i-1,pixel);
                    pixelWindow[1]=rgb_u[0];
                    if(rgb_u[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[1]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[1]=0;
                    }
                }else{
                    pixelWindow[1]=0;
                    pixelWindowWeight[1]=0;
                }
                if(j<width-1 && i>0){ //右上方
                    double rgb_ur[]=data.getPixel(j+1,i-1,pixel);
                    pixelWindow[2]=rgb_ur[0];
                    if(rgb_ur[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[2]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[2]=0;
                    }
                }else {
                    pixelWindow[2]=0;
                    pixelWindowWeight[2]=0;
                }

                if(j>0){ //正西
                    double rgb_l[]=data.getPixel(j-1,i,pixel);
                    pixelWindow[3]=rgb_l[0];
                    if(rgb_l[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[3]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[3]=0;
                    }
                }else{
                    pixelWindow[3]=0;
                    pixelWindowWeight[3]=0;
                }

                if(j<width-1){ //正东
                    double rgb_r[]=data.getPixel(j+1,i,pixel);
                    pixelWindow[5]=rgb_r[0];
                    if(rgb_r[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[5]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[5]=0;
                    }
                }else{
                    pixelWindow[5]=0;
                    pixelWindowWeight[5]=0;
                }

                if(j>0 && i<height-1){ //左下
                    double rgb_bl[]=data.getPixel(j-1,i+1,pixel);
                    pixelWindow[6]=rgb_bl[0];
                    if(rgb_bl[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[6]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[6]=0;
                    }
                }else{
                    pixelWindow[6]=0;
                    pixelWindowWeight[6]=0;
                }

                if(i<height-1){ //正下
                    double rgb_b[]=data.getPixel(j,i+1,pixel);
                    pixelWindow[7]=rgb_b[0];
                    if(rgb_b[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[7]=2; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[7]=0;
                    }
                }else{
                    pixelWindow[7]=0;
                    pixelWindowWeight[7]=0;
                }

                if(i<height-1 && j<width-1){
                    double rgb_br[]=data.getPixel(j+1,i+1,pixel);
                    pixelWindow[8]=rgb_br[0];
                    if(rgb_br[0]>=-999){
                        //tiff里的数据如果是nodata，值为-32767或其他的用户指定的较小负数，因为是dem所以较小的负数直接为无效数据
                        pixelWindowWeight[8]=1; //上下左右四个格子权重为2
                    }else{
                        pixelWindowWeight[8]=0;
                    }
                }else{
                    pixelWindow[8]=0;
                    pixelWindowWeight[8]=0;
                }

                double slopeAngle=calcPixelWindowSlopeMethod1(pixelWindow,pixelWindowWeight,cellsize);
                int rgbValue=(int)Math.floor(slopeAngle*scalefactor);
                out.getRaster().setPixel(j,i,new int[]{rgbValue,rgbValue,rgbValue});
            }
            
        }
        return out;
    }


    public static void main(String[] args) {
        File file = new File("d:/hainan.tif");
        AbstractGridFormat format = GridFormatFinder.findFormat( file );
        GridCoverage2DReader reader = format.getReader( file );
        System.out.println(reader.getCoordinateReferenceSystem());
        GridCoverageFactory gcf=new GridCoverageFactory();
        GridCoverage2D coverage = null;
        try {
            coverage = (GridCoverage2D) reader.read(null);
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
            Envelope env = coverage.getEnvelope();
            RenderedImage image = coverage.getRenderedImage();
            Raster data=image.getData();
            System.out.println(data.getWidth()+"======="+data.getHeight());
            GridGeometry2D gg = coverage.getGridGeometry();
            DirectPosition2D posWorld = new DirectPosition2D(118,26);
            GridCoordinates2D posGrid = gg.worldToGrid(posWorld);
            GridCoordinates2D initGrid=new GridCoordinates2D(0,0);
            GridCoordinates2D neGrid=new GridCoordinates2D(1,0);
            DirectPosition world=gg.gridToWorld(initGrid);
            DirectPosition world2=gg.gridToWorld(neGrid);
            System.out.println(posGrid.x+"==="+posGrid.y);
            System.out.println(world.getCoordinate()[0]+","+world.getCoordinate()[1]);
            System.out.println(world2.getCoordinate()[0]+","+world.getCoordinate()[1]);
            BufferedImage outImage= CalcSlop3Band(image,30);
            ImageIO.write(outImage,"PNG",new FileOutputStream("d:/out1.png"));

            ReferencedEnvelope re=new ReferencedEnvelope(coverage.getEnvelope2D(),crs);
            GridCoverage2D gc = gcf.create("name", outImage, re);
            GeoTiffWriter geoTiffWriter=new GeoTiffWriter(new File("d:/out.tif"));
            geoTiffWriter.write(gc,null);
            geoTiffWriter.dispose();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main1(String[] args) {
        File file = new File("/usr/local/tomcat/temp/zip-download/hainan_slope.tif");
        AbstractGridFormat format = GridFormatFinder.findFormat( file );
        GridCoverage2DReader reader = format.getReader( file );
        System.out.println(reader.getCoordinateReferenceSystem());

        GridCoverage2D coverage = null;
        try {
            coverage = (GridCoverage2D) reader.read(null);
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
            Envelope env = coverage.getEnvelope();
            RenderedImage image = coverage.getRenderedImage();
            Raster data=image.getData();

            GridGeometry2D gg = coverage.getGridGeometry();
            DirectPosition2D posWorld = new DirectPosition2D(118,26);
            GridCoordinates2D posGrid = gg.worldToGrid(posWorld);
            double[] pixel=new double[1];
            System.out.println(posGrid.x+"==="+posGrid.y);
            double rgb[]=data.getPixel(posGrid.x,posGrid.y,pixel);
            System.out.println(rgb[0]);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

    }
}
