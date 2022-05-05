package vip.ylove.sdk.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.sdk.common.StAuthInfo;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.dto.StResponseBody;
import vip.ylove.sdk.dto.StResquestBody;
import vip.ylove.sdk.exception.StException;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;

import java.util.concurrent.TimeUnit;

/**
 * 服务端解密请求参数和加密响应结果工具
 * @author catcancry
 **/
public class StServerUtil {

    private static Logger log = LoggerFactory.getLogger(StServerUtil.class);

    /**
     * 生成RSA公私钥，并在控制台输出
     **/
    public static void createRSABase64Key(){
        RSA rsa = new RSA();
        log.info("privateKey:\n{}",rsa.getPrivateKeyBase64());
        log.info("publicKey:\n{}",rsa.getPublicKeyBase64());
    }

    /**
     * 生成aes的key
     * @return java.lang.String
     **/
    public static String createAESBase64Key() {
       return Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());
    }

    /**
     * 加密响应结果(在线程不变的情况下可以使用这个方法，完成加密解密)
     * @param privateKey rsa私钥
     * @param content 加密内容
     * @return vip.ylove.sdk.dto.StResponseBody
     **/
    public static StResponseBody encrypt(String privateKey,String  content) {
        StResponseBody encrypt = encrypt(privateKey,StKeyUtil.getKey(), content);
        StKeyUtil.remove();
        return encrypt;
    }

    /**
     * 加密响应结果
     * @param privateKey rsa私钥
     * @param aesKey aesKey
     * @param content 加密内容
     * @return vip.ylove.sdk.dto.StResponseBody
     **/
    public static StResponseBody encrypt(String privateKey,String aesKey,String content) {
        StKeyUtil.remove();
        StAuthUtil.auth.remove();
        StopWatch stopWatch = new StopWatch("加密对象");
        stopWatch.start("对象转换为byte[]");
        byte [] dataByte = StrUtil.bytes(content);
        stopWatch.stop();
        stopWatch.start("AES加密");
        String encryptData = SecureUtil.aes(StrUtil.bytes(aesKey)).encryptBase64(dataByte);
        stopWatch.stop();
        stopWatch.start("签名内容");
        //签名原始内容
        String sign = SecureUtil.sign(SignAlgorithm.MD5withRSA,privateKey,null).signHex(dataByte);
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return new StResponseBody(sign,encryptData);
    }




    /**
     * 解密请求参数
     * @param privateKey rsa私钥
     * @param content    加密内容
     * @param obj        扩展信息
     * @param stAuth     鉴权接口
     * @return java.lang.Object
     **/
    public static  byte[] dencrypt(String privateKey, String content,Object obj, StAbstractAuth stAuth){
        StopWatch stopWatch = new StopWatch("参数解密");
        stopWatch.start("将入参转换为StDencryptBody对象");
        StResquestBody dencryptBody = JSONUtil.toBean(content, StResquestBody.class);
        stopWatch.stop();
        if(StrUtil.isBlankIfStr(dencryptBody.getSign())){
            StException.throwExec(6,"sign不能为空");
        }
        if(StrUtil.isBlankIfStr(dencryptBody.getKey())){
            StException.throwExec(6,"key不能为空");
        }
        if(StrUtil.isBlankIfStr(dencryptBody.getData())){
            StException.throwExec(6,"data不能为空");
        }

        /**
         * 使用私钥解密
         */
        stopWatch.start("RSA私钥解密key");
        byte[] decryptKey =  SecureUtil.rsa(privateKey,null).decrypt(dencryptBody.getKey(),KeyType.PrivateKey);
        String keyData = new String(decryptKey);
        stopWatch.stop();
        //验签
        stopWatch.start("验签");
        try {
            boolean verify = signVerifyMD5(keyData,dencryptBody.getSign());
            if(!verify){
                StException.throwExec(1,"签名验证失败，请求内容可能被篡改");
            }
        } catch (Exception e) {
            StException.throwExec(8,"签名验证失败");
        }
        stopWatch.stop();

        String[] split = keyData.split(StConst.SPLIT);
        if(split.length == 4){
            String aesKey = split[0];
            String t = split[1];
            String appId = split[2];
            String auth = split[3];

            stopWatch.start("鉴权验证");
            //调用接口进行权限验证,若没有实现则默认跳过验证
            if(stAuth != null){
                boolean authResult = stAuth.auth(appId, auth,t,obj);
                if(!authResult){
                    StException.throwExec(2,"认证未通过");
                    return null;
                }
            }else{
                log.debug("未实现StAuth权限验证，跳过验证");
            }
            //存储授权信息
            StAuthUtil.auth.set(new StAuthInfo(appId,auth,t));

            stopWatch.stop();

            stopWatch.start("AES解密body");
            //解密body
            byte[] decryptData = SecureUtil.aes(StrUtil.bytes(aesKey)).decrypt(dencryptBody.getData());
            stopWatch.stop();
            //并保存key,用于加密响应结果
            StKeyUtil.puKey(aesKey);
            log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

            return decryptData;

        }
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        StException.throwExec(11,"按照规定格式进行加密");
        return null;
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
}
