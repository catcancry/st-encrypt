package vip.ylove.server.advice.encrypt;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;
import vip.ylove.sdk.exception.StException;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author:Bobby
 * DateTime:2019/4/9
 **/
public class DecryptHttpInputMessage implements HttpInputMessage{

    private static final Logger log = LoggerFactory.getLogger(DecryptHttpInputMessage.class);

    private HttpHeaders headers;
    private InputStream body;


    public DecryptHttpInputMessage(String RSAKey, StAbstractRequestDencrypt stDencrypt , StAbstractAuth stAbstractAuth, HttpInputMessage inputMessage, String privateKey) {
        StopWatch stopWatch = new StopWatch("请求参数解密");
        if (StrUtil.isBlankIfStr(privateKey)) {
            StException.throwExec(3,"privateKey is null");
        }
        stopWatch.start("读取信息时长");
        this.headers = inputMessage.getHeaders();

        String content = null;
        try {
            content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            StException.throwExec(7,"从body中获取信息失败");
        }
        stopWatch.stop();
        stopWatch.start("解密内容");
        byte[]  bodyData = stDencrypt.dencrypt(RSAKey,content,stAbstractAuth);
        stopWatch.stop();
        stopWatch.start("转换为原始对象");
        this.body = new ByteArrayInputStream(bodyData);
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Override
    public InputStream getBody(){
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
