package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.exception.StException;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author catcancry
 **/
public class DecryptHttpInputMessage implements HttpInputMessage{

    private static final Logger log = LoggerFactory.getLogger(DecryptHttpInputMessage.class);

    private HttpHeaders headers;
    private InputStream body;


    public DecryptHttpInputMessage(StEncrypt stEncrypt, String RSAKey, StAbstractRequestDencrypt stDencrypt , StAbstractAuth stAbstractAuth, HttpInputMessage inputMessage, String privateKey) {
        if (StrUtil.isBlankIfStr(privateKey)) {
            StException.throwExec(3,"privateKey is null");
        }
        this.headers = inputMessage.getHeaders();
        log.debug("当前请求类型contentType:{}",this.headers.getContentType());
        //仅仅支持APPLICATION_JSON_VALUE
        if(this.headers.getContentType() != null && this.headers.getContentType().toString().startsWith(MediaType.APPLICATION_JSON_VALUE) ){
            String content = null;
            try {
                content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                StException.throwExec(7,"从body中获取信息失败");
            }
            byte[]  bodyData = stDencrypt.dencrypt(RSAKey,content,stEncrypt,stAbstractAuth);
            this.body = new ByteArrayInputStream(bodyData);
        }else{
            log.error("当前请求类型contentType:{},不支持的加密头，仅仅支持-{}",this.headers.getContentType(),MediaType.APPLICATION_JSON_VALUE);
            StException.throwExec(15,"不支持的请求方式，contentType必须为application/json");
        }
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
