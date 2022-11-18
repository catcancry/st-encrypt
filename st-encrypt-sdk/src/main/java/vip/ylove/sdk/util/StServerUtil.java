package vip.ylove.sdk.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.common.StAuthInfo;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.common.StErrorCode;
import vip.ylove.sdk.dto.StResponseBody;
import vip.ylove.sdk.dto.StResquestBody;
import vip.ylove.sdk.exception.StException;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;

/**
 * 服务端解密请求参数和加密响应结果工具
 **/
public class StServerUtil {

    private static Logger log = LoggerFactory.getLogger(StServerUtil.class);

    /**
     * 加密响应结果
     *
     * @param privateKey 私钥
     * @param content  加密内容
     * @param stAuth   授权验证
     * @return vip.ylove.sdk.dto.StResponseBody
     **/
    public static StResponseBody encrypt(final String privateKey, final String content,final StAbstractAuth stAuth) {
        String key = stAuth.key();
        if(key == null ){
            StException.throwExec(StException.ErrorCode.NOT_GET_KEY,"获取数据加密解密key,但是未能获取到");
            return null;
        }
        byte[] dataByte = StrUtil.bytes(content, StConst.DEFAULT_CHARSET);
        String encryptData = SecureUtil.aes(StrUtil.bytes(key, StConst.DEFAULT_CHARSET)).encryptBase64(dataByte);
        //签名原始内容
        String sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, null).signHex(dataByte);
        //移除授权内容
        StAuthUtil.clearStAuth();
        return new StResponseBody(sign, encryptData);
    }


    /**
     * 解密请求参数
     *
     * @param privateKey  私钥
     * @param content    加密内容
     * @param stEncrypt  扩展信息
     * @param stAuth     鉴权接口
     * @return java.lang.Object
     **/
    public static byte[] dencrypt(final String privateKey,final String content,final StEncrypt stEncrypt,final StAbstractAuth stAuth) {
        StResquestBody dencryptBody = JSONUtil.toBean(content, StResquestBody.class);
        return dencrypt(privateKey,dencryptBody.getKey(),dencryptBody.getData(),stEncrypt,stAuth);
    }

    /**
     * 解密请求参数
     *
     * @param privateKey  私钥
     * @param encryptKey    加密key
     * @param encryptData    加密data
     * @param stEncrypt  扩展信息
     * @param stAuth     鉴权接口
     * @return java.lang.Object
     **/
    public static byte[] dencrypt(final String privateKey,final String encryptKey,final String encryptData,final StEncrypt stEncrypt,final StAbstractAuth stAuth) {
        //当key为空时,从StAbstractAuth中获取加密key
        if (!StrUtil.isBlankIfStr(encryptKey)) {
            //使用私钥解密
            byte[] decryptKey = SecureUtil.rsa(privateKey, null).decrypt(encryptKey, KeyType.PrivateKey);
            String keyData = new String(decryptKey);
            String[] split = keyData.split(StConst.SPLIT);
            if (split.length == 4) {
                String aesKey = split[0];
                String t = split[1];
                String appId = split[2];
                String auth = split[3];

                //调用接口进行权限验证,若没有实现则默认跳过验证
                if (stAuth != null ) {
                    if(stEncrypt == null  || stEncrypt.auth()){
                        boolean authResult = stAuth.auth(aesKey,appId, auth, t, stEncrypt);
                        if (!authResult) {
                            StException.throwExec(StErrorCode.error2);
                            return null;
                        }
                    }
                } else {
                    log.debug("未实现StAuth权限验证,或者不进行权限验证,跳过验证");
                }
                //存储授权信息
                StAuthUtil.setStAuth(new StAuthInfo() {
                    @Override
                    public String getAppId() {
                        return appId;
                    }

                    @Override
                    public String getAppAuth() {
                        return auth;
                    }

                    @Override
                    public String getT() {
                        return t;
                    }

                    @Override
                    public String getKey() {
                        return aesKey;
                    }
                });
            } else {
                StException.throwExec(StErrorCode.error11);
                return null;
            }

        }else{
            if(stAuth == null || stAuth.key() == null || stAuth.key().length() == 0){
                StException.throwExec(StErrorCode.error12);
            }
        }

        //加密data为空，说明只是单纯的往后端传递了一个加密key
        byte[] decryptData = null;
        if(!StrUtil.isBlankIfStr(encryptData)){
            String key = stAuth.key();
            if(key == null ){
                StException.throwExec(StException.ErrorCode.NOT_GET_KEY,"获取数据加密解密key,但是未能获取到");
                return null;
            }
            //解密body
           decryptData = SecureUtil.aes(StrUtil.bytes(key, StConst.DEFAULT_CHARSET)).decrypt(encryptData);
        }else{
            decryptData = null;
        }
        return decryptData;
    }

}
