package vip.ylove.server.advice.dencrypt.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

public class StFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(StFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StHttpServletRequestWrapper customHttpServletRequestWrapper = null;
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            customHttpServletRequestWrapper = new StHttpServletRequestWrapper(req);
        }catch (Exception e){
            log.warn("StHttpServletRequestWrapper Error:", e);
        }
        chain.doFilter((Objects.isNull(customHttpServletRequestWrapper) ? request : customHttpServletRequestWrapper), response);
    }
}