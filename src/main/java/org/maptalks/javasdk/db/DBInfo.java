package org.maptalks.javasdk.db;

import com.alibaba.fastjson.annotation.JSONField;
import org.maptalks.geojson.CRS;

/**
 * Created by fuzhen on 2015/9/17.
 */
public class DBInfo {
    private String name;
    private CRS crs;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name="crs")
    public CRS getCRS() {
        return crs;
    }
    @JSONField(name="crs")
    public void setCRS(CRS crs) {
        this.crs = crs;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
