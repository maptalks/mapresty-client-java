package org.maptalks.javasdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.maptalks.gis.core.geojson.Feature;
import org.maptalks.gis.core.geojson.FeatureCollection;
import org.maptalks.gis.core.geojson.json.GeoJSONFactory;
import org.maptalks.javasdk.db.DBInfo;
import org.maptalks.javasdk.db.InstallSettings;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.http.HttpRestClient;
import org.maptalks.javasdk.db.Layer;
import org.maptalks.javasdk.http.RestResult;
import org.maptalks.javasdk.utils.ArrayUtils;
import org.maptalks.javasdk.utils.JsonUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 空间库操作类
 * @author duscin
 *
 */
public class MapDatabase {
    public String restURL;
    public final String dbRestURL;
    protected final String db;
    private boolean useGZIP = true;
    protected String host;

    /**
     * 构造函数
     *
     * @param host
     *            服务器地址
     * @param port
     *            端口号
     * @param db
     *            数据库名
     */
    public MapDatabase(final String host, int port, final String db) {
        this.db = db;
        this.host = host+":"+port;
        this.restURL = "http://" + this.host + "/enginerest/";
        this.dbRestURL = this.restURL + "rest/databases/" + db + "/";
    }

    /**
     * 空间库初始化
     * @param settings 空间库初始化参数, 如果为空, 则采用默认参数初始化空间库
     * @throws IOException
     * @throws RestException
     */
    public void install(InstallSettings settings) throws IOException, RestException {
        final String url = this.dbRestURL.substring(0,this.dbRestURL.length()-1)+"?op=install";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("settings", JSON.toJSONString(settings));
        HttpRestClient.doPost(url, param, useGZIP);
    }

    /**
     * 获取空间库的信息
     * @return
     */
    public DBInfo getDatabaseInfo() throws IOException, RestException {
        final String url = this.dbRestURL;
        final String resultData = HttpRestClient.doGet(url, null, this.useGZIP);
        RestResult result = JSON.parseObject(resultData, RestResult.class);
        if (!result.isSuccess()) {
            throw new RestException(result.getErrCode(), result.getError());
        }
        DBInfo dbInfo =  JSON.parseObject(result.getData(), DBInfo.class);
        dbInfo.setName(this.db);
        return dbInfo;
    }

    /**
     * 取得所有图层
     *
     * @return
     * @throws IOException
     * @throws RestException
     */
    public List<Layer> getAllLayers() throws IOException, RestException {
        final String url = this.dbRestURL + "layers";
        final List rest = HttpRestClient.doGetList(url, null, Layer.class,
                useGZIP);
        return rest;
    }

    /**
     * 获取图层
     * @param id
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Layer getLayer(final String id) throws IOException,
            RestException {
        if (id == null || id.length() == 0) {
            return null;
        }
        final String url = this.dbRestURL + "layers/"+id;
        final List rest = HttpRestClient.doGetList(url, null, Layer.class,
                useGZIP);
        if (rest == null || rest.size() == 0) {
            return null;
        }
        return (Layer) rest.get(0);
    }

    /**
     * 添加图层
     *
     * @param layer
     * @throws IOException
     * @throws RestException
     */
    public void addLayer(final Layer layer) throws IOException, RestException {
        if (layer == null) {
            return;
        }
        final String url = this.dbRestURL + "layers?op=create";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("data", JSON.toJSONString(layer));
        HttpRestClient.doPost(url, param, useGZIP);
        return;
    }

    /**
     * 删除图层
     *
     * @param id
     * @throws IOException
     * @throws RestException
     */
    public void removeLayer(final String id) throws IOException,
            RestException {
        final String url = this.dbRestURL + "layers/"+id+"?op=remove";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("data", id);

        HttpRestClient.doPost(url, param, useGZIP);
        return;
    }

    /**
     * 更新图层
     *
     * @param layer
     * @throws IOException
     * @throws RestException
     */
    public void updateLayer(String id, final Layer layer) throws IOException,
            RestException {
        if (layer == null)
            return;
        final String url = this.dbRestURL + "layers/"+id+"?op=update";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("data", JSON.toJSONString(layer));
        HttpRestClient.doPost(url, param, useGZIP);
        return;
    }

    /**
     * 一次性查询多个图层中复合条件的Geometry，但返回的是geometry的json形式，用于只需要返回Json的场景，速度较快
     * @param queryFilter
     * @param page
     * @param count
     * @param layerIds
     * @return
     * @throws IOException
     * @throws RestException
     */
    public String queryJson(QueryFilter queryFilter, int page, int count, String[] layerIds) throws IOException, RestException {
        if (page < 0 || count <= 0 || layerIds == null || layerIds.length==0) {
            return null;
        }
        if (queryFilter == null) {
            queryFilter = new QueryFilter();
        }
        final String url = this.dbRestURL + "layers/"+ ArrayUtils.join(layerIds)+"/data?op=query";

        final Map<String, String> params = FeatureLayer
                .prepareFilterParameters(queryFilter);
        params.put("page", page + "");
        params.put("count", count + "");

        return HttpRestClient.doPost(url, params, this.isUseGZIP());
    }

    /**
     * 一次性查询多个图层中符合条件的数据, 没有结果时返回空数组
     * @param queryFilter
     * @param page
     * @param count
     * @param layerIds
     * @return
     * @throws IOException
     * @throws RestException
     */
    public FeatureCollection[] query(QueryFilter queryFilter, int page, int count, String[] layerIds) throws IOException, RestException {
        if (queryFilter == null) {
            queryFilter = new QueryFilter();
        }
        final String json = queryJson(queryFilter, page, count, layerIds);
        if (json == null || json.length() == 0) {
            return new FeatureCollection[0];
        }
        return GeoJSONFactory.createFeatureCollectionArray(json);
    }

    /**
     * 是否采用gzip, 能显著减小数据传输体积, 默认采用
     * @return
     */
    public boolean isUseGZIP() {
        return useGZIP;
    }

    /**
     * 设定是否采用gzip模式，如果设为false则不采用
     * @param useGZIP
     */
    public void setUseGZIP(final boolean useGZIP) {
        this.useGZIP = useGZIP;
    }
}
