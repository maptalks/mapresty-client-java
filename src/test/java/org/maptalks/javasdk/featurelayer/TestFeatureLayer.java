package org.maptalks.javasdk.featurelayer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.gis.core.geojson.Feature;
import org.maptalks.gis.core.geojson.Point;
import org.maptalks.javasdk.FeatureLayer;
import org.maptalks.javasdk.MapDatabase;
import org.maptalks.javasdk.Settings;
import org.maptalks.javasdk.db.CoordinateType;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.featurelayer.common.TestEnvironment;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by fuzhen on 2015/9/19.
 */
public class TestFeatureLayer {
    private FeatureLayer featureLayer;
    private MapDatabase mapService;
    private CoordinateType coordinateType;

    protected final String TEST_LAYER_IDENTIFIER = "JUNIT"
            + System.currentTimeMillis();

    protected MapDatabase getMapDatabase() {
        MapDatabase db = new MapDatabase(Settings.TEST_HOST, Settings.TEST_PORT, Settings.TEST_DB);
        return db;
    }
    @Before
    public void prepare() throws Exception {
        mapService = this.getMapDatabase();

        coordinateType = CoordinateType.DEFAULT;
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
        featureLayer.add(feature);
        Feature result = featureLayer.getFirst("test1='test1' and test2=2");
        Assert.assertEquals(feature, result);

    }
}
