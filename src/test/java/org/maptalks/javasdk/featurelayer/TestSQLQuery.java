package org.maptalks.javasdk.featurelayer;

import org.maptalks.javasdk.featurelayer.common.Equations;
import org.maptalks.javasdk.featurelayer.common.TestFeatureQuery;

/**
 * Created by fuzhen on 2015/10/6.
 */
public class TestSQLQuery extends TestFeatureQuery{
    @Override
    public String eq(String field, Object value) {
        return Equations.SQL.eq(field, value);
    }

    @Override
    public String startsWith(String field, String prefix) {
        return Equations.SQL.startsWith(field, prefix);
    }
}
