package vip.ylove;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = SpringUtil.getBean(ObjectMapper.class);

    /**
     *  转换为jsonStr
     * @param obj obj
     * @return JsonString
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * json转换为bean
     * @param jsonStr jsonString
     * @param clas  bean.class
     * @return      bean
     * @param <T>   bean
     */
    public static <T> T toBean(String jsonStr,Class<T> clas) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr,clas);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
