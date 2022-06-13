package vip.ylove.sdk.server.dencrypt;

import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.util.StServerUtil;

/**
 * 服务端-请求参数解密接口
 **/
public interface StAbstractRequestDencrypt {


    /**
     *  定义解密方式
     * @param privateKey 解密私钥
     * @param content    加密内容内容
     * @param stEncrypt  方法上的注解
     * @param stAuth     鉴权方法 可为空
     * @return
     */
    default public byte[] dencrypt(String privateKey, String content, StEncrypt stEncrypt, StAbstractAuth stAuth) {
        return StServerUtil.dencrypt(privateKey,content,stEncrypt,stAuth);
    }

    /**
     *  定义解密方式
     *
     * @param privateKey 解密私钥
     * @param encryptKey 数据加密key
     * @param content    加密内容内容
     * @param stEncrypt  方法上的注解
     * @param stAuth     鉴权方法 可为空
     * @return
     */
    default public byte[] dencrypt(String privateKey,String encryptKey,  String content, StEncrypt stEncrypt, StAbstractAuth stAuth) {
        return StServerUtil.dencrypt(privateKey,encryptKey,content,stEncrypt,stAuth);
    }

}
