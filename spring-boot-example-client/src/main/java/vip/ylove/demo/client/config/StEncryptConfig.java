package vip.ylove.demo.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *  配置秘钥等信息
 * @author catcancry
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "st.encrypt")
public class StEncryptConfig {

    /**
     * 加密私钥
     */
    private String publicKey;

    /**
     *  授与调用方的 appId 也可以是账号(主要用于客户端)
     */
    private String appId;

    /**
     *  授与调用方的 appAuth 也可以是密码(主要用于客户端)
     */
    private String appAuth;



}
