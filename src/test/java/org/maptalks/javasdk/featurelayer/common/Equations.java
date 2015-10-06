package org.maptalks.javasdk.featurelayer.common;

/**
 * Created by fuzhen on 2015/10/6.
 */
public class Equations {
    public static class SQL {
        public static String eq(String field, Object value) {
            if (value instanceof String) {
                return field+" = '"+value+"'";
            } else {
                return field+" = "+value;
            }

        }

        public static String startsWith(String field, String prefix) {
            return field+" like '"+prefix+"%'";
        }
    }

    public static class Mongo {
        public static String eq(String field, Object value) {
            if (value instanceof String) {
                return "{\""+field+"\":\""+value+"\"}";
            } else {
                return "{\""+field+"\":"+value+"}";
            }

        }

        public static String startsWith(String field, String prefix) {
            return "{\""+field+"\":/^"+prefix+"/}}";
        }
    }
}
