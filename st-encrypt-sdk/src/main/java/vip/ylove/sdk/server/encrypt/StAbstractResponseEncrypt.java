package vip.ylove.sdk.server.encrypt;

/**
 * 服务端-响应加密接口
 * @author catcancry
 **/
public interface StAbstractResponseEncrypt {

     /**
      *  加密方法
      * @author catcancry
      * @param RSAKey  key
      * @param content 内容
      * @return java.lang.Object
      **/
     Object encrypt(final String RSAKey,final String content);

}
