package org.maptalks.javasdk.featurelayer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.geojson.CRS;
import org.maptalks.geojson.Feature;
import org.maptalks.geojson.Point;
import org.maptalks.geojson.Polygon;
import org.maptalks.javasdk.*;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;
import org.maptalks.javasdk.featurelayer.common.TestEnvironment;
import org.maptalks.proj4.Proj4;

/**
 * Created by dusci on 2016/2/4.
 */
public class TestCRS {
    private FeatureLayer featureLayer;
    private MapDatabase mapService;
    private CRS crs;

    protected final String TEST_LAYER_IDENTIFIER = "JUNIT"
            + System.currentTimeMillis();

    protected MapDatabase getMapDatabase() {
        MapDatabase db = new MapDatabase(Settings.TEST_HOST, Settings.TEST_PORT, Settings.TEST_DB);
        return db;
    }
    @Before
    public void prepare() throws Exception {
        mapService = this.getMapDatabase();

        crs = mapService.getDatabaseInfo().getCRS();
        final Layer testLayer = new Layer();
        testLayer.setId(TEST_LAYER_IDENTIFIER);
        testLayer.setType(TestEnvironment.LAYER_TYPE);
        mapService.addLayer(testLayer);
        LayerField field1 = new LayerField("test1", "VARCHAR(20)");
        LayerField field2 = new LayerField("test2", "INT");
        //TODO 多测试几种数据类型

        featureLayer = new FeatureLayer(TEST_LAYER_IDENTIFIER, mapService);

        featureLayer.addLayerField(field1);
        featureLayer.addLayerField(field2);
    }

    @After
    public void after() throws Exception {
        mapService.removeLayer(TEST_LAYER_IDENTIFIER);
        final Layer layer = mapService.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNull(layer);
    }

    @Test
    public void testAdd() throws Exception {
        Proj4 proj4 = new Proj4(CRS.getProj4(CRS.BD09LL), CRS.getProj4(this.crs));
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), CRS.BD09LL);
        Feature[] collection = featureLayer.query(null,0,1);
        Feature actual = collection[0];
        Assert.assertArrayEquals(proj4.forward(point.getCoordinates()) , ((Point) actual.getGeometry()).getCoordinates(), 0);
    }

    @Test
    public void testAddWithDbCRS() throws Exception {
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), mapService.getDatabaseInfo().getCRS() );
        Feature[] collection = featureLayer.query(null,0,1);
        Feature actual = collection[0];
        Assert.assertArrayEquals(point.getCoordinates() , ((Point) actual.getGeometry()).getCoordinates(), 0);
    }

    @Test
    public void testQuery() throws Exception {
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), null);
        Feature[] collection = featureLayer.query(null,0,1);
        Feature actual = collection[0];
        Assert.assertArrayEquals(point.getCoordinates() , ((Point) actual.getGeometry()).getCoordinates(), 0);
    }

    @Test
    public void testQueryResultCRS() throws Exception {
        Proj4 proj4 = new Proj4(CRS.getProj4(CRS.BD09LL), CRS.getProj4(this.crs));
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), null);
        QueryFilter filter = new QueryFilter();
        filter.setResultCRS(CRS.BD09LL);
        Feature[] collection = featureLayer.query(filter,0,1);
        Feature actual = collection[0];
        Assert.assertArrayEquals(proj4.inverse(point.getCoordinates()) , ((Point) actual.getGeometry()).getCoordinates(), 0);
    }

    @Test
    public void testSpatialFilterCRS() throws Exception {
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), null);

        double delta = 1E-6;
        double[] center = point.getCoordinates();
        double[][] shell = new double[][]{
            new double[]{center[0] - delta, center[1] - delta},
            new double[]{center[0] - delta, center[1] + delta},
            new double[]{center[0] + delta, center[1] + delta},
            new double[]{center[0] + delta, center[1] - delta},
            new double[]{center[0] - delta, center[1] - delta}
        };

        QueryFilter filter = new QueryFilter();
        SpatialFilter sf = new SpatialFilter(new Polygon(new double[][][]{shell}), SpatialFilter.RELATION_INTERSECT);
        filter.setSpatialFilter(sf);
        Feature[] collection = featureLayer.query(filter,0,1);
        Assert.assertTrue(collection.length == 1);

        //spatial filter's CRS will be set to the same as QueryFilter's resultCRS
        filter.setResultCRS(CRS.BD09LL);
        collection = featureLayer.query(filter,0,1);
        Assert.assertTrue(collection.length == 0);

        filter.setResultCRS(null);
        sf.setCRS(CRS.BD09LL);
        filter.setSpatialFilter(sf);
        collection = featureLayer.query(filter,0,1);
        Assert.assertTrue(collection.length == 0);
    }

    @Test
    public void testBatchAdd() throws Exception {
        Proj4 proj4 = new Proj4(CRS.getProj4(CRS.BD09LL), CRS.getProj4(this.crs));
        Feature[] features = new Feature[10];
        Point point = TestEnvironment.genPoint();
        for (int i = 0; i < 10; i++) {
            features[i] = new Feature(point);
        }
        featureLayer.add(features, CRS.BD09LL);
        Feature[] actual = featureLayer.query(null,0,1000);
        Assert.assertEquals(actual.length, features.length);
        for (int i = 0; i < features.length; i++) {
            Point p = ((Point) features[i].getGeometry());
            Assert.assertArrayEquals(proj4.forward(p.getCoordinates()) , ((Point) actual[i].getGeometry()).getCoordinates(), 0);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        Proj4 proj4 = new Proj4(CRS.getProj4(CRS.BD09LL), CRS.getProj4(this.crs));
        Point point = TestEnvironment.genPoint();
        featureLayer.add(new Feature(point), null);
        Feature[] collection = featureLayer.query(null,0,1);
        Feature actual = collection[0];
        Assert.assertArrayEquals(point.getCoordinates() , ((Point) actual.getGeometry()).getCoordinates(), 0);

        featureLayer.update("1=1",new Feature(point), CRS.BD09LL );
        collection = featureLayer.query(null,0,1);
        actual = collection[0];
        Assert.assertArrayEquals(proj4.forward(point.getCoordinates()) , ((Point) actual.getGeometry()).getCoordinates(), 0);
    }
}
