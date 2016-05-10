package org.maptalks.javasdk.featurelayer.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.geojson.Feature;
import org.maptalks.geojson.FeatureCollection;
import org.maptalks.geojson.Geometry;
import org.maptalks.geojson.json.GeoJSONFactory;
import org.maptalks.javasdk.FeatureLayer;
import org.maptalks.javasdk.MapDatabase;
import org.maptalks.javasdk.QueryFilter;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuzhen on 2015/9/18.
 */
public abstract class TestCRUD extends TestCommon {
    private FeatureLayer featureLayer;
    private MapDatabase mapService;

    protected final String TEST_LAYER_IDENTIFIER = "JUNIT"
            + System.currentTimeMillis();

    @Before
    public void prepare() throws Exception {
        mapService = this.getMapDatabase();

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

    protected abstract MapDatabase getMapDatabase();


    @After
    public void after() throws Exception {
        mapService.removeLayer(TEST_LAYER_IDENTIFIER);
        final Layer layer = mapService.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNull(layer);
    }

    //测试点的增删改查
    @Test
    public void testPointCrud() throws Exception {
        Geometry geometry = TestEnvironment.genPoint();
        testCRUD(geometry);
    }

    @Test
    public void testPolylineCRUD() throws Exception {
        Geometry geometry = TestEnvironment.genPolyline();
        testCRUD(geometry);
    }

    @Test
    public void testPolygonCRUD() throws Exception {
        final Geometry geometry = TestEnvironment.genPolygon();
        testCRUD(geometry);
    }


    @Test
    public void testMultipointCRUD() throws Exception {
        Geometry geometry = TestEnvironment.genMultipoint();
        testCRUD(geometry);
    }

    @Test
    public void testMultiPolylineCRUD() throws Exception {
        Geometry geometry = TestEnvironment.genMultiPolyline();
        testCRUD(geometry);
    }

    @Test
    public void testMultiPolygonCRUD() throws Exception {
        Geometry geometry = TestEnvironment.genMultiPolygon();
        testCRUD(geometry);
    }

    @Test
    public void testGeometryCollection() throws Exception {
        Layer layer = mapService.getLayer(TEST_LAYER_IDENTIFIER);
        //TODO 目前只有引擎图层才支持GeometryCollection的存储
        if (layer.getType() == Layer.TYPE_DB_TABLE) {
            Geometry geometry = TestEnvironment.genGeometryCollection();
            testCRUD(geometry);
        }

    }




    public void testCRUD(Geometry geometry) throws Exception {
        final Layer layer = mapService.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNotNull(layer);

        Feature feature = new Feature(geometry);

        Map properties = new HashMap();
        properties.put("test1", "haha");
        properties.put("test2", 2);
        feature.setProperties(properties);
        featureLayer.add(feature, null);

        StringWriter writer = new StringWriter();

        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setCondition(eq("test1", "haha"));
        queryFilter.setResultFields(new String[]{"*"});

        String result = featureLayer.queryJson(queryFilter, 0, 10);
        Assert.assertNotNull(result);
        FeatureCollection[] collections = GeoJSONFactory.createFeatureCollectionArray(result);
        Assert.assertEquals(1, collections[0].getFeatures().length);
        Feature queryGeo = collections[0].getFeatures()[0];


        Assert.assertEquals("haha", queryGeo.getProperties().get("test1"));
        Assert.assertEquals(2, queryGeo.getProperties().get("test2"));
        properties.put("test1", "hehe");
        properties.put("test2", 0);
        feature.setProperties(properties);
        featureLayer.update(eq("test1", "haha"), feature, null);

        queryFilter.setCondition(eq("test1", "hehe"));
        result = featureLayer.queryJson(queryFilter, 0, 10);
        collections = GeoJSONFactory.createFeatureCollectionArray(result);
        queryGeo = collections[0].getFeatures()[0];
        Map retAttr = queryGeo.getProperties();
        Assert.assertEquals("hehe", retAttr.get("test1"));
        Assert.assertEquals(0, retAttr.get("test2"));
        featureLayer.remove(eq("test1", "hehe"));
        result = featureLayer.queryJson(queryFilter, 0, 10);
        Assert.assertEquals("[{\"features\":[],\"type\":\"FeatureCollection\",\"layer\":\""+TEST_LAYER_IDENTIFIER+"\"}]", result);
    }

    private boolean isSpatial(Layer layer) {
        return layer.getType().equalsIgnoreCase(Layer.TYPE_DB_SPATIAL_TABLE) || layer.getType().equalsIgnoreCase(Layer.TYPE_DB_SPATIAL_VIEW);
    }


}
