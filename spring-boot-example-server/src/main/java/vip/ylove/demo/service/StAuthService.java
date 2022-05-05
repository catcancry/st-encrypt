package vip.ylove.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;

@Slf4j
@Service
public class StAuthService implements StAbstractAuth {
    @Override
    public boolean auth(String appId, String auth, String t,Object obj) {
        //从这里这里可以获取到请求相关信息
        MethodParameter parameter = (MethodParameter)obj;
        //模拟进行授权验证
        log.info("appId:{}--auth:{}", appId, auth);
        if ("123456".equals(appId) && "123456".equals(auth)) {
            return true;
        }
        log.debug("授权验证未通过");
        return false;
    }
}
