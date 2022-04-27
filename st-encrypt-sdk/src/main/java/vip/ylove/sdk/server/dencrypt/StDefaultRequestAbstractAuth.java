package vip.ylove.sdk.server.dencrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  服务端-默认实现鉴权接口
 * @author catcancry
 **/
public class StDefaultRequestAbstractAuth implements StAbstractAuth {

    private static final Logger log = LoggerFactory.getLogger(StDefaultRequestAbstractAuth.class);

    @Override
    public boolean auth(String appId, String auth, long t) {
        log.debug("默认认证方式:appId[{}]-auth[{}]-t[{}]",appId, auth,t);
        return true;
    }
}
