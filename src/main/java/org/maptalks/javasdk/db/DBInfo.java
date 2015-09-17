package org.maptalks.javasdk.db;

/**
 * Created by fuzhen on 2015/9/17.
 */
public class DBInfo {
    private String name;
    private String coordinateType;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
