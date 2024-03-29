package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import vip.ylove.sdk.common.StConst;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StStandardMultipartHttpServletRequest extends StandardMultipartHttpServletRequest implements StFilterWrapper {


    private Map<String, String[]> params = new HashMap<>();

    // 保存request body的数据
    private byte[] body;

    // 解析request的inputStream(即body)数据，转成字符串
    public StStandardMultipartHttpServletRequest(HttpServletRequest request) {
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

    public String getParameterValue(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /**
     * 获取body
     * @return body字节
     **/
    public byte[] getBody() {
        return this.body;
    }
    /**
     * 设置负载
     * @param body body内容
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

    public static class StandardMultipartFile implements MultipartFile, Serializable {


        String originalFilename;

        String contentType;

        byte[] bytes ;

        public StandardMultipartFile(String originalFilename, String contentType, byte[] bytes) {
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.bytes = bytes;
        }

        public String getName() {
            return this.getName();
        }

        public String getOriginalFilename() {
            return this.originalFilename;
        }

        public String getContentType() {
            return this.contentType;
        }

        public boolean isEmpty () {
            return bytes.length == 0;
        }

        public long getSize () {
            return bytes.length;
        }

        public byte[] getBytes() throws IOException {
            return this.bytes;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo (File destination) throws IOException {
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(destination);
                outputStream.write(bytes);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }


}