package vip.ylove.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@ConfigurationProperties(prefix = "st.encrypt")
@Configuration
public class StConfig {

    /**
     * 加密私钥
     */
    private String privateKey;

    /**
     * 加密私钥
     */
    private String publicKey;

    /**
     *  授与调用方的 appId 也可以是账号
     */
    private String appId;

    /**
     *  授与调用方的 appAuth 也可以是密码
     */
    private String appAuth;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppAuth() {
        return appAuth;
    }

    public void setAppAuth(String appAuth) {
        this.appAuth = appAuth;
    }
}
