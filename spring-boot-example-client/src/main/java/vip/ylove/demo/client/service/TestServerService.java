package vip.ylove.demo.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vip.ylove.demo.client.common.BaseResult;
import vip.ylove.demo.client.common.EncryptResult;


@Component
@FeignClient(name = "server",path = "/server",url = "http://127.0.0.1:8080")
public interface TestServerService {


    @PostMapping("/test/encrypt")
    public EncryptResult encrypt(@RequestBody Object form);


    @PostMapping("/test/noEncrypt")
    public BaseResult noEncrypt(Object form);
}
