package org.maptalks.javasdk.featurelayer;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.geojson.CRS;
import org.maptalks.geojson.Feature;
import org.maptalks.geojson.Point;
import org.maptalks.geojson.json.GeoJSONFactory;
import org.maptalks.javasdk.FeatureLayer;
import org.maptalks.javasdk.MapDatabase;
import org.maptalks.javasdk.Settings;
import org.maptalks.javasdk.ErrorCodes;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.featurelayer.common.TestEnvironment;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.fail;

/**
 * Created by fuzhen on 2015/9/19.
 */
public class TestFeatureLayer {
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
    public void testGetFirst() throws IOException, RestException {
        Point point = TestEnvironment.genPoint();
        Feature feature = new Feature(point);
        feature.setProperties(new HashMap<String, Object>(){
            {
                put("test1","test1");
                put("test2",2);
            }
        });
        featureLayer.add(feature, null);
        Feature result = featureLayer.getFirst(null);
        Assert.assertEquals(feature, result);

    }

    @Test
    public void testAddList() throws IOException, RestException {
        Point point = TestEnvironment.genPoint();
        Feature f1 = new Feature(point);
        f1.setProperties(new HashMap<String, Object>() {
            {
                put("test1", "test1");
                put("test2", 2);
            }
        });
        Feature f2 = (Feature) GeoJSONFactory.create(JSON.toJSONString(f1));
        f2.getProperties().put("test2",3);
        featureLayer.add(Arrays.asList(new Feature[]{f1, f2}), null);
        Feature[] result = featureLayer.query(null, 0, 10);
        Assert.assertTrue(result.length == 2);
        Assert.assertEquals(result[0], f1);
        Assert.assertEquals(result[1], f2);

    }

    @Test
    public void testRemoveAll() throws IOException, RestException {
        Point point = TestEnvironment.genPoint();
        Feature f1 = new Feature(point);
        f1.setProperties(new HashMap<String, Object>() {
            {
                put("test1", "test1");
                put("test2", 2);
            }
        });
        Feature f2 = (Feature) GeoJSONFactory.create(JSON.toJSONString(f1));
        f2.getProperties().put("test2",3);
        featureLayer.add(Arrays.asList(new Feature[]{f1, f2}), null);
        Feature[] result = featureLayer.query(null, 0, 10);
        Assert.assertTrue(result.length == 2);
        featureLayer.removeAll();
        Assert.assertTrue(0 == featureLayer.count(null));
    }

    @Test
    public void testGeometryCannotBeNull() throws IOException, RestException {
        Feature f1 = new Feature(null);
        f1.setProperties(new HashMap<String, Object>() {
            {
                put("test1", "test1");
                put("test2", 2);
            }
        });
        try {
            featureLayer.add(Arrays.asList(new Feature[]{f1}), null);
           fail();
        } catch (RestException e) {
            Assert.assertEquals(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT, e.getErrCode());
        }
    }

    @Test
    public void testUpdateProperties() throws IOException, RestException {
        Point point = TestEnvironment.genPoint();
        Feature f1 = new Feature(point);
        f1.setProperties(new HashMap<String, Object>() {
            {
                put("test1", "test1");
                put("test2", 2);
            }
        });
        Feature f2 = (Feature) GeoJSONFactory.create(JSON.toJSONString(f1));
        f2.getProperties().put("test2", 3);
        featureLayer.add(Arrays.asList(new Feature[]{f1, f2}), null);

        featureLayer.updateProperties("test1 = 'test1'",new HashMap<String, Object>() {
            {
                put("test1", "test2");
                put("test2", 4);
            }
        });

        Feature[] result = featureLayer.query(null, 0, 10);
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(result[i].getProperties().get("test1"), "test2");
            Assert.assertEquals(result[i].getProperties().get("test2"), 4);
        }
    }


}
