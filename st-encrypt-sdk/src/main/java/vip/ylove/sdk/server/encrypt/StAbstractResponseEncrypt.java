package vip.ylove.sdk.server.encrypt;

/**
 * 服务端-响应加密接口
 * @author catcancry
 **/
public interface StAbstractResponseEncrypt {

     /**
      * 加密响应结果
      * @param privateKey 私钥
      * @param aesKey  加密aes的key
      * @param content  加密内容
      * @return java.lang.Object
      **/
     Object encrypt(final String privateKey,final String aesKey,final String content);

}
