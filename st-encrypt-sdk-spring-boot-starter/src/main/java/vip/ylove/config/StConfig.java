package vip.ylove.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * 开启全局加密解密
     **/
    private boolean enableGlobalEncrypt =  false;

    /**
     * 配置intercepter相关参数
     */
    private StIntercepterConfig stIntercepterConfig = new StIntercepterConfig();
    /**
     * 配置filter相关参数
     */
    private StFilterConfig stFilterConfig = new StFilterConfig();

    /**
     * st-filter配置
     */
    public class StFilterConfig {

        /**
         *  默认 st-filter-name
         */
        private String name ="st-filter-name";

        /**
         * 默认 Ordered.LOWEST_PRECEDENCE 2147483647
         */
        private int order = Ordered.LOWEST_PRECEDENCE;
        /**
         * 默认 /*
         */
        private String [] urlPatterns = new String[]{"/*"};

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String[] getUrlPatterns() {
            return urlPatterns;
        }

        public void setUrlPatterns(String[] urlPatterns) {
            this.urlPatterns = urlPatterns;
        }
    }
    /**
     * st-intercepter配置
     */
    public class StIntercepterConfig {
        /**
         * 默认 null
         */
        private final List<String> patterns = new ArrayList();
        /**
         * 默认 null
         */
        private final List<String> includePatterns = new ArrayList();
        /**
         * 默认 null
         */
        private final List<String> excludePatterns = new ArrayList();
        /**
         * 默认 0
         */
        private int order = 0;

        public List<String> getPatterns() {
            return patterns;
        }

        public List<String> getIncludePatterns() {
            return includePatterns;
        }

        public List<String> getExcludePatterns() {
            return excludePatterns;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

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

    public StIntercepterConfig getStIntercepterConfig() {
        return stIntercepterConfig;
    }

    public void setStIntercepterConfig(StIntercepterConfig stIntercepterConfig) {
        this.stIntercepterConfig = stIntercepterConfig;
    }

    public StFilterConfig getStFilterConfig() {
        return stFilterConfig;
    }

    public void setStFilterConfig(StFilterConfig stFilterConfig) {
        this.stFilterConfig = stFilterConfig;
    }
}
