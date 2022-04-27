package vip.ylove.demo.client.common.exception;

/**
 * Created by liuruijie on 2017/1/14.
 * 自定义异常
 */
public class BaseException extends RuntimeException{

    private int code;//状态码

    public BaseException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static void throwExec(int code,String msg){
        throw new BaseException(msg, code);
    }
}
