package org.maptalks.javasdk.featurelayer.common;

import org.maptalks.geojson.*;
import org.maptalks.javasdk.db.Layer;

/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestEnvironment {
    public static String LAYER_TYPE = Layer.TYPE_DB_TABLE;


    public static Geometry[] genAllGeometries() {
        Geometry[] geometries = new Geometry[]{
                genPoint(),
                genPolyline(),
                genPolygon(),
                genMultipoint(),
                genMultiPolyline(),
                genMultiPolygon()
        };
        return geometries;
    }

    //-----生成各种图形的方法-----------------

    /**
     * 生成点数据
     * @return
     */
    public static Point genPoint() {
        final Point geometry = new Point(new double[]{109,32});
        return geometry;
    }

    /**
     * 生成多折线
     * @return
     */
    public static LineString genPolyline() {
        double[][] path = new double[][]{new double[]{109,32},new double[]{109,31}, new double[]{109,30}};
        return new LineString(path);
    }

    /**
     * 生成多边形
     * @return
     */
    public static Geometry genPolygon() {
        double[][][] rings = new double[][][]{
                new double[][]{new double[]{109,32},new double[]{109,31},
                        new double[]{109,30},new double[]{108,30},new double[]{109,32}},
                new double[][]{new double[]{108.5,30.5},new double[]{108.5,30.3},
                        new double[]{108.2,30.3},new double[]{108.5,30.5}}
        };
        return new Polygon(rings);
    }

    /**
     * 生成MultiPoint数据
     * @return
     */
    public static Geometry genMultipoint() {
        double[][] multiPoint = new double[][]{new double[]{109,32},new double[]{109,31}, new double[]{109,30}};
        return new MultiPoint(multiPoint);
    }

    /**
     * 生成MultiPolyline数据
     * @return
     */
    public static Geometry genMultiPolyline() {
        double[][][] multiPath = new double[][][] {
                new double[][]{new double[]{109,32},new double[]{109,31},
                        new double[]{109,30},new double[]{108,30},new double[]{109,32}},
                new double[][]{new double[]{119,32},new double[]{119,31},
                        new double[]{119,30},new double[]{118,30},new double[]{119,32}}
        };
        return new MultiLineString(multiPath);
    }

    /**
     * 生成MultiPolygon数据
     * @return
     */
    public static Geometry genMultiPolygon() {
        double[][][][] multiRings = new double[][][][] {
                //polygon1
                new double[][][] {
                        new double[][]{new double[]{109,32},new double[]{109,31},
                                new double[]{109,30},new double[]{108,30},new double[]{109,32}}
                },
                //polygon2
                new double[][][] {
                        new double[][]{new double[]{119,32},new double[]{119,31},
                                new double[]{119,30},new double[]{118,30},new double[]{119,32}}
                }
        };
        return new MultiPolygon(multiRings);
    }

    /**
     * 生成GeometryCollection数据
     * @return
     */
    public static Geometry genGeometryCollection() {

        Geometry[] geometries = new Geometry[]{
                genPoint(),
                genPolyline(),
                genPolygon(),
                genMultipoint(),
                genMultiPolyline(),
                genMultiPolygon()
        };
        return new GeometryCollection(geometries);
    }
}
