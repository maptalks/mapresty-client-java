package org.maptalks.javasdk;

import org.maptalks.geojson.CRS;

/**
 * 查询条件类
 *
 * 对于JSON反序列化,
 * JSON字符串无法直接用fastjson, jackson等JSON库反序列化为QueryFilter对象
 * 请调用QueryFilter.create方法来进行JSON反序列化
 */
public class QueryFilter {
    /**
     * 全部自定义属性,即查询结果返回所有的自定义属性 例子:  queryFilter.setResultFields(QueryFilter.ALL_FIELDS);
     */
    public final static String[] ALL_FIELDS = new String[] { "*" };

    /**
     * 查询条件
     */
    private String condition;

    /**
     * 空间过滤filter
     */
    private SpatialFilter spatialFilter;

    /**
     * 结果坐标系
     */
    private CRS crs;

    /**
     * 要返回的自定义属性
     */
    private String[] resultFields;

    private boolean returnGeometry = true;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public SpatialFilter getSpatialFilter() {
        return spatialFilter;
    }

    public void setSpatialFilter(SpatialFilter spatialFilter) {
        this.spatialFilter = spatialFilter;
    }

    public String[] getResultFields() {
        return resultFields;
    }

    public void setResultFields(String[] resultFields) {
        this.resultFields = resultFields;
    }

    public CRS getResultCRS() {
        return crs;
    }

    public void setResultCRS(CRS crs) {
        this.crs = crs;
    }

    /**
     * 是否返回Geometry,默认为true
     */
    public boolean isReturnGeometry() {
        return returnGeometry;
    }

    public void setReturnGeometry(boolean returnGeometry) {
        this.returnGeometry = returnGeometry;
    }

}
