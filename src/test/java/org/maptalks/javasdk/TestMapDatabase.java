package org.maptalks.javasdk;

import org.junit.Assert;
import org.junit.Test;
import org.maptalks.javasdk.db.*;
import org.maptalks.javasdk.exceptions.RestException;

import java.io.IOException;
import java.util.List;


/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestMapDatabase {
    protected final String TEST_LAYER_IDENTIFIER = "JUNIT"
            + System.currentTimeMillis();
    protected MapDatabase getMapDatabase() {
        MapDatabase db = new MapDatabase(Settings.TEST_HOST, Settings.TEST_PORT, Settings.TEST_DB);
        return db;
    }

    @Test
    public void testGetDatabaseInfo() throws IOException, RestException {
        MapDatabase db = this.getMapDatabase();
        DBInfo dbInfo = db.getDatabaseInfo();
        Assert.assertNotNull(dbInfo);
        Assert.assertTrue(dbInfo.getCoordinateType().equals(CoordinateType.gcj02));
        Assert.assertEquals(Settings.TEST_DB, dbInfo.getName());
    }

 //   @Test
    //install map service on database
    public void testInstall() throws IOException, RestException {
        MapDatabase db = new MapDatabase("SGH1PDMIS01",8090,"y-sde");
        db.install(null);
    }

    @Test
    public void testGetLayerInfo() throws IOException, RestException {
        MapDatabase db = this.getMapDatabase();
        Layer layer = new Layer();
        layer.setId(TEST_LAYER_IDENTIFIER);
        db.addLayer(layer);
        boolean hit = false;
        List<Layer> layers = db.getAllLayers();
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getId().equals(TEST_LAYER_IDENTIFIER)) {
                hit = true;
                 break;
            }
        }
        Assert.assertTrue(hit);
        Assert.assertNotNull(layers);
        Assert.assertTrue(layers.size() > 0);
        db.removeLayer(TEST_LAYER_IDENTIFIER);
    }

    /**
     * 测试图层的增删改查
     * @throws Exception
     */
    @Test
    public void testCRUD() throws Exception {
        MapDatabase db = this.getMapDatabase();
        final Layer testLayer = new Layer();
        testLayer.setId(TEST_LAYER_IDENTIFIER);
        testLayer.setName("测试");
        testLayer.setSource("source");
        db.addLayer(testLayer);

        Layer layer = db.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNotNull(layer);
        Assert.assertEquals(TEST_LAYER_IDENTIFIER, layer.getId());
        Assert.assertEquals("测试", layer.getName());
        Assert.assertEquals("source",layer.getSource());

        db.updateLayer(layer.getId(),layer);
        layer = db.getLayer(TEST_LAYER_IDENTIFIER);

        layer.setName("测试2");
        db.updateLayer(layer.getId(), layer);
        layer = db.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertEquals("测试2", layer.getName());

        db.removeLayer(TEST_LAYER_IDENTIFIER);
        layer = db.getLayer(TEST_LAYER_IDENTIFIER);
        Assert.assertNull(layer);
    }

}
