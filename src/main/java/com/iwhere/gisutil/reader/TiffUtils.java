package com.iwhere.gisutil.reader;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class TiffUtils {


    public static void main(String[] args) {
        File file = new File("d:/BaiduNetdiskDownload/fujian.tif");
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
