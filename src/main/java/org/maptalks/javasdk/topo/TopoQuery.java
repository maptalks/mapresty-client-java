package org.maptalks.javasdk.topo;

import com.alibaba.fastjson.JSON;
import org.maptalks.gis.core.geojson.Geometry;
import org.maptalks.gis.core.geojson.json.GeoJSONFactory;
import org.maptalks.javasdk.exceptions.RestException;
import org.maptalks.javasdk.http.HttpRestClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fuzhen on 2015/10/29.
 */
public class TopoQuery {
    private final String host;
    private final String restURL;

    /**
     * 构造函数
     *
     * @param host
     *            服务器地址
     * @param port
     *            端口号
     */
    public TopoQuery(final String host, int port) {

        this.host = host+":"+port;
        this.restURL = "http://" + this.host + "/enginerest/";
    }

    /**
     * 计算src与targets的相交图形, 相交部分根据情况可能是MultiPolygon或Polygon等
     * 如果Geometry未指定CRS, 则采用默认CRS
     * @param src
     * @param targets
     * @return
     */
    public Geometry[] intersection(Geometry src, Geometry[] targets) throws IOException, RestException {
        return topo(src, targets, "analysis/intersection",null);
    }


    /**
     * 对targets做simpilfy操作
     * @param targets
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] simplify(Geometry[] targets) throws IOException, RestException {
        return topo(null, targets, "simplify",null);
    }

    /**
     * 计算targets的缓冲
     * @param targets
     * @param distance 缓冲距离
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] buffer(Geometry[] targets, final double distance) throws IOException, RestException {
        return topo(null, targets, "analysis/buffer",new HashMap<String, String>(){{put("distance",distance+"");}});
    }

    /**
     * 计算targets的外包多边形
     * 凸壳分析（ConvexHull）:包含几何形体的所有点的最小凸壳多边形（外包多边形）
     * @param targets
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] convexHull(Geometry[] targets) throws IOException, RestException {
        return topo(null, targets, "analysis/convexhull",null);
    }

    /**
     * 计算src和targets的差异部分
     * 差异分析（Difference）: AB形状的差异分析就是A里有B里没有的所有点的集合。
     * @param src
     * @param targets
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] difference(Geometry src, Geometry[] targets) throws IOException, RestException {
        return topo(src, targets, "analysis/difference",null);
    }

    /**
     * 计算src和targets的对称差异部分
     * 对称差异分析（SymDifference）:AB形状的对称差异分析就是位于A中或者B中但不同时在AB中的所有点的集合
     * @param src
     * @param targets
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] symDifference(Geometry src, Geometry[] targets) throws IOException, RestException {
        return topo(src, targets, "analysis/symdifference",null);
    }

    /**
     * 计算src与targets中图形分别合并后的图形
     * 联合分析（Union）:AB的联合操作就是AB所有点的集合。
     * @param src
     * @param targets
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Geometry[] union(Geometry src, Geometry[] targets) throws IOException, RestException {
        return topo(src, targets, "analysis/union",null);
    }

    /**
     * 判断src和targets的空间关系, 返回的结果数组中: 1-符合关系;0:不符合关系
     * @param src
     * @param targets
     * @param relation 空间关系, 参考SpatialFilter中的常量
     * @return
     * @throws IOException
     * @throws RestException
     */
    public Integer[] relate(Geometry src, Geometry[] targets, int relation) throws IOException, RestException {
        final String url = this.restURL + "geometry/relation";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("source",src.toString());
        parameters.put("targets", JSON.toJSONString(targets));
        parameters.put("relation",relation+"");
        String json = HttpRestClient.doPost(url, parameters,true);
        List<Integer> results =  JSON.parseArray(json, Integer.class);
        return results.toArray(new Integer[results.size()]);
    }

    private Geometry[] topo(Geometry src, Geometry[] targets, String operation, Map<String, String> parameters) throws IOException, RestException {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        final String url = this.restURL + "geometry/"+operation;
        if (src != null) {
            parameters.put("source",src.toString());
        }
        parameters.put("targets", JSON.toJSONString(targets));
        String json = HttpRestClient.doPost(url, parameters,true);
        if (json == null) {
            return new Geometry[targets.length];
        }
        return GeoJSONFactory.createGeometryArray(json);
    }
}
