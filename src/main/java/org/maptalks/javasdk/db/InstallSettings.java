package org.maptalks.javasdk.db;

import org.maptalks.gis.core.geojson.CRS;

/**
 * Created by fuzhen on 2015/9/21.
 */
public class InstallSettings {
    private CRS crs;

    public void InstallSettings() {

    }

    public void InstallSettings(CRS crs) {
        this.crs = crs;
    }

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }
}
