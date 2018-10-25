package org.maptalks.javasdk.db;

import org.maptalks.util.Objects;

public class DataOpRemove extends DataOp {

    private String condition;

    public DataOpRemove(String condition) {
        super(DataOp.REMOVE);
        setCondition(condition);
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataOpRemove)) {
            return false;
        }
        DataOpRemove other = (DataOpRemove) obj;
        return Objects.equals(this.condition, other.condition);
    }
}
