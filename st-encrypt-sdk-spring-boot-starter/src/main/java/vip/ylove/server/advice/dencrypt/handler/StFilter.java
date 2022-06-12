package vip.ylove.server.advice.dencrypt.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ylove.server.advice.dencrypt.DecryptHttpInputMessage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

public class StFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(DecryptHttpInputMessage.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StHttpServletRequestWrapper customHttpServletRequestWrapper = null;
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            customHttpServletRequestWrapper = new StHttpServletRequestWrapper(req);
        }catch (Exception e){
            log.warn("customHttpServletRequestWrapper Error:", e);
        }
        chain.doFilter((Objects.isNull(customHttpServletRequestWrapper) ? request : customHttpServletRequestWrapper), response);
    }
}