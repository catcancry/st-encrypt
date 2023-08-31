package vip.ylove.sdk.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StDefaultJsonCode implements StAbstractJsonDcode {

    static Object mapper = null;

    public static void setMapper() {
        if (mapper == null){
            synchronized (StDefaultJsonCode.class){
                if (mapper == null){
                    mapper = new ObjectMapper();
                }
            }
        }
    }

    @Override
    public String toJson(Object data) {
        try {
            StDefaultJsonCode.setMapper();
            return ((ObjectMapper)mapper).writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T toBean(String data, Class<T> cls) {
        try {
            StDefaultJsonCode.setMapper();
            return ((ObjectMapper)mapper).readValue(data,cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T toBean(byte[] data, Class<T> cls) {
        try {
            StDefaultJsonCode.setMapper();
            return ((ObjectMapper)mapper).readValue(data,cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
