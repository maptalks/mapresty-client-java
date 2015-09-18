package org.maptalks.javasdk.featurelayer;

import org.maptalks.javasdk.MapDatabase;
import org.maptalks.javasdk.Settings;
import org.maptalks.javasdk.featurelayer.common.TestCRUD;

/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestSQL extends TestCRUD{
    @Override
    protected MapDatabase getMapDatabase() {
        MapDatabase db = new MapDatabase(Settings.TEST_HOST, Settings.TEST_PORT, Settings.TEST_DB);
        return db;
    }

    @Override
    public String eq(String field, String value) {
        return field+" = '"+value+"'";
    }

    @Override
    public String startsWith(String field, String prefix) {
        return field+" like '"+prefix+"%'";
    }
}
