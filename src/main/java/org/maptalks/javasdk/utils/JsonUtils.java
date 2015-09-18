package org.maptalks.javasdk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by fuzhen on 2015/9/18.
 */
public class JsonUtils {
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        final SerializerFeature[] features = { SerializerFeature.DisableCircularReferenceDetect };
        return JSON.toJSONString(obj, features);
    }
}
