package org.maptalks.javasdk.db;

import org.maptalks.gis.core.geojson.CRS;
import org.maptalks.gis.core.geojson.common.CoordinateType;

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

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
