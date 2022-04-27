package vip.ylove.sdk.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.dto.StBody;
import vip.ylove.sdk.dto.StResquestBody;
import vip.ylove.sdk.exception.StException;

import java.util.concurrent.TimeUnit;

/**
 *  调用端加密请求参数和解密响应结果工具
 * @author catcancry
 **/
public class StClientUtil {

    private static Logger log = LoggerFactory.getLogger(StClientUtil.class);

    /**
     * 在控制台打印RSA 公私钥
     **/
    public static void createRSABase64Key(){
        RSA rsa = new RSA();
        log.info("privateKey:\n{}",rsa.getPrivateKeyBase64());
        log.info("publicKey:\n{}",rsa.getPublicKeyBase64());
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
     * @param AesKey  随机aesKey
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, String AesKey, long t, String appId, String auth, Object data) {
        return encrypt( RSAPublicKey,  AesKey,  t,  appId,  auth, JSONUtil.toJsonStr(data));
    }
    /**
     * 生成加密请求参数
     * @param RSAPublicKey 加密公钥
     * @param AesKey  随机aesKey
     * @param t       时间戳
     * @param appId   授权id 可以为空
     * @param auth    授权值 可以为空
     * @param data    加密内容
     * @return vip.ylove.sdk.dto.StResquestBody
     */
    public static StResquestBody encrypt(String RSAPublicKey, String AesKey, long t, String appId, String auth, String data) {
        StStopWatch stopWatch = new StStopWatch("客户端-加密信息");
        stopWatch.start("生成明文key");
        StringBuffer keyDataBuffer = new StringBuffer(20);
        if(StrUtil.isBlankIfStr(AesKey)){
            StException.throwExec(4,"AesKey不能为空");
        }

        keyDataBuffer.append(AesKey)
                .append(StConst.SPLIT)
                .append(t)
                .append(StConst.SPLIT)
                .append(appId)
                .append(StConst.SPLIT)
                .append(auth);

        String keyData =  keyDataBuffer.toString();
        stopWatch.stop();
        stopWatch.start("RSA加密key");
        String key = SecureUtil.rsa(null,RSAPublicKey).encryptBase64(keyData,KeyType.PublicKey);
        stopWatch.stop();
        stopWatch.start("RSA签名key");
        String sign =  StClientUtil.signMD5Hex(keyData);
        stopWatch.stop();
        stopWatch.start("AES加密内容");
        String encryptData = SecureUtil.aes(StrUtil.bytes(AesKey)).encryptBase64(data);
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
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
        return encrypt( RSAPublicKey, StKeyUtil.puKey(StClientUtil.createAESBase64Key()),  t,  appId,  auth, JSONUtil.toJsonStr(data));
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
        return encrypt( RSAPublicKey, StKeyUtil.puKey(StClientUtil.createAESBase64Key()),  t,  appId,  auth, data);
    }

    /**
     * 解密请求结果
     * @param publicKey rsa公钥
     * @param aesKey  解密AES key
     * @param stEncryptBody 解密内容
     * @return java.lang.String
     */
    public static  String dencrypt(String publicKey, String aesKey, StBody stEncryptBody){
        StStopWatch stopWatch = new StStopWatch("客户端-解密信息");
        String data = stEncryptBody.getData();
        String sign = stEncryptBody.getSign();

        stopWatch.start("AES解密内容");
        byte[] decryptData = SecureUtil.aes(StrUtil.bytes(aesKey)).decrypt(data);
        stopWatch.stop();
        stopWatch.start("验证签名");
        //签名原始内容s
        boolean verify = StClientUtil.signVerifyMD5(decryptData,sign);
        log.debug("验签结果：{}",verify);
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return new String(decryptData);
    }

    /**
     * 生成签名 使用简单signMD5Hex 保证效率
     * @param data 内容
     * @return java.lang.String
     */
    public static String signMD5Hex(String data){
        return DigestUtil.md5Hex(data);
    }

    /**
     * 生成签名 使用简单signMD5Hex 保证效率
     * @param data 内容
     * @return java.lang.String
     */
    public static String  signMD5Hex(byte[] data){
        return DigestUtil.md5Hex(data);
    }

    /**
     * 验证签名
     * @param data 内容
     * @param sign 签名
     * @return boolean
     */
    public static boolean  signVerifyMD5(byte[] data,String sign){
        return sign.equals(DigestUtil.md5Hex(data));
    }
    /**
     * 验证签名
     * @param data 内容
     * @param sign 签名
     * @return boolean
     */
    public static boolean  signVerifyMD5(String data,String sign){

        return sign.equals(DigestUtil.md5Hex(data));
    }

    /**
     * 解密请求结果(在线程不变的情况下可以使用这个方法，完成加密解密)
     * @param publicKey RSA 公钥
     * @param stEncryptBody 解密内容
     * @return java.lang.String
     */
    public static  String dencrypt(String publicKey, StBody stEncryptBody){
        return StClientUtil.dencrypt(publicKey,StKeyUtil.getKey(),stEncryptBody);
    }

}
