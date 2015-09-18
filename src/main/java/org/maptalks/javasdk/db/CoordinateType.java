package org.maptalks.javasdk.db;

/**
 * Created by fuzhen on 2015/8/11.
 */
public enum CoordinateType {
    wgs84("wgs84"), gcj02("gcj02"), cgcs2000("cgcs2000"), bd09ll("bd09ll"), pixel("pixel"),DEFAULT("gcj02");

    private final String type;

    CoordinateType(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public static CoordinateType getInstance(String t) {
        try {
            return CoordinateType.valueOf(t);
        } catch (Throwable e) {
            return null;
        }
    }
}
