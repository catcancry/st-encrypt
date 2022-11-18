package vip.ylove.sdk.util;

import vip.ylove.sdk.common.StAuthInfo;

/**
 * 基于BCrypt对auth进行基本授权码加密和验证，拒绝明文存储授权码
 * @author catcancry
 */
public class StAuthUtil {
    /**
     * 保存授权信息
     * @author catcancry
     **/
    private static final  ThreadLocal<StAuthInfo> auth = new ThreadLocal<>();

    /**
     * 获取授权信息
     * @return StAuthInfo
     */
    public static StAuthInfo getStAuth(){
        return auth.get();
    }

    /**
     * 设置授权信息
     * @param stAuthInfo 授权信息
     */
    protected static void setStAuth(final StAuthInfo stAuthInfo){
         auth.set(stAuthInfo);
    }

    /**
     * 移除授权信息
     */
    protected static void clearStAuth(){
        auth.remove();
    }

    /**
     * 验证授权是否正确
     * @author catcancry
     * @param appAuth         请求中的授权码
     * @param encryptAppAuth   服务端保存的加密授权信息
     * @return boolean
     **/
    public static boolean verify(final String appAuth,final String encryptAppAuth){
       return BCrypt.checkpw(appAuth,encryptAppAuth);
    }

    /**
     *  对授权信息进行加密
     * @author catcancry
     * @param pwd  授权码
     * @return java.lang.String
     **/
    public static String encrypt(final String pwd){
        return BCrypt.hashpw(pwd,BCrypt.gensalt());
    }
}
