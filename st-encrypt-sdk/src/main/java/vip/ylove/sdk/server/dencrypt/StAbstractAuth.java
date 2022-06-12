package vip.ylove.sdk.server.dencrypt;

import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.util.StAuthUtil;

/**
 * 服务端-鉴权接口，可以完成简单的鉴权，判断接口是否可以被访问
 **/
public interface StAbstractAuth {

    /**
     * 验证是否可以调用
     *
     * @param aesKey 数据加密key
     * @param appId  appId
     * @param auth   授权码
     * @param t 时间戳 也可以是其他信息扩展信息
     * @param stEncrypt 补充信息
     * @return boolean
     **/
    default public boolean auth(String aesKey, String appId, String auth, String t, StEncrypt stEncrypt) {
        return true;
    }

    /**
     * 获取key 该方法可以将前端加密key存在后端，减少每次加密传输key加大请求负担
     * 默认情况下每次请求都传递不同的加密key,但在部分场景下也可以在登陆后，将key存在后端
     * @return
     */
    default public String key(){
        if(StAuthUtil.getStAuth() == null || StAuthUtil.getStAuth().getKey() == null){
           return null;
        }
        return StAuthUtil.getStAuth().getKey();
    }

}
