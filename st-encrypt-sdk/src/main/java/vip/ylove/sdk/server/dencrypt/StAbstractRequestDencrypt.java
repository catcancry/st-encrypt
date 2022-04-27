package vip.ylove.sdk.server.dencrypt;

/**
 * 服务端-请求参数解密接口
 * @author catcancry
 **/
public interface StAbstractRequestDencrypt {

    /**
     * 定义解密
     * @param RSAKey key
     * @param content 内容
     * @param stAuth 鉴权方法 可为空
     * @return byte[]
     */
    byte[]  dencrypt(final  String RSAKey,final String content,final StAbstractAuth stAuth);

}
