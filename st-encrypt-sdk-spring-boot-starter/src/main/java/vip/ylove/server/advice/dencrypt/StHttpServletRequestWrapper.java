package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.io.IoUtil;
import vip.ylove.sdk.common.StConst;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StHttpServletRequestWrapper extends HttpServletRequestWrapper implements StFilterWrapper {


    private Map<String, String[]> params = new HashMap<>();

    // 保存request body的数据
    private byte[] body;

    // 解析request的inputStream(即body)数据，转成字符串
    public StHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        //先获取参数，然后再获取流
        this.params.putAll(request.getParameterMap());
        try(InputStream inputStream  = request.getInputStream()){
            this.body  = IoUtil.readBytes(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector(params.keySet()).elements();
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

    /**
     * 获取body
     * @return  body
     **/
    public byte[] getBody() {
        return this.body;
    }
    /**
     * 设置负载
     * @param   body 设置body
     **/
    public void setBody(byte[] body) {
        this.body = body;
    }


    /**
     * 移除加密参数
     **/
    public void removeStEncryptParams() {
        params.remove(StConst.KEY);
        params.remove(StConst.DATA);
        params.remove(StConst.SIGN);
    }

    /**
     * 将解密后的参数回填
     * @param otherParams 其他参数
     **/
    public void addParameters(Map<String, Object> otherParams) {
        if(otherParams != null){
            for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
                if( entry.getValue() != null){
                    if (entry.getValue() instanceof String[]) {
                        params.put(entry.getKey(), (String[]) entry.getValue());
                    } else if (entry.getValue() instanceof String) {
                        params.put(entry.getKey(), new String[]{(String) entry.getValue()});
                    } else {
                        params.put(entry.getKey(), new String[]{String.valueOf(entry.getValue())});
                    }
                }
            }
        }
    }
}