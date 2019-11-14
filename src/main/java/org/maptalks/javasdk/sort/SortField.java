package org.maptalks.javasdk.sort;

import com.alibaba.fastjson.annotation.JSONField;

public class SortField {
    @JSONField(ordinal = 1)
    private String field;
    @JSONField(ordinal = 2)
    private SortOrder order;

    public SortField() {}

    public SortField(String field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }
}
