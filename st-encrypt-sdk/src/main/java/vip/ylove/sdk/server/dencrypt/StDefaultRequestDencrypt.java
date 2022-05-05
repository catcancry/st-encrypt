package vip.ylove.sdk.server.dencrypt;


import vip.ylove.sdk.util.StServerUtil;

/**
 *  服务端-默认请求参数解密接口实现
 * @author catcancry
 **/
public class StDefaultRequestDencrypt implements StAbstractRequestDencrypt {

    @Override
    public byte[] dencrypt(String RSAKey, String content,Object obj,StAbstractAuth stAuth) {
        return StServerUtil.dencrypt(RSAKey,content,obj,stAuth);
    }
}
