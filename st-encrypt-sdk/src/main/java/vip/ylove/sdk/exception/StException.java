package vip.ylove.sdk.exception;

import vip.ylove.sdk.common.StErrorCode;

public class StException extends   RuntimeException{

    private int code;//状态码

    public StException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static void throwExec(int code,String msg){
        throw new StException(msg, code);
    }

    public static void throwExec(StErrorCode errorCode){
        throw new StException(errorCode.getError(), errorCode.getCode());
    }

    public class ErrorCode{

        /**
         * 不支持的请求头类型
         **/
        public final static int NOT_SUPPORT_CONTENT_TYPE = 15;

        /**
         * 未获取到数据加密解密的key
         */
        public final static int NOT_GET_KEY = 31;

        /**
         * 未知的錯誤
         */
        public final static int REQUEST_DENCRYPT_UNKNOWN_ERROR = 33;
        /**
         *  上传文件md5验证失败
         */
        public static final int UPLOAD_FILE_MD5_VERIFICATION_ERROR = 36;
    }

}
