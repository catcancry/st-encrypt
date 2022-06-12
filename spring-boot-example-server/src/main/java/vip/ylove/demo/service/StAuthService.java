package vip.ylove.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;

@Slf4j
@Service
public class StAuthService implements StAbstractAuth {
    @Override
    public boolean auth(String aesKey, String appId, String auth, String t, StEncrypt stEncrypt) {
        //模拟进行授权验证
        log.info("自定义appId[{}]-auth[{}]-t[{}]-stEncrypt[{}]",appId, auth,t,stEncrypt);
        if ("123456".equals(appId) && "123456".equals(auth)) {
            return true;
        }
        log.debug("授权验证未通过");
        return false;
    }

    @Override
    public String key() {
       String key =  StAbstractAuth.super.key();
       //若没有获取到动态key,则可以在此处获取静态key,例如token,jwt中等
        return key;
    }
}
