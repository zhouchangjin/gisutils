package com.iwhere.gisutil.converter.osm.model.names;

/**
 * 基础地理信息分类码GB
 */
public enum GBCodeEnum {

    LRDL_CJGL("420000","0620","城际公路","LineString","LRDL","公路"),
    LRDL_GD("420100","0620","国道","LineString","LRDL","公路"),
    LRDL_GDJC("420101","0620","国道建成","LineString","LRDL","公路"),
    LRDL_GDJZZ("420102","0620","国道建筑中","LineString","LRDL","公路"),
    LRDL_SD("420200","0620","省道","LineString","LRDL","公路"),
    LRDL_SDJC("420201","0620","省道建成","LineString","LRDL","公路"),
    LRDL_SDJZZ("420202","0620","省道建筑中","LineString","LRDL","公路"),
    LRDL_XD("420300","0620","县道","LineString","LRDL","公路"),
    LRDL_XDJC("420301","0620","县道建成","LineString","LRDL","公路"),
    LRDL_XDJZZ("420302","0620","县道建筑中","LineString","LRDL","公路"),
    LRDL_XIANGD("420400","0620","乡道","LineString","LRDL","公路"),

    LRDL_ZYGL("420500","0620","专用公路","LineString","LRDL","公路"),

    LRDL_ZD("420600","0620","匝道","LineString","LRDL","公路"),

    LCTL_CSDL("430000","0630","城市道路","LineString","LCTL","城市道路"),

    LCTL_GDJT("430100","0630","轨道交通","LineString","LCTL","城市道路"),

    LCTL_DT("430101","0630","地铁","LineString","LCTL","城市道路"),
    LCTL_QGCXF("430102","0630","轻轨|磁悬浮","LineString","LCTL","城市道路"),
    LCTL_YGDC("430103","0630","有轨电车","LineString","LCTL","城市道路"),

    LCTL_KSL("430200","0630","快速路","LineString","LCTL","城市道路"),

    LCTL_GJL("430300","0630","高架路","LineString","LCTL","城市道路"),
    LCTL_YD("430400","0630","引道","LineString","LCTL","城市道路"),

    LCTL_JD("430500","0630","街道","LineString","LCTL","城市道路"),

    LCTL_ZGD("430501","0630","街道-主干道","LineString","LCTL","城市道路"),

    LCTL_CGD("430502","0630","街道-次干道","LineString","LCTL","城市道路"),

    LCTL_ZX("430503","0630","街道-支线","LineString","LCTL","城市道路"),
    LCTL_NBDL("430600","0630","内部道路","LineString","LCTL","城市道路"),

    LVLL_XCDL("440000","0640","乡村道路","LineString","LCTL","乡村道路"),

    LVLL_JGL("440100","0640","机耕路","LineString","LCTL","乡村道路"),

    LVLL_XCL("440200","0640","乡村路","LineString","LCTL","乡村道路"),

    LVLL_XL("440300","0640","小路","LineString","LCTL","乡村道路"),

    LVLL_SLL("440400","0640","时令路","LineString","LCTL","乡村道路"),

    LVLL_SA("440500","0640","山隘","LineString","LCTL","乡村道路"),

    LVLL_ZD("440600","0640","栈道","LineString","LCTL","乡村道路");

    GBCodeEnum(String gbcode,String cccode,String fName,
               String geoType,String layername,String cName){
        geometryType=geoType;
        gbCode=gbcode;
        layerName=layername;
        featureName=fName;
        ccCode=gbcode;
        className=cName;

    }

    /**
     * 几何类别
     */
    String geometryType;

    /**
     * 国标码
     */
    String gbCode;

    /**
     * 要素分层
     */
    String layerName;

    /**
     * 要素名称
     */
    String featureName;

    /**
     * cc码
     */
    String ccCode;

    /**
     * 分类名称
     */
    String className;

    public String getGeometryType() {
        return geometryType;
    }

    public String getGbCode() {
        return gbCode;
    }

    public String getLayerName() {
        return layerName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getCcCode() {
        return ccCode;
    }

    public String getClassName() {
        return className;
    }
}
