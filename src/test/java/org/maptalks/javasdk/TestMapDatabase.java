package org.maptalks.javasdk;

import org.junit.Assert;
import org.junit.Test;
import org.maptalks.gis.core.geojson.CRS;
import org.maptalks.gis.core.geojson.Feature;
import org.maptalks.gis.core.geojson.FeatureCollection;
import org.maptalks.gis.core.geojson.Point;
import org.maptalks.javasdk.db.DBInfo;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.exceptions.InvalidLayerException;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.featurelayer.common.TestEnvironment;

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
        Assert.assertTrue(dbInfo.getCRS().equals(CRS.DEFAULT));
        Assert.assertEquals(Settings.TEST_DB, dbInfo.getName());
    }

//    @Test
    //install map service on database
    public void testInstall() throws IOException, RestException {
        MapDatabase db = new MapDatabase("localhost",8090,"default");
        db.install(null);
    }

    @Test
    public void testQueryLayers() throws IOException, RestException, InvalidLayerException {
        MapDatabase db = this.getMapDatabase();
        String[] layerIds = new String[]{TEST_LAYER_IDENTIFIER + "_1", TEST_LAYER_IDENTIFIER + "_2"};
        for (int i = 0; i < layerIds.length; i++) {
            Layer layer = new Layer();
            layer.setId(layerIds[i]);
            db.addLayer(layer);
        }
        try {

            Point point = TestEnvironment.genPoint();
            Feature feature = new Feature(point);
            for (int i = 0; i < layerIds.length; i++) {
                new FeatureLayer(layerIds[i], db).add(feature, null);
            }

            FeatureCollection[] collections = db.query(null, 0, 10, layerIds);
            Assert.assertTrue(collections.length == 2);
                Assert.assertEquals(collections[0].getFeatures()[0], feature);
            Assert.assertEquals(collections[1].getFeatures()[0], feature);

        } finally {
            for (int i = 0; i < layerIds.length; i++) {
                db.removeLayer(layerIds[i]);
            }
        }

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
