package vip.ylove.server.advice.dencrypt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.FileUploadUtils;

import java.io.IOException;
import java.util.Objects;

public class StFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(StFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StFilterWrapper stReq = null;
        if (request instanceof HttpServletRequest) {
            try {
                if (FileUploadUtils.isMultipartContent((HttpServletRequest) request)) {
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