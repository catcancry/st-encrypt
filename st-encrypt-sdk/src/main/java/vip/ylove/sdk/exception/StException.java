package vip.ylove.sdk.exception;

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

    public class ErrorCode{

        /**
         * 未获取到数据加密解密的key
         */
        public final static int NOT_GET_KEY = 31;


    }

}
