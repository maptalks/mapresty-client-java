package org.maptalks.javasdk.featurelayer.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.gis.core.geojson.Feature;
import org.maptalks.gis.core.geojson.Geometry;
import org.maptalks.gis.core.geojson.common.CoordinateType;
import org.maptalks.javasdk.*;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;
import org.maptalks.javasdk.exceptions.InvalidLayerException;
import org.maptalks.javasdk.exceptions.RestException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by fuzhen on 2015/10/5.
 */
public abstract class TestFeatureQuery extends TestCommon {
    private FeatureLayer featureLayer;
    private MapDatabase mapService;
    private Feature[] features;

    protected final String TEST_LAYER_IDENTIFIER = "JUNIT"
            + System.currentTimeMillis();

    protected MapDatabase getMapDatabase() {
        MapDatabase db = new MapDatabase(Settings.TEST_HOST, Settings.TEST_PORT, Settings.TEST_DB);
        return db;
    }

    @Before
    public void prepare() throws IOException, RestException, InvalidLayerException {
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

        prepareData();
    }


    public void prepareData() throws IOException, RestException {
        Geometry[] geometries = TestEnvironment.genAllGeometries();
        features = new Feature[geometries.length];
        for (int i = 0; i < geometries.length; i++) {
            final int index = i;
            features[i] = new Feature(geometries[i]);
            features[i].setProperties(new HashMap<String, Object>() {
                {
                    put("test1","name"+index);
                    put("test2", index);
                }
            });
        }
        featureLayer.add(features);
    }

    @After
    public void after() throws Exception {
        mapService.removeLayer(TEST_LAYER_IDENTIFIER);
        final Layer layer = mapService.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNull(layer);
    }

    /**
     * 测试QueryFilter为null
     * @throws IOException
     * @throws RestException
     */
    @Test
    public void testNullFilter() throws IOException, RestException {
        Feature[] result = featureLayer.query(null, 0, 10);
        Assert.assertEquals(result.length, features.length);
        for (int i = 0; i < features.length; i++) {
            Assert.assertEquals(features[i], result[i]);
        }
    }

    /**
     * 测试condition是否生效
     * @throws IOException
     * @throws RestException
     */
    @Test
    public void testCondition() throws IOException, RestException {
        int index = 5;
        QueryFilter filter = new QueryFilter();
        filter.setCondition(eq("test2",index));
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertEquals(result.length, 1);
        Assert.assertEquals(features[index], result[0]);
    }

    /**
     * 测试condition无结果查询
     * @throws IOException
     * @throws RestException
     */
    @Test
    public void testConditionWithNoResult() throws IOException, RestException {
        int index = -1;
        QueryFilter filter = new QueryFilter();
        filter.setCondition(eq("test2",index));
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertTrue(result.length == 0);
    }

    /**
     * 测试利用resultFields返回指定的属性
     * @throws IOException
     * @throws RestException
     */
    @Test
    public void testResultFields() throws IOException, RestException {
        QueryFilter filter = new QueryFilter();
        filter.setResultFields(new String[]{"test1"});
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertEquals(result.length, features.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertNotNull(result[i].getProperties().get("test1"));
            Assert.assertNull(result[i].getProperties().get("test2"));
        }
    }

    @Test
    public void testEmptyFields() throws IOException, RestException {
        QueryFilter filter = new QueryFilter();
        //set empty fields
        filter.setResultFields(new String[]{});
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertEquals(result.length, features.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertNull(result[i].getProperties());
        }
    }

    @Test
    public void testNoReturnGeometry() throws IOException, RestException {
        QueryFilter filter = new QueryFilter();
        filter.setReturnGeometry(false);
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertEquals(result.length, features.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertNull(result[i].getGeometry());
        }
    }

    @Test
    public void testCoordinateType() throws IOException, RestException {
        QueryFilter filter = new QueryFilter();
        filter.setCrs(CoordinateType.bd09ll.toCRS());
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertEquals(result.length, features.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertNotEquals(features[i].getGeometry(), result[i].getGeometry());
        }
    }

    @Test
    public void testSpatialFilter() throws IOException, RestException {
        SpatialFilter spatialFilter = new SpatialFilter();
        spatialFilter.setRelation(SpatialFilter.RELATION_WITHIN);
        spatialFilter.setFilterGeometry(TestEnvironment.genCircle());
        QueryFilter filter = new QueryFilter();
        filter.setSpatialFilter(spatialFilter);
        Feature[] result = featureLayer.query(filter, 0, Integer.MAX_VALUE);
        Assert.assertTrue(result.length < features.length);

    }

}