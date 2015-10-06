package org.maptalks.javasdk.featurelayer.common;

/**
 * Created by fuzhen on 2015/6/26.
 */
public abstract class TestCommon {

    /**
     * 生成条件为等于的条件判断查询语句
     * 例如SQL数据库是 field='value'
     *     MongoDB是  {'field':value}
     * @param field
     * @param value
     * @return
     */
    public abstract String eq(String field, Object value);

    /**
     * 生成是否以prefix开头的查询语句
     * 例如在SQL中为 field like '%prefix'
     * @param field
     * @param prefix
     * @return
     */
    public abstract String startsWith(String field, String prefix);
}
