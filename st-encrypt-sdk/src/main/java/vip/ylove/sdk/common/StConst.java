package vip.ylove.sdk.common;

import java.nio.charset.Charset;

public class StConst {

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
         * 默认模式 即对请求参数打包加密，再后端解开 适用于get、post等相关请求
         */
        public static int DEFAULT = 1;

    }

    public static class ErrorCode{

        public String a="1";

    }

}
