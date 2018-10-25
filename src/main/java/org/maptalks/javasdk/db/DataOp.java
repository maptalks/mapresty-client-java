package org.maptalks.javasdk.db;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class DataOp {
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String UPDATE = "update";

    @JSONField(ordinal = -1)
    private String op;

    public DataOp(String op) {
        setOp(op);
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
