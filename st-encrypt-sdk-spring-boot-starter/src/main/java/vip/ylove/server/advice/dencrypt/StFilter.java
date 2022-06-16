package vip.ylove.server.advice.dencrypt;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(StFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StFilterWrapper stReq = null;
        if (request instanceof HttpServletRequest) {
            try {
                if (ServletFileUpload.isMultipartContent((HttpServletRequest) request)) {
                    stReq =  new StStandardMultipartHttpServletRequest((HttpServletRequest)request);
                }else{
                    stReq =  new StHttpServletRequestWrapper((HttpServletRequest)request);
                }
            }catch (Exception e){
                log.warn("StHttpServletRequestWrapper Error:", e);
            }
        }
        chain.doFilter((Objects.isNull(stReq) ? request : stReq), response);
    }
}