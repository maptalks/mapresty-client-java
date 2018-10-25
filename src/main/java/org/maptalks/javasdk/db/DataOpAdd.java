package org.maptalks.javasdk.db;

import org.maptalks.geojson.Feature;
import org.maptalks.util.Objects;

import java.util.List;

public class DataOpAdd extends DataOp {

    private List<Feature> features;

    public DataOpAdd(List<Feature> features) {
        super(DataOp.ADD);
        setFeatures(features);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataOpAdd)) {
            return false;
        }
        DataOpAdd other = (DataOpAdd) obj;
        return Objects.equals(this.features, other.features);
    }
}
