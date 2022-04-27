package vip.ylove.sdk.server.dencrypt;

/**
 * 服务端-请求参数解密接口
 * @author catcancry
 * @date 2022/4/26 14:21
 **/
public interface StAbstractRequestDencrypt {

    /**
     * 定义解密
     * @param RSAKey
     * @param content
     * @param stAuth
     * @return
     */
    byte[]  dencrypt(final  String RSAKey,final String content,final StAbstractAuth stAuth);

}
