package org.maptalks.javasdk.topo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maptalks.geojson.*;
import org.maptalks.javasdk.SpatialFilter;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.featurelayer.common.TestEnvironment;

import java.io.IOException;

import static org.maptalks.javasdk.Settings.TEST_PORT;

/**
 * Created by fuzhen on 2015/10/29.
 */
public class TestTopoQuery {
    private Geometry[] baseGeometries;
    private TopoQuery topoQuery;

    @Before
    public void prepare() {
        topoQuery = new TopoQuery("localhost",TEST_PORT);
        baseGeometries = TestEnvironment.genAllGeometries();
    }

    @Test
    public void testContexHull() throws IOException, RestException {
        Geometry[] results = topoQuery.convexHull(baseGeometries);
        Assert.assertTrue(results.length == baseGeometries.length);
        for (int i = 0; i < results.length; i++) {
            Assert.assertNotNull(results[i]);
            Assert.assertTrue(results.toString().length()>0);
        }
    }

    @Test
    public void testDifference() throws IOException, RestException {
        for (int i = 0; i < baseGeometries.length; i++) {
            Geometry[] results = topoQuery.difference(baseGeometries[i],baseGeometries);
            /*Assert.assertTrue(results.length == baseGeometries.length);
            for (int j = 0; j < results.length; j++) {
                Assert.assertNotNull(results[j]);
                Assert.assertTrue(results[j].toString().length()>0);
            }*/
        }
    }

    @Test
    public void testSymDifference() throws IOException, RestException {
        for (int i = 0; i < baseGeometries.length; i++) {
            Geometry[] results = topoQuery.symDifference(baseGeometries[i], baseGeometries);
            /*Assert.assertTrue(results.length == baseGeometries.length);
            for (int j = 0; j < results.length; j++) {
                Assert.assertNotNull(results[j]);
                Assert.assertTrue(results[j].toString().length()>0);
            }*/
        }
    }

    @Test
    public void testUnion() throws IOException, RestException {
        for (int i = 0; i < baseGeometries.length; i++) {
            Geometry[] results = topoQuery.union(baseGeometries[i], baseGeometries);
            Assert.assertTrue(results.length == baseGeometries.length);
            for (int j = 0; j < results.length; j++) {
                Assert.assertNotNull(results[j]);
                Assert.assertTrue(results[j].toString().length()>0);
            }
        }
    }

    @Test
    public void testBuffer() throws IOException, RestException {
        Geometry[] results = topoQuery.buffer(baseGeometries, 100);
        Assert.assertTrue(results.length == baseGeometries.length);
        for (int i = 0; i < results.length; i++) {
            Assert.assertNotNull(results[i]);
            Assert.assertTrue(results.toString().length()>0);
        }
    }
    /**
     * 测试相交
     */
    @Test
    public void testIntersection() throws IOException, RestException {
        for (int i = 0; i < baseGeometries.length; i++) {
            Geometry[] results = topoQuery.intersection(baseGeometries[i], baseGeometries);
            Assert.assertTrue(results.length == baseGeometries.length);
            for (int j = 0; j < results.length; j++) {
                Assert.assertNotNull(results[j]);
                Assert.assertTrue(results[j].toString().length()>0);
            }
        }

    }

    @Test
    public void testRelate() throws IOException, RestException {
        for (int i = 0; i < baseGeometries.length; i++) {
            Integer[] results = topoQuery.relate(baseGeometries[i], baseGeometries, SpatialFilter.RELATION_INTERSECT);
            Assert.assertTrue(results.length == baseGeometries.length);
            for (int j = 0; j < results.length; j++) {

                Assert.assertTrue(results[j] == 1);
            }
        }
    }
}
