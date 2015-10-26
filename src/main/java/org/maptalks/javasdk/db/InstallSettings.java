package org.maptalks.javasdk.db;

import org.maptalks.gis.core.geojson.common.CoordinateType;

/**
 * Created by fuzhen on 2015/9/21.
 */
public class InstallSettings {
    private CoordinateType coordinateType;

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(CoordinateType coordinateType) {
        this.coordinateType = coordinateType;
    }
}
