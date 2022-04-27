package vip.ylove.sdk.server.encrypt;

/**
 * 服务端-响应加密接口
 * @author catcancry
 * @date 2022/4/26 14:29
 **/
public interface StAbstractResponseEncrypt {

     Object encrypt(final String RSAKey,final String content);

}
