package vip.ylove.sdk.common;

/**
 * @author catcancry
 */
public class StAuthInfo {
    /**
     * 授权appId
     **/
    String appId;
    /**
     * 授权appAuth
     **/
    String appAuth;
    /**
     * 扩展信息 时间戳，也可以是其他信息
     **/
    String t;

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


    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public StAuthInfo() {
    }

    public StAuthInfo(String appId, String appAuth, String t) {
        this.appId = appId;
        this.appAuth = appAuth;
        this.t = t;
    }

    @Override
    public String toString() {
        return "StAuthInfo{" + "appId='" + appId + '\'' + ", appAuth='" + appAuth + '\'' + ", t='" + t + '\'' + '}';
    }
}
