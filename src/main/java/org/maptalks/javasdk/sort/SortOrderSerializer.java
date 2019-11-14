package org.maptalks.javasdk.sort;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class SortOrderSerializer implements ObjectSerializer {
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SortOrder order = (SortOrder) object;
        serializer.write(order.getName());
    }
}
