package vip.ylove.demo.rest;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.ylove.config.StConfig;
import vip.ylove.demo.common.BaseResult;
import vip.ylove.demo.rest.form.TestForm;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.util.StClientUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestRest {

    @Autowired
    private StConfig stConfig;

    /**
     * 不使用加密
     *
     * @param form
     * @return
     */
    @PostMapping("/noEncrypt")
    public BaseResult noEncrypt(@RequestBody TestForm form) {
        log.info("服务器收到post json请求参数:{}", form);
        return BaseResult.success_(form);
    }


    /**
     * 不使用加密
     *
     * @param form
     * @return
     */
    @RequestMapping("/noEncrypt2")
    public BaseResult noEncrypt2(TestForm form, MultipartFile f) {
        log.info("服务器收到表单或者get请求参数:{}", form);
        if (f != null) {
            String name = new Date().getTime() +"-"+ f.getOriginalFilename();
            File dir = new File("/home/st-test");
            if(dir == null|| !dir.exists()){
                dir.mkdirs();
            }
            try (InputStream inputStream = f.getInputStream(); FileOutputStream fileOutputStream = new FileOutputStream(new File("/home/st-test/"+name))) {
                IoUtil.copy(inputStream, fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return BaseResult.success_(form);
    }


    /**
     * 加密的请求
     * StAuthInfo auth 获取到具体的授权向信息，进行相应的权限验证，也可以使用StAuthUtil,auth获取
     *
     * @param form
     * @return
     */
    @StEncrypt(data = "login")
    @PostMapping("/encrypt")
    public BaseResult encrypt(@RequestBody TestForm form) {
        log.info("服务器收到post json请求参数:{}", form);
        return BaseResult.success_(form);
    }

    /**
     * 加密的请求
     * StAuthInfo auth 获取到具体的授权向信息，进行相应的权限验证，也可以使用StAuthUtil,auth获取
     *
     * @param
     * @return
     */
    @StEncrypt(data = "login")
    @RequestMapping("/encrypt2")
    public BaseResult encrypt2(TestForm form, MultipartFile f) {
        return this.noEncrypt2(form,f);
    }

    /**
     * 生成公私钥，在控制台看
     *
     * @param form
     * @return
     */
    @PostMapping("/createRSABase64Key")
    public Object createRSABase64Key(@RequestBody Object form) {
        return BaseResult.success_("生成了一对base64编码的RSA公私钥",StClientUtil.createRSABase64Key());
    }


}
