//package vip.ylove.demo.config;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import org.mockito.internal.invocation.AbstractAwareMethod;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import vip.ylove.sdk.json.StAbstractJsonDcode;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 配置加解密使用fastjson
// */
//@Configuration
//public class ConfigStJsonDcode {
//    @Bean
//    public StAbstractJsonDcode initStAbstractJsonCode(){
//        return new StAbstractJsonDcode(){
//            @Override
//            public String toJson(Object data) {
//                return JSONObject.toJSONString(data);
//            }
//
//            @Override
//            public <T> T toBean(String data, Class<T> cls) {
//                return JSONObject.parseObject(data,cls);
//            }
//
//            @Override
//            public <T> T toBean(byte[] data, Class<T> cls) {
//                return JSONObject.parseObject(data,cls);
//            }
//        };
//    }
//}