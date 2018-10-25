package org.maptalks.javasdk.db;

import com.alibaba.fastjson.annotation.JSONField;
import org.maptalks.geojson.Feature;
import org.maptalks.util.Objects;

import java.util.List;

public class DataOpUpdate extends DataOp {

    private List<Item> items;

    public DataOpUpdate(List<Item> items) {
        super(DataOp.UPDATE);
        setItems(items);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataOpUpdate)) {
            return false;
        }
        DataOpUpdate other = (DataOpUpdate) obj;
        return Objects.equals(this.items, other.items);
    }

    public static class Item {
        @JSONField(ordinal = 1)
        private String condition;
        @JSONField(ordinal = 2)
        private Feature feature;

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public Feature getFeature() {
            return feature;
        }

        public void setFeature(Feature feature) {
            this.feature = feature;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Item)) {
                return false;
            }
            Item other = (Item) obj;
            return Objects.equals(this.condition, other.condition)
                && Objects.equals(this.feature, other.feature);
        }
    }
}
