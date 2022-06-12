package vip.ylove.sdk.server.encrypt;

import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.util.StServerUtil;

/**
 * 服务端-响应加密接口
 * @author catcancry
 **/
public interface StAbstractResponseEncrypt {

     /**
      * 加密响应结果
      * @param privateKey 私钥
      * @param content  加密内容
      * @param stAuth   授权信息
      * @return java.lang.Object
      **/
     default public Object encrypt(final String privateKey, final String content,final StAbstractAuth stAuth) {
          return StServerUtil.encrypt(privateKey,content,stAuth);
     }
}
