package vip.ylove.demo.client.rest;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.ylove.demo.client.common.BaseResult;
import vip.ylove.demo.client.common.EncryptResult;
import vip.ylove.demo.client.common.Result;
import vip.ylove.demo.client.config.StEncryptConfig;
import vip.ylove.demo.client.service.TestServerService;
import vip.ylove.sdk.dto.StResquestBody;
import vip.ylove.sdk.util.StClientUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestClientRest {

    @Autowired
    private StEncryptConfig stConfig;

    @Autowired
    private TestServerService serverService;

    /**
     * 不使用加密
     * @param form
     * @return
     */
    @PostMapping("/noEncrypt")
    public BaseResult noEncrypt(@RequestBody Object form){
        BaseResult result = serverService.noEncrypt(form);
        return result;
    }

    /**
     * 去调用服务器端
     * @param form
     * @return
     */
    @PostMapping("/encrypt")
    public Object test(@RequestBody Object form){
        StopWatch stopWatch = new StopWatch("第三方调用加密服务");
        log.info("{}",form);
        stopWatch.start("加密请求参数");
        //加密请求参数
        StResquestBody encrypt = StClientUtil.encrypt(stConfig.getPublicKey(),
                                                System.currentTimeMillis(),
                                                stConfig.getAppId(),
                                                stConfig.getAppAuth(), form);
        stopWatch.stop();
        stopWatch.start("发送请求");
        //发送请求
        EncryptResult stEncryptBody = serverService.encrypt(encrypt);
        stopWatch.stop();
        stopWatch.start("解密响应结果");
        Object result = null;
        if( stEncryptBody.isSuccess()){
            //解密数据
             result = StClientUtil.dencrypt(stConfig.getPublicKey(), stEncryptBody);
        }else{
            result = stEncryptBody;
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MICROSECONDS));
        return result;
    }


}
