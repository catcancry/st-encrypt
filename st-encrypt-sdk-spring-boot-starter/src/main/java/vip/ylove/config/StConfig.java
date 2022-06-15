package vip.ylove.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置参数
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
     *  授与调用方的 appId 也可以是账号(主要用于客户端)
     */
    private String appId;

    /**
     *  授与调用方的 appAuth 也可以是密码(主要用于客户端)
     */
    private String appAuth;

    /**
     * 加密filter顺序
     **/
    private Integer stFilterOrder;

    /**
     * 跳过的路径
     **/
    private String excludesUrl;

    /**
     * 开启全局加密解密
     **/
    private boolean enableGlobalEncrypt =  false;

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

    public boolean isEnableGlobalEncrypt() {
        return enableGlobalEncrypt;
    }

    public void setEnableGlobalEncrypt(boolean enableGlobalEncrypt) {
        this.enableGlobalEncrypt = enableGlobalEncrypt;
    }

    public String getExcludesUrl() {
        return excludesUrl;
    }

    public void setExcludesUrl(String excludesUrl) {
        this.excludesUrl = excludesUrl;
    }

    public Integer getStFilterOrder() {
        return stFilterOrder;
    }

    public void setStFilterOrder(Integer stFilterOrder) {
        this.stFilterOrder = stFilterOrder;
    }
}
