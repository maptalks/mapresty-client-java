package org.maptalks.javasdk.db;

/**
 * Created by fuzhen on 2015/9/17.
 */
public interface ErrorCodes {
        int ERRCODE_INVALID_RESTMETHOD = 10000;
        int ERRCODE_ILLEGAL_ARGUMENT = 10001;
        int ERRCODE_MAPDB_NOTEXISTS = 10002;
        int ERRCODE_LAYER_NOTEXISTS = 10003;
        int ERRCODE_INVALID_COORDINATE = 10004;
        int ERRCODE_INVALID_CoordinateType = 10005;
        int ERRCODE_SQL_EXCEPTION = 10006;
        int ERRCODE_MONGO_EXCEPTION = 10007;
        int ERRCODE_IO_EXCEPTION = 10008;
        int ERRCODE_TOPO_EXCEPTION = 10009;
        int ERRCODE_SERIALIZE_EXCEPTION = 10010;
        int ERRCODE_UNKNOWN = 20000;
}
