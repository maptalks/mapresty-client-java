package org.maptalks.javasdk.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.maptalks.geojson.Feature;
import org.maptalks.geojson.GeoJSONFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BulkData extends HashMap<String, List<DataOp>> {

    public static BulkData create(byte[] bytes) throws IOException {
        String json = new String(bytes, "UTF-8");
        return create(json);
    }

    public static BulkData create(String json) {
        JSONObject root = JSON.parseObject(json);
        BulkData data = new BulkData();

        for (Entry<String, Object> entry : root.entrySet()) {
            List<DataOp> list = new ArrayList<DataOp>();
            JSONArray array = (JSONArray) entry.getValue();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String op = obj.getString("op");
                if (DataOp.ADD.equals(op)) {
                    list.add(createOpAdd(obj));
                } else if (DataOp.REMOVE.equals(op)) {
                    list.add(createOpRemove(obj));
                } else if (DataOp.UPDATE.equals(op)) {
                    list.add(createOpUpdate(obj));
                }
            }
            data.put(entry.getKey(), list);
        }

        return data;
    }

    private static DataOpAdd createOpAdd(JSONObject obj) {
        List<Feature> features = new ArrayList<Feature>();
        JSONArray array = obj.getJSONArray("features");
        for (int i = 0; i < array.size(); i++) {
            JSONObject node = array.getJSONObject(i);
            Feature feature = (Feature) GeoJSONFactory.create(node);
            features.add(feature);
        }
        return new DataOpAdd(features);
    }

    private static DataOpRemove createOpRemove(JSONObject obj) {
        String condition = obj.getString("condition");
        return new DataOpRemove(condition);
    }

    private static DataOpUpdate createOpUpdate(JSONObject obj) {
        List<DataOpUpdate.Item> items = new ArrayList<DataOpUpdate.Item>();
        JSONArray array = obj.getJSONArray("items");
        for (int i = 0; i < array.size(); i++) {
            JSONObject node = array.getJSONObject(i);
            DataOpUpdate.Item item = new DataOpUpdate.Item();
            String condition = node.getString("condition");
            item.setCondition(condition);
            Feature feature = (Feature) GeoJSONFactory.create(node.getJSONObject("feature"));
            item.setFeature(feature);
            items.add(item);
        }
        return new DataOpUpdate(items);
    }

}
