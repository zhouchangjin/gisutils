package com.iwhere.gisutil.converter.geojson;

import com.iwhere.gisutil.file.FileUtil;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shape2GeoJSON {

    /**
     * 保存features为shp格式
     *
     * @param features 要素类
     * @param TYPE     要素类型
     * @param shpPath  shp保存路径
     * @return 是否保存成功
     */
    public static boolean saveFeaturesToShp(List<SimpleFeature> features, SimpleFeatureType TYPE, String shpPath) {
        try {
            ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
            File shpFile = new File(shpPath);
            Map<String, Serializable> params = new HashMap<>();
            params.put("url", shpFile.toURI().toURL());
            params.put("create spatial index", Boolean.TRUE);

            ShapefileDataStore newDataStore =
                    (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
            newDataStore.setCharset(StandardCharsets.UTF_8);

            newDataStore.createSchema(TYPE);

            Transaction transaction = new DefaultTransaction("create");
            String typeName = newDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

            if (featureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
                featureStore.setTransaction(transaction);
                try {
                    featureStore.addFeatures(collection);
                    FileUtil.generateCpgFile(shpPath, StandardCharsets.UTF_8);
                    transaction.commit();
                } catch (Exception problem) {
                    problem.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } else {
                System.out.println(typeName + " does not support read/write access");
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }



    /**
     * GeoJson to Shp
     *
     * @param geojsonPath geojson 文件路径
     * @param shpPath     shp 文件路径
     * @return 转换是否成功
     */
    public static boolean transformGeoJsonToShp(String geojsonPath, String shpPath) {
        try {
            // open geojson
            InputStream in = new FileInputStream(geojsonPath);
            GeometryJSON gjson = new GeometryJSON();
            FeatureJSON fjson = new FeatureJSON(gjson);
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = fjson.readFeatureCollection(in);
            // convert schema for shapefile
            SimpleFeatureType schema = features.getSchema();
            GeometryDescriptor geom = schema.getGeometryDescriptor();
            // geojson文件属性
            List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
            // geojson文件空间类型（必须在第一个）
            GeometryType geomType = null;
            List<AttributeDescriptor> attribs = new ArrayList<>();
            for (AttributeDescriptor attrib : attributes) {
                AttributeType type = attrib.getType();
                if (type instanceof GeometryType) {
                    geomType = (GeometryType) type;
                } else {
                    attribs.add(attrib);
                }
            }
            if (geomType == null)
                return false;

            // 使用geomType创建gt
            GeometryTypeImpl gt = new GeometryTypeImpl(new NameImpl("the_geom"), geomType.getBinding(),
                    geom.getCoordinateReferenceSystem() == null ? DefaultGeographicCRS.WGS84 : geom.getCoordinateReferenceSystem(), // 用户未指定则默认为wgs84
                    geomType.isIdentified(), geomType.isAbstract(), geomType.getRestrictions(),
                    geomType.getSuper(), geomType.getDescription());

            // 创建识别符
            GeometryDescriptor geomDesc = new GeometryDescriptorImpl(gt, new NameImpl("the_geom"), geom.getMinOccurs(),
                    geom.getMaxOccurs(), geom.isNillable(), geom.getDefaultValue());

            // the_geom 属性必须在第一个
            attribs.add(0, geomDesc);

            SimpleFeatureType outSchema = new SimpleFeatureTypeImpl(schema.getName(), attribs, geomDesc, schema.isAbstract(),
                    schema.getRestrictions(), schema.getSuper(), schema.getDescription());
            List<SimpleFeature> outFeatures = new ArrayList<>();
            try (FeatureIterator<SimpleFeature> features2 = features.features()) {
                while (features2.hasNext()) {
                    SimpleFeature f = features2.next();
                    SimpleFeature reType = DataUtilities.reType(outSchema, f, true);

                    reType.setAttribute(outSchema.getGeometryDescriptor().getName(),
                            f.getAttribute(schema.getGeometryDescriptor().getName()));

                    outFeatures.add(reType);
                }
            }
            return saveFeaturesToShp(outFeatures, outSchema, shpPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean transformShpToGeoJson(String shpPath, String geojsonPath,String shpCharsetName) {
        try {
            File file = new File(shpPath);
            FileDataStore myData = FileDataStoreFinder.getDataStore(file);
            // 设置解码方式
            ((ShapefileDataStore) myData).setCharset(Charset.forName(shpCharsetName));
            SimpleFeatureSource source = myData.getFeatureSource();
            SimpleFeatureType schema = source.getSchema();
            Query query = new Query(schema.getTypeName());

            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
            FeatureJSON fjson = new FeatureJSON();
            File geojson = new File(geojsonPath);
            try (FeatureIterator<SimpleFeature> featureIterator = collection.features();
                 StringWriter writer = new StringWriter();
                 BufferedWriter buffer = new BufferedWriter(Files.newBufferedWriter(geojson.toPath(), StandardCharsets.UTF_8))) {
                writer.write("{\"type\":\"FeatureCollection\",\"crs\":");
                fjson.writeCRS(schema.getCoordinateReferenceSystem(), writer);
                writer.write(",");
                writer.write("\"features\":");
                writer.write("[");
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = featureIterator.next();
                    fjson.writeFeature(feature, writer);
                    if (featureIterator.hasNext())
                        writer.write(",");
                }
                writer.write("]");
                writer.write("}");
                buffer.write(writer.toString());
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {

            /**
            String filePath="D:/test/beijing-路网_polyline.shp";
            String geojsonPath="d:/out.geojson";
            transformShpToGeoJson(filePath,geojsonPath,"GBK");
            **/


            String geojsonPath1="d:/test/140321.geoJson";
            String shapPath1="d:/out.shp";
            transformGeoJsonToShp(geojsonPath1,shapPath1);

    }

}
