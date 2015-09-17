package org.maptalks.javasdk;


import org.maptalks.gis.core.geojson.Geometry;

/**
 * 空间查询条件�?
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
    //中心点包含关系
    public final static int RELATION_CONTAINCENTER = 7;

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

    public void setGeometry(final Geometry geometry) {
        this.geometry = geometry;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(final int relation) {
        this.relation = relation;
    }
}
