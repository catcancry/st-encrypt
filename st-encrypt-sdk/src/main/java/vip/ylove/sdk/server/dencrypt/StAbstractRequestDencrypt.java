package vip.ylove.sdk.server.dencrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.exception.StException;
import vip.ylove.sdk.util.StAuthUtil;
import vip.ylove.sdk.util.StServerUtil;

import java.io.InputStream;
import java.util.Map;

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
     * @return byte[]
     */
    default public byte[] dencrypt(final String privateKey,final String content,final StEncrypt stEncrypt,final StAbstractAuth stAuth) {
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
     * @return 解密内容
     */
    default public byte[] dencrypt(final String privateKey,final String encryptKey,final String content,final StEncrypt stEncrypt,final StAbstractAuth stAuth) {
        return StServerUtil.dencrypt(privateKey,encryptKey,content,stEncrypt,stAuth);
    }

   /**
    *  文件上传模式1，验证文件方式 默认使用md5
    * @param fileName 文件名称
    * @param in 文件输入流已经自动关闭
    * @param params 表单其他信息
    **/
    default public void dencryptFile(String fileName,InputStream in, Map<String,Object> params){
        if (params == null) {
            StException.throwExec(StException.ErrorCode.UPLOAD_FILE_MD5_VERIFICATION_ERROR, "文件[" + fileName + "]可能被篡改,在" + StConst.DATA + "加密数据中未发现上传文件的md5的参数");
        }
        //获取文件md5
        String fileMD5 = DigestUtil.md5Hex(in);
        if (params.get(fileMD5) == null) {
            StException.throwExec(StException.ErrorCode.UPLOAD_FILE_MD5_VERIFICATION_ERROR, "文件[" + fileName + "]可能被篡改,在" + StConst.DATA + "加密数据中未发现上传文件的md5[" + fileMD5 + "]的参数");
        }
    }
    /**
     *  文件上传模式2，验证文件方式 默认先用md5签名原文件,再使用base64对文件编码，最后使用aes加密内容
     * @param fileName 文件名称
     * @param enFileStr 文件加密密文
     * @param params 表单其他信息
     * @return 返回解密的文件内容
     **/
    default byte[]  dencryptFile(String fileName,String enFileStr, Map<String,Object> params){
        if (params == null) {
            StException.throwExec(StException.ErrorCode.UPLOAD_FILE_MD5_VERIFICATION_ERROR, "文件[" + fileName + "]可能被篡改,在" + StConst.DATA + "加密数据中未发现上传文件的md5的参数");
        }
        String aesKey = StAuthUtil.getStAuth().getKey();
        byte[] fileDecrypt = SecureUtil.aes(StrUtil.bytes(aesKey, StConst.DEFAULT_CHARSET)).decrypt(enFileStr);
        byte[] fileO = Base64.decode(fileDecrypt);
        //验证文件md5值
        String fileMD5 = DigestUtil.md5Hex(fileO);
        if (params.get(fileMD5) == null) {
            StException.throwExec(StException.ErrorCode.UPLOAD_FILE_MD5_VERIFICATION_ERROR, "文件[" + fileName + "]可能被篡改,在" + StConst.DATA + "加密数据中未发现上传文件的md5[" + fileMD5 + "]的参数");
        }
        return fileO;
    }
}
