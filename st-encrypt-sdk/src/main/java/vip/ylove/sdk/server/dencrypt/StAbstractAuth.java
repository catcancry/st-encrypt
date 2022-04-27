package vip.ylove.sdk.server.dencrypt;

/**
 * 服务端-鉴权接口，可以完成简单的鉴权，判断接口是否可以被访问
 * @author catcancry
 **/
public interface StAbstractAuth {

    /**
     * 验证是否可以调用
     * @param appId appId
     * @param auth 授权码
     * @param t 时间戳
     * @return boolean
     **/
    boolean auth(final  String appId,final String auth,final long t);

}
