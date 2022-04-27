package vip.ylove.demo.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.ylove.annotation.StEncrypt;
import vip.ylove.config.StConfig;
import vip.ylove.demo.common.BaseResult;
import vip.ylove.sdk.util.StClientUtil;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestRest {

    @Autowired
    private StConfig stConfig;

    /**
     * 不使用加密
     * @param form
     * @return
     */
    @PostMapping("/noEncrypt")
    public BaseResult noEncrypt(@RequestBody Object form){
        return BaseResult.success_(form);
    }

    /**
     * 加密的请求
     * @param form
     * @return
     */
    @StEncrypt
    @PostMapping("/encrypt")
    public BaseResult encrypt(@RequestBody Object form){
        log.info("服务器收到请求参数:{}",form);
        return BaseResult.success_(form);
    }

    /**
     * 生成公私钥，在控制台看
     * @param form
     * @return
     */
    @PostMapping("/createRSABase64Key")
    public Object createRSABase64Key(@RequestBody Object form){
        StClientUtil.createRSABase64Key();
        return BaseResult.success_();
    }


}
