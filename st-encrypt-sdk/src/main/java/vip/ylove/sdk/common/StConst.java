package vip.ylove.sdk.common;

import java.nio.charset.Charset;

public class StConst {

    /**
     * 签名保存的参数名称
     */
    public static String SIGN ="sign";
    /**
     * 加密key保存的参数名称
     */
    public static String KEY ="key";
    /**
     * 加密数据保存参数名称
     */
    public static String DATA ="data";

    /**
     *  文件上传加密模式
     */
    public static String UPLOAD_MODE ="st_upload_mode";

    /**
     * 加密分隔
     */
    public static String SPLIT = "###";

    /**
     * 默认编码格式
     */
    public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 加密模式
     */
    public static class enMode{

        /**
         * 默认模式 即对请求参数打包加密.对文件只添加md5
         */
        public static String DEFAULT = "1";

    }

    public static class ErrorCode{

        public String a="1";

    }

}
