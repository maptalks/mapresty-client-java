package org.maptalks.javasdk;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.maptalks.gis.core.geojson.CRS;
import org.maptalks.gis.core.geojson.Polygon;
import org.maptalks.gis.core.geojson.common.CoordinateType;
import org.maptalks.javasdk.utils.JsonUtils;

/**
 * Created by fuzhen on 2015/9/18.
 */
public class TestFilterJson {
    @Test
    public void testSpatialFilterJson() {
        String sfStr = "{\"geometry\":{\"coordinates\":[[[67.49999999011854,11.178401880321333],"
                + "[78.74999998966939,11.178401880321333],[78.74999998966939,7.186526913860446E-9],"
                + "[67.49999999011854,7.186526913860446E-9],[67.49999999011854,11.178401880321333]]],"
                + "\"type\":\"Polygon\"},\"relation\":0}";
        SpatialFilter filter = SpatialFilter.create(sfStr);
        Assert.assertTrue(filter.getGeometry() instanceof Polygon);
        String json = JsonUtils.toJsonString(filter);
        Assert.assertEquals(sfStr, json);
    }

    @Test
    public void testQueryFilterJson() {
        String sfStr = "{\"geometry\":{\"coordinates\":[[[67.49999999011854,11.178401880321333],"
                + "[78.74999998966939,11.178401880321333],[78.74999998966939,7.186526913860446E-9],"
                + "[67.49999999011854,7.186526913860446E-9],[67.49999999011854,11.178401880321333]]],"
                + "\"type\":\"Polygon\"},\"relation\":0}";
        SpatialFilter filter = SpatialFilter.create(sfStr);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setCondition("foo = 1");
        queryFilter.setResultFields(new String[]{"foo1", "foo2"});
        queryFilter.setCrs(CRS.DEFAULT);
        queryFilter.setSpatialFilter(filter);

        String qfJson = JsonUtils.toJsonString(queryFilter);
//        JSONObject jsonObj = JSON.parseObject(qfJson);
//        jsonObj.put("coordinateType","wrongType");
//        qfJson = JsonUtils.toJsonString(jsonObj);

        QueryFilter parsed = QueryFilter.create(qfJson);
        Assert.assertEquals(parsed.getCrs(),CRS.DEFAULT);
        String parsedJson = JSON.toJSONString(parsed);

        Assert.assertEquals(qfJson, parsedJson);
    }

}
