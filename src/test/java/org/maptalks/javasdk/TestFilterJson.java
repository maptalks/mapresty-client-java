package org.maptalks.javasdk;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.maptalks.gis.core.geojson.CRS;
import org.maptalks.gis.core.geojson.Polygon;
import org.maptalks.javasdk.utils.JsonUtils;

/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestFilterJson {
    @Test
    public void testSpatialFilterJson() {
        String sfStr = "{\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[67.49999999011854,11.178401880321333],"
                + "[78.74999998966939,11.178401880321333],[78.74999998966939,7.186526913860446E-9],"
                + "[67.49999999011854,7.186526913860446E-9],[67.49999999011854,11.178401880321333]]]"
                + "},\"relation\":0}";
        SpatialFilter filter = JSON.parseObject(sfStr,SpatialFilter.class);
        Assert.assertTrue(filter.getGeometry() instanceof Polygon);
        String json = JsonUtils.toJsonString(filter);
        Assert.assertEquals(sfStr, json);
    }

    @Test
    public void testQueryFilterJson() {
        String sfStr = "{\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[67.49999999011854,11.178401880321333],"
                + "[78.74999998966939,11.178401880321333],[78.74999998966939,7.186526913860446E-9],"
                + "[67.49999999011854,7.186526913860446E-9],[67.49999999011854,11.178401880321333]]]"
                + "},\"relation\":0}";
//        SpatialFilter filter = SpatialFilter.create(sfStr);
        SpatialFilter filter = JSON.parseObject(sfStr,SpatialFilter.class);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setCondition("foo = 1");
        queryFilter.setResultFields(new String[]{"foo1", "foo2"});
        queryFilter.setResultCRS(CRS.DEFAULT);
        queryFilter.setSpatialFilter(filter);

        String qfJson = JsonUtils.toJsonString(queryFilter);
        System.out.println(qfJson);
        QueryFilter parsed = JSON.parseObject(qfJson, QueryFilter.class);
        Assert.assertEquals(parsed.getResultCRS(),CRS.DEFAULT);
        String parsedJson = JSON.toJSONString(parsed);

        Assert.assertEquals(qfJson, parsedJson);
    }

}
