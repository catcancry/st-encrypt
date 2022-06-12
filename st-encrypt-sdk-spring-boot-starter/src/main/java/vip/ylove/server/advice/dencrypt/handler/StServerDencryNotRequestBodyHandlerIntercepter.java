package vip.ylove.server.advice.dencrypt.handler;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import vip.ylove.config.StConfig;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.annotation.StEncryptSkip;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;
import vip.ylove.sdk.util.StServerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class StServerDencryNotRequestBodyHandlerIntercepter implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StAbstractRequestDencrypt stDencrypt;
    @Autowired
    private StConfig stConfig;
    @Autowired
    private StAbstractAuth stAuth ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        //判断是否需要解密参数
        StEncrypt se = handlerMethod.getMethodAnnotation(StEncrypt.class);
        if (se != null ) {
            if(se.req()){
                //需要进行解密
                pushUserInfo2Body(se,request, handlerMethod);
                return true;
            }
        }else  if(stConfig.isEnableGlobalEncrypt()){ //开启全局验证
            if (!handlerMethod.getMethod().isAnnotationPresent(StEncryptSkip.class) ){ //是否跳过方法
                //需要进行解密
                pushUserInfo2Body(se,request, handlerMethod);
                return true;
            }
        }
        return true;
    }

    private void pushUserInfo2Body(StEncrypt stEncrypt,HttpServletRequest request, HandlerMethod handlerMethod) {
        StHttpServletRequestWrapper requestWrapper = (StHttpServletRequestWrapper)request;
        try{
            String ct = request.getContentType();
            System.out.println(ct);
            //get请求处理
            if("GET".equals(request.getMethod())){
                String key = request.getParameter(StConst.KEY);
                String data = request.getParameter(StConst.DATA);
                //移除参数中的StEncryptParams
                requestWrapper.removeStEncryptParams();
                byte[] dencrypt = StServerUtil.dencrypt(stConfig.getPrivateKey(), key, data, stEncrypt, stAuth);
                Map<String,Object> params = JSONUtil.toBean(new String(dencrypt, StConst.DEFAULT_CHARSET),Map.class);
                requestWrapper.addParameters(params);
            }else{
                //application/json
                if(ct.startsWith(MediaType.APPLICATION_JSON_VALUE)){
                    String key = request.getParameter(StConst.KEY);
                    String data = request.getParameter(StConst.DATA);
                }else if(ct.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)){
                    requestWrapper.addParameter("add", "add");
                    String body = requestWrapper.getBody();
                    if(body != null){
                        String[] ps = body.split("&");
                        if(ps != null){
                            String key = null;
                            String data = null;
                            for (int i = 0; i < ps.length; i++) {
                                String[] p = ps[i].split("=");
                                if(StConst.KEY.equals(p[0])){
                                    key = p[1];
                                }else if(StConst.DATA.equals(p[0])){
                                    data = p[1];
                                }
                            }
                            byte[] dencrypt = StServerUtil.dencrypt(stConfig.getPrivateKey(), key, data, stEncrypt, stAuth);
                            Map<String,Object> params = JSONUtil.toBean(new String(dencrypt, StConst.DEFAULT_CHARSET),Map.class);
                            StringBuffer buffer = new StringBuffer();
                            params.entrySet().forEach(e -> {
                                buffer.append(e.getKey())
                                        .append("=")
                                        .append(e.getValue())
                                        .append("&");
                            });
                            if(buffer.length() > 0){
                                buffer.substring(0,buffer.length() - 1);
                            }
                            System.out.println(buffer.toString());
                        }
                    }
                    byte[] dencrypt = StServerUtil.dencrypt(stConfig.getPrivateKey(), body, stEncrypt, stAuth);
                    System.out.println(new String(dencrypt, StConst.DEFAULT_CHARSET));
                }
            }
        }catch (Exception e){
          e.printStackTrace();
        }
    }

}
