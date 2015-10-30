package org.maptalks.javasdk;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import org.maptalks.gis.core.geojson.Geometry;
import org.maptalks.gis.core.geojson.json.GeoJSONFactory;

/**
 * 空间查询条件类
 *
 * 对于JSON反序列化,
 * JSON字符串无法直接用fastjson, jackson等JSON库反序列化为SpatialFilter对象
 * 请调用SpatialFilter.create方法来进行JSON反序列化
 *
 * @author fuzhen
 *
 */
public class SpatialFilter {
    // 图形关系常量定义
    //相交关系
    public final static int RELATION_INTERSECT = 0;
    //包含关系
    public final static int RELATION_CONTAIN = 1;
    //分离关系
    //  public final static int RELATION_DISJOINT = 2;
    //重叠关系
    public final static int RELATION_OVERLAP = 3;
    //相切关系
    public final static int RELATION_TOUCH = 4;
    //被包含关系
    public final static int RELATION_WITHIN = 5;
    //以下是非标准的图形关系
    /**
     * 相交但不包含关系即within 或 overlap
     */
    public final static int RELATION_INTERECTNOTCONTAIN = 100;
    /**
     * 包含中心点
     */
    public final static int RELATION_CONTAINCENTER = 101;
    /**
     * 中心点被包含
     */
    public final static int RELATION_CENTERWITHIN = 102;
    /**
     * 空间关系比较的geometry对象
     */
    private Geometry geometry;
    /**
     * 空间关系，取值范围从上面定义的关系常量中取得
     */
    private int relation;

    public SpatialFilter() {

    }

    public SpatialFilter(Geometry geometry, int relation) {
        this.geometry = geometry;
        this.relation = relation;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(final Object geometry) {
        if (geometry instanceof JSONObject) {
            String type = ((JSONObject) geometry).getString("type");
            if (type != null) {
                Class clazz = GeoJSONFactory.getGeoJsonType(type);
                Geometry geo = (Geometry) JSON.toJavaObject(((JSONObject) geometry), clazz);
                this.geometry = geo;
            }
        } else if (geometry instanceof Geometry) {
            this.geometry = ((Geometry) geometry);
        } else if (geometry instanceof String) {
            this.geometry = ((Geometry) GeoJSONFactory.create(((String) geometry)));
        }
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(final int relation) {
        this.relation = relation;
    }
}
