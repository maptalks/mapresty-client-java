package org.maptalks.javasdk.sort;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class SortOrderDeserializer implements ObjectDeserializer {
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        String s = lexer.stringVal();
        if ("asc".equalsIgnoreCase(s)) {
            return (T) SortOrder.ASC;
        } else if ("desc".equalsIgnoreCase(s)) {
            return (T) SortOrder.DESC;
        }
        return (T) SortOrder.ASC;
    }

    public int getFastMatchToken() {
        return 0;
    }
}
