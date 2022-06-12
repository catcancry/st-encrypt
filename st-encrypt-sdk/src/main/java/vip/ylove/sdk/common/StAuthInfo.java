package vip.ylove.sdk.common;

/**
 * 获取用户信息
 */
public interface StAuthInfo {

    /**
     * 获取授权 appId
     * @return 授权信息
     */
    public String getAppId();

    /**
     * 获取授权 appAuth
     * @return 授权信息
     */
    public String getAppAuth();

    /**
     * 获取 t
     * @return t信息
     */
    public String getT();

    /**
     * 获取加密的key
     * @return 加密的key
     */
    public String getKey();

}
