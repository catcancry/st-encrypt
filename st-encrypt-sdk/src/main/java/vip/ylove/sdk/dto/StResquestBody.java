package vip.ylove.sdk.dto;

/**
 * 请求参数加密结果
 * @author catcancry
 * @date 2022/4/26 14:22
 **/
public class StResquestBody {

    /**
     * 签名加密内容
     */
    String sign;
    /**
     * RSA公钥加密（随机AES的KEY+时间搓+appId+授权auth）
     */
    String key;
    /**
     * AES 加密（data数据）
     */
    String data;

    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }

    public StResquestBody() {
    }

    public StResquestBody(String sign, String key, String data) {
        this.sign = sign;
        this.key = key;
        this.data = data;
    }

}
