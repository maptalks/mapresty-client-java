package org.maptalks.javasdk;

import org.junit.Assert;
import org.junit.Test;
import org.maptalks.javasdk.db.CoordinateType;
import org.maptalks.javasdk.db.DBInfo;
import org.maptalks.javasdk.exceptions.RestException;

import java.io.IOException;
/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestMapDatabase {
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
}
