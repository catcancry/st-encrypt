package vip.ylove.sdk.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.sdk.common.StAuthInfo;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.dto.StBody;
import vip.ylove.sdk.dto.StResquestBody;
import vip.ylove.sdk.exception.StException;

import java.util.HashMap;

/**
 *  调用端加密请求参数和解密响应结果工具
 * @author catcancry
 **/
public class StClientUtil {

    private static Logger log = LoggerFactory.getLogger(StClientUtil.class);

    /**
     * key 对象,包含了一对被base64公私钥字符
     **/
    public static class Key{
        /**
         * 私钥
         **/
        private String privateKey;
        /**
         * 公钥
         **/
        private String publicKey;
        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public Key(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }
    /**
     * 生成一对RSA公私钥
     * @return Key 返回公私钥对
     **/
    public static Key createRSABase64Key(){
        RSA rsa = new RSA();
        log.info("privateKey:\n{}",rsa.getPrivateKeyBase64());
        log.info("publicKey:\n{}",rsa.getPublicKeyBase64());
        return new Key(rsa.getPrivateKeyBase64(),rsa.getPublicKeyBase64());
    }

    /**
     * 生成AES key
     * @return java.lang.String
     **/
    public static String createAESBase64Key() {
       return Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());
    }

    /**
     * 生成加密请求参数
     * @param RSAPublicKey 加密公钥
     * @param aesKey  随机aesKey
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, String aesKey, long t, String appId, String auth, Object data) {
        return encrypt( RSAPublicKey,  aesKey,  t,  appId,  auth, JSONUtil.toJsonStr(data));
    }
    /**
     * 生成加密请求参数
     * @param RSAPublicKey 加密公钥
     * @param aesKey  随机aesKey
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, String aesKey, long t, String appId, String auth, String data) {
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
                return String.valueOf(t);
            }
            @Override
            public String getKey() {
                return aesKey;
            }
        });
        StringBuffer keyDataBuffer = new StringBuffer(20);
        if(StrUtil.isBlankIfStr(aesKey)){
            StException.throwExec(4,"AesKey不能为空");
        }

        keyDataBuffer.append(aesKey)
                .append(StConst.SPLIT)
                .append(t)
                .append(StConst.SPLIT)
                .append(appId)
                .append(StConst.SPLIT)
                .append(auth);

        String keyData =  keyDataBuffer.toString();
        String key = SecureUtil.rsa(null,RSAPublicKey).encryptBase64(keyData,StConst.DEFAULT_CHARSET,KeyType.PublicKey);
        String sign = null;//由于使用的一把公私钥加密解密,因此加签就不需要了
        String encryptData = SecureUtil.aes(StrUtil.bytes(aesKey,StConst.DEFAULT_CHARSET)).encryptBase64(data,StConst.DEFAULT_CHARSET);
        return new StResquestBody( sign,  key,  encryptData);
    }




    /**
     * 生成加密请求参数(在线程不变的情况下可以使用这个方法，完成加密解密)
     * @param RSAPublicKey 加密公钥
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, long t, String appId, String auth, Object data) {
        return encrypt( RSAPublicKey, StClientUtil.createAESBase64Key(),  t,  appId,  auth, JSONUtil.toJsonStr(data));
    }

    /**
     * 生成加密请求参数(在线程不变的情况下可以使用这个方法，完成加密解密)
     * @param RSAPublicKey 加密公钥
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, long t, String appId, String auth, String data) {
        return encrypt( RSAPublicKey, StClientUtil.createAESBase64Key(),  t,  appId,  auth, data);
    }

    /**
     * 解密请求结果
     * @param publicKey rsa公钥
     * @param aesKey  解密AES key
     * @param stEncryptBody 解密内容
     * @return java.lang.String
     */
    public static  String dencrypt(String publicKey, String aesKey, StBody stEncryptBody){
        StAuthUtil.clearStAuth();
        String data = stEncryptBody.getData();
        String sign = stEncryptBody.getSign();
        byte[] decryptData = SecureUtil.aes(StrUtil.bytes(aesKey,StConst.DEFAULT_CHARSET)).decrypt(data);
        //签名原始内容
        boolean verify = SecureUtil.sign(SignAlgorithm.MD5withRSA,null,publicKey).verify (decryptData,HexUtil.decodeHex(sign));
        log.debug("验签结果：{}",verify);
        if(!verify){
            StException.throwExec(12,"验签失败");
        }
        return new String(decryptData);
    }


    /**
     * 解密请求结果(在线程不变的情况下可以使用这个方法，完成加密解密)
     * @param publicKey RSA 公钥
     * @param stEncryptBody 解密内容
     * @return java.lang.String
     */
    public static  String dencrypt(String publicKey, StBody stEncryptBody){
        return StClientUtil.dencrypt(publicKey, StAuthUtil.getStAuth().getKey(), stEncryptBody);
    }

}
