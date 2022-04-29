package vip.ylove.sdk.util;

/**
 * 基于BCrypt对auth进行基本授权码加密和验证，拒绝明文存储授权码
 * @Description
 * @Author catcancry
 */
public class StAuthUtil {

    /**
     * 验证授权是否正确
     * @author catcancry
     * @param appAuth         请求中的授权码
     * @param encryptAppAuth   服务端保存的加密授权信息
     * @return boolean
     **/
    public static boolean verify(String appAuth,String encryptAppAuth){
       return BCrypt.checkpw(appAuth,encryptAppAuth);
    }

    /**
     *  对授权信息进行加密
     * @author catcancry
     * @param pwd  授权码
     * @return java.lang.String
     **/
    public static String encrypt(String pwd){
        return BCrypt.hashpw(pwd,BCrypt.gensalt());
    }
}
