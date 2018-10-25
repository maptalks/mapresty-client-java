package org.maptalks.geojson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GeoJSONFactory {
    private static Map<String, Class> geoJsonTypeMap = new HashMap<String, Class>();

    static {
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_POINT, Point.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_LINESTRING, LineString.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_POLYGON, Polygon.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_MULTIPOINT, MultiPoint.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_MULTILINESTRING, MultiLineString.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_MULTIPOLYGON, MultiPolygon.class);
        geoJsonTypeMap.put(GeoJSONTypes.TYPE_GEOMETRYCOLLECTION, GeometryCollection.class);
    }

    public static GeoJSON create(String json) {
        JSONObject node = JSON.parseObject(json);
        return create(node);
    }

    public static GeoJSON create(JSONObject node) {
        String type = node.getString("type");
        if ("FeatureCollection".equals(type)) {
            return readFeatureCollection(node);
        } else if ("Feature".equals(type)) {
            return readFeature(node);
        } else {
            return readGeometry(node, type);
        }
    }

    private static FeatureCollection readFeatureCollection(JSONObject node) {

        JSONArray array = node.getJSONArray("features");
        if (array == null) {
            // return a empty FeatureCollection
            FeatureCollection result = new FeatureCollection();
            result.setFeatures(new Feature[0]);
            return result;
        }
        int size = array.size();
        Feature[] features = new Feature[size];
        for (int i = 0; i < size; i++) {
            features[i] = readFeature(array.getJSONObject(i));
        }
        FeatureCollection result = new FeatureCollection();
        result.setFeatures(features);
        return result;
    }

    private static Feature readFeature(JSONObject node) {
        JSONObject geoJ = node.getJSONObject("geometry");
        Geometry geo = null;
        if (geoJ != null) {
            geo = readGeometry(geoJ, geoJ.getString("type"));
        }
        node.remove("geometry");
        Feature result = JSON.toJavaObject(node, Feature.class);
        result.setGeometry(geo);
        return result;
    }

    private static Geometry readGeometry(JSONObject node, String type) {
        if (GeoJSONTypes.TYPE_GEOMETRYCOLLECTION.equals(type)) {
            GeometryCollection result = new GeometryCollection(new Geometry[0]);
            JSONArray array = node.getJSONArray("geometries");
            if (array != null) {
                int size = array.size();
                Geometry[] geometries = new Geometry[size];
                for (int i = 0; i < size; i++) {
                    JSONObject jgeo = array.getJSONObject(i);
                    geometries[i] = readGeometry(jgeo, jgeo.getString("type"));
                }
                result.setGeometries(geometries);
            } else {
                // return a empty GeometryCollection
                result.setGeometries(new Geometry[0]);
            }
            return result;
        }
        Class<?> clazz = getGeoJsonType(type);
        Object obj = JSON.toJavaObject(node, clazz);
        return ((Geometry) obj);
    }

    private static Class<?> getGeoJsonType(String type) {
        return geoJsonTypeMap.get(type);
    }
}
