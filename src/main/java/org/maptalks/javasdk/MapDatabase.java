package org.maptalks.javasdk;

import com.alibaba.fastjson.JSON;
import org.maptalks.javasdk.db.DBInfo;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.http.HttpRestClient;
import org.maptalks.javasdk.db.Layer;

import java.io.IOException;
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
        this.restURL = "http://" + this.host + "/rest/";
        this.dbRestURL = this.restURL + "rest/databases/" + db + "/";
    }

    /**
     * 获取空间库的信息
     * @return
     */
    public DBInfo getDatabaseInfo() {
        return null;
    }

    /**
     * 取得所有图层
     *
     * @return
     * @throws IOException
     * @throws RestException
     */
    public List<Layer> getLayerInfo() throws IOException, RestException {
        final String url = this.dbRestURL + "layers";
        final List rest = HttpRestClient.doParseGet(url, null, Layer.class,
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
        final List rest = HttpRestClient.doParseGet(url, null, Layer.class,
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
