package org.maptalks.javasdk.sort;

import com.alibaba.fastjson.annotation.JSONType;

@JSONType(deserializer = SortOrderDeserializer.class, serializer = SortOrderSerializer.class, serializeEnumAsJavaBean = true)
public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    SortOrder(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
