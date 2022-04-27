package vip.ylove.sdk.server.encrypt;

import vip.ylove.sdk.util.StServerUtil;

/**
 * 服务端-响应加密接口默认实现
 * @author catcancry
 **/
public class StDefaultResponseEncrypt implements StAbstractResponseEncrypt {

    @Override
    public Object encrypt(final  String AesKey,final String content) {
        return StServerUtil.encrypt(AesKey,content);
    }
}
