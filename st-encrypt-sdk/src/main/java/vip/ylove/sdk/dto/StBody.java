package vip.ylove.sdk.dto;

/**
 * 通用响应数据接口
 * @author catcancry
 **/
public interface StBody extends StResult{

    default public String getSign() {
        return null;
    }

    default public void setSign(String sign) {}

    default public String getKey() {
        return null;
    }

    default public void setKey(String key) {}

    default public String getData() {
        return null;
    }

    default public void setData(String data) {}

}
