package vip.ylove.sdk.common;

/**
 *
 */
public enum  StErrorCode {

    error2(2, "认证未通过"),
    error11(11, "按照规定格式进行加密"),
    error12(12,"请按照正确的协议进行参数加密解密，预测没有参入解密key");
    /**
     * 不支持的请求头类型
     **/
    public final static int NOT_SUPPORT_CONTENT_TYPE = 15;

    int code;

    String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    StErrorCode(int code, String error) {
        this.code = code;
        this.error = error;
    }
}
