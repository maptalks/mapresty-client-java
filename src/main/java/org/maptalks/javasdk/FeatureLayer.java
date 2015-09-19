package org.maptalks.javasdk;

import com.alibaba.fastjson.JSON;
import org.maptalks.gis.core.geojson.Feature;
import org.maptalks.gis.core.geojson.json.GeoJSONFactory;
import org.maptalks.javasdk.db.CoordinateType;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.db.LayerField;
import org.maptalks.javasdk.db.ErrorCodes;
import org.maptalks.javasdk.exceptions.InvalidLayerException;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.http.HttpRestClient;
import org.maptalks.javasdk.utils.ArrayUtils;
import org.maptalks.javasdk.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 图层数据操作类
 * @author duscin
 *
 */
public class FeatureLayer extends Layer {
    private MapDatabase mapDatabase;
    private String restURL;

    /**
     * 构造函数
     *
     * @param id
     * @param mapDatabase
     * @throws IOException
     * @throws RestException
     * @throws InvalidLayerException
     */
    public FeatureLayer(final String id, final MapDatabase mapDatabase)
            throws IOException, RestException, InvalidLayerException {
        Layer layer =  mapDatabase.getLayer(id);
        if (layer == null) {
            throw new InvalidLayerException(
                    "there is no layer with identifier:" + id);
        }
        this.setId(layer.getId());
        this.setName(layer.getName());
        this.setFields(layer.getFields());
        this.setSource(layer.getSource());
        this.setType(layer.getType());
        this.setSymbolConfig(layer.getSymbolConfig());
        this.setFeatureIdColumn(layer.getFeatureIdColumn());
        this.mapDatabase = mapDatabase;
        this.restURL = mapDatabase.dbRestURL;
    }

    /**
     * 默认的构造函数
     */
    protected FeatureLayer() {
    }

    /**
     * 添加图层自定义属性列
     * @param field 欲添加的自定义属性列
     * @throws IOException
     * @throws RestException
     */
    public void addLayerField(LayerField field) throws IOException,
            RestException {
        if (field == null)
            return;
        final String url = this.restURL + "layers/"+this.getId()+"/fields?op=create";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("data", JsonUtils.toJsonString(field));
        HttpRestClient.doPost(url, param, this.mapDatabase.isUseGZIP());
        return;
    }

    /**
     * 修改图层自定义属性列
     * @param fieldName 原属性名
     * @param field 新的自定义属性定义
     * @throws IOException
     * @throws RestException
     */
    public void updateLayerField(String fieldName, LayerField field)
            throws IOException, RestException {
        if (fieldName == null
                || fieldName.trim().length() == 0
                || field == null)
            return;
        final String url = this.restURL + "layers/"+this.getId()+"/fields/"+fieldName+"?op=update";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("data", JSON.toJSONString(field));
        HttpRestClient.doPost(url, param, this.mapDatabase.isUseGZIP());
        return;
    }

    /**
     * 删除图层自定义属性列
     * 对于某些数据库, 属性列存在数据或其他情况下, 属性列可能无法被删除
     * @param fieldName 属性名
     * @throws IOException
     * @throws RestException
     */
    public void removeLayerField(String fieldName) throws IOException,
            RestException {
        if (fieldName == null || fieldName.trim().length() == 0) {
            return;
        }
        final String url = this.restURL + "layers/"+this.getId()+"/fields/"+fieldName+"?op=remove";
        HttpRestClient.doPost(url, null, this.mapDatabase.isUseGZIP());
        return;
    }

    /**
     * 取得图层的自定义属性列
     * @return
     * @throws IOException
     * @throws RestException
     */
    public List<LayerField> getLayerFields() throws IOException, RestException {
        final String url = this.restURL + "layers/"+this.getId()+"/fields";
        final List<LayerField> rest = HttpRestClient.doGetList(url, null,
                LayerField.class, this.mapDatabase.isUseGZIP());
        if (rest == null || rest.size() == 0)
            return null;
        return rest;
    }

    /**
     * 添加 Feature
     *
     * @param feature
     * @throws RestException
     * @throws IOException
     */
    public void add(final Feature feature) throws IOException,
            RestException {
        if (feature == null)
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "Geometry  is null");
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=create";
        postRequest(url, JsonUtils.toJsonString(feature));
    }

    /**
     * 批量添加 Feature
     *
     * @param features
     * @throws IOException
     * @throws RestException
     */
    public void add(final List<Feature> features) throws IOException,
            RestException {
        if (features == null)
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "Geometry  is null");
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=create";
        postRequest(url, JsonUtils.toJsonString(features));
    }

    /**
     * 通过 id 查询 Feature, 该方法只返回一条数据, 如果 id 对应多个 Feature, 则返回第一个
     * @param id featureIdColumn列的id值
     * @return
     * @throws RestException
     */
    public Feature getById(Object id) throws RestException, IOException {
        if (id == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "id or feature is null");
        }
        if (this.getFeatureIdColumn() == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "there is no feature id column defined for layer "+this.getId());
        }
        String quote = (id instanceof String)?"'":"";
        String condition = this.getFeatureIdColumn()+" = " + quote + id + quote;
        QueryFilter filter = new QueryFilter();
        filter.setCondition(condition);
        Feature[] features = this.query(filter, 0, 1);
        if (features!=null && features.length>0) {
            return features[0];
        }
        return null;
    }

    /**
     * 更新符合条件的 Feature
     *
     * @param condition
     * @param feature
     * @throws RestException
     * @throws IOException
     */
    public void update(String condition, final Feature feature)
            throws IOException, RestException {
        if (feature == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "feature is null");
        }
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=update";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("data", JsonUtils.toJsonString(feature));
        params.put("condition", condition);
        HttpRestClient.doPost(url, params, this.mapDatabase.isUseGZIP());
    }

    /**
     * 通过 id 更新 Feature
     *
     * @param id featureIdColumn列的id值
     * @param feature
     */
    public void updateById(Object id, Feature feature) throws RestException, IOException {
        if (id == null || feature == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "id or feature is null");
        }
        if (this.getFeatureIdColumn() == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "there is no feature id column defined for layer "+this.getId());
        }
        String quote = (id instanceof String)?"'":"";
        String condition = this.getFeatureIdColumn()+" = " + quote + id + quote;
        this.update(condition, feature);
    }

    /**
     * 删除符合查询条件的Feature
     *
     * @param condition
     * @throws RestException
     * @throws IOException
     */
    public void remove(String condition)
            throws IOException, RestException {
        if (condition == null || condition.trim().length() == 0) {
            return;
        }
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=remove";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("condition", condition);
        HttpRestClient.doPost(url, params, this.mapDatabase.isUseGZIP());
    }

    /**
     * 通过 id 删除 Feature
     *
     * @param id featureIdColumn列的id值
     */
    public void removeById(Object id) throws RestException, IOException {
        if (id == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "id or feature is null");
        }
        if (this.getFeatureIdColumn() == null) {
            throw new RestException(ErrorCodes.ERRCODE_ILLEGAL_ARGUMENT,
                    "there is no feature id column defined for layer "+this.getId());
        }
        String quote = (id instanceof String)?"'":"";
        String condition = this.getFeatureIdColumn()+" = " + quote + id + quote;
        this.remove(condition);
    }

    /**
     * 删除图层表中所有数据
     *
     * @throws IOException
     * @throws RestException
     */
    public void removeAll() throws IOException, RestException {
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=removeAll";
        postRequest(url, null);

    }


    /**
     * 批量修改图层的自定义属性数据
     * @param condition 查询条件
     * @param properties 新的自定义属性数据
     * @throws IOException
     * @throws RestException
     */
    public void updateProperties(String condition, Object properties) throws IOException,
            RestException {
        if (properties == null) {
            return;
        }
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=update";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("condition", condition);
        params.put("data", JsonUtils.toJsonString(properties));

        HttpRestClient.doPost(url, params, this.mapDatabase.isUseGZIP());
    }

    private void postRequest(final String url, final String data)
            throws IOException, RestException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("data", data);
        HttpRestClient.doPost(url, params, this.mapDatabase.isUseGZIP());
    }


    /**
     * 查询符合条件的数据
     * @param queryFilter
     * @param page 第几页
     * @param count 每页结果数
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Feature[] query(QueryFilter queryFilter, int page, int count)
            throws IOException, RestException {
        if (queryFilter == null) {
            queryFilter = new QueryFilter();
        }
        final String json = queryJson(queryFilter, page, count);
        if (json == null || json.length() == 0) {
            return null;
        }
        return GeoJSONFactory.createFeatureArray(json);
    }


    /**
     * 查询Geometry，但返回的是geometry的json形式，用于只需要返回Json的场景，速度较快
     * @param queryFilter
     * @param page
     * @param count
     * @return
     * @throws IOException
     * @throws RestException
     */
    public String queryJson(QueryFilter queryFilter, int page, int count)
            throws IOException, RestException {
        if (page < 0 || count <= 0) {
            return null;
        }
        if (queryFilter == null) {
            queryFilter = new QueryFilter();
        }
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=query";

        final Map<String, String> params = FeatureLayer
                .prepareFilterParameters(queryFilter);
        params.put("page", page + "");
        params.put("count", count + "");

        return HttpRestClient.doPost(url, params, this.mapDatabase.isUseGZIP());
    }

    /**
     * 统计结果数
     * @param queryFilter
     * @return
     * @throws IOException
     * @throws RestException
     */
    public long count(QueryFilter queryFilter) throws IOException,
            RestException {
        if (queryFilter == null) {
            queryFilter = new QueryFilter();
        }
        final String url = this.restURL + "layers/"+this.getId()+"/data?op=count";
        final Map<String, String> params = FeatureLayer
                .prepareFilterParameters(queryFilter);
        return Long.parseLong(HttpRestClient.doPost(url, params,
                this.mapDatabase.isUseGZIP()));
    }

    protected static Map<String, String> prepareFilterParameters(
            QueryFilter queryFilter) {
        if (queryFilter == null) {
            return new HashMap<String, String>();
        }
        final Map<String, String> params = new HashMap<String, String>();
        String condition = queryFilter.getCondition();
        if (condition != null && condition.length() > 0) {
            params.put("condition", condition);
        }
        SpatialFilter spatialFilter = queryFilter.getSpatialFilter();
        if (spatialFilter != null && spatialFilter.getGeometry() != null) {
            params.put("spatialFilter", JsonUtils.toJsonString(spatialFilter));
        }

        CoordinateType retCoordinateType = queryFilter.getCoordinateType();
        if (retCoordinateType != null) {
            params.put("coordinateType", retCoordinateType.toString());
        }
        if (queryFilter.isReturnGeometry()) {
            params.put("returnGeometry", queryFilter.isReturnGeometry() + "");
        }
        String[] fields = queryFilter.getResultFields();
        if (fields != null) {
            params.put("fields", ArrayUtils.join(fields));
        }
        return params;
    }
}
