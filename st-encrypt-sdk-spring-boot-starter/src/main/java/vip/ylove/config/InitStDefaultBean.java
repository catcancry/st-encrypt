package vip.ylove.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vip.ylove.sdk.json.StAbstractJsonDcode;
import vip.ylove.sdk.json.StDefaultJsonCode;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;
import vip.ylove.sdk.server.encrypt.StAbstractResponseEncrypt;

@Component
public class InitStDefaultBean {

    private static final Logger log = LoggerFactory.getLogger(InitStDefaultBean.class);

    @Bean
    @ConditionalOnMissingBean(StAbstractRequestDencrypt.class)
    public StAbstractRequestDencrypt StAbstractDencryptInitDefault(){
        log.debug("没有自定义StAbstractRequestDencrypt,使用默认设置");
        return new StAbstractRequestDencrypt() {};
    }

    @Bean
    @ConditionalOnMissingBean(StAbstractResponseEncrypt.class)
    public StAbstractResponseEncrypt StAbstractEncryptInitDefault(){
        log.debug("没有自定义StAbstractResponseEncrypt,使用默认设置");
        return new StAbstractResponseEncrypt(){};
    }

    @Bean
    @ConditionalOnMissingBean(StAbstractAuth.class)
    public StAbstractAuth StAbstractAuthInitDefault(){
        log.debug("没有自定义StAbstractAuth,使用默认设置");
        return new StAbstractAuth() { };
    }

    @Bean
    @ConditionalOnMissingBean(StAbstractJsonDcode.class)
    public StAbstractJsonDcode StAbstractJsonInitDefault(){
        log.debug("StAbstractJson,使用默认设置");
        return new StDefaultJsonCode();
    }
    
}
