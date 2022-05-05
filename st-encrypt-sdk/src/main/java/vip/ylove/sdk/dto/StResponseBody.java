package vip.ylove.sdk.dto;

import cn.hutool.core.util.StrUtil;

/**
 * 响应的加密结果
 * @author catcancry
 **/
public class StResponseBody implements StBody {
    /**
     * 签名加密内容
     */
    String sign;
    /**
     * AES 加密（data数据）
     */
    String data;

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    public StResponseBody() {
    }

    public StResponseBody(String sign, String data) {
        this.sign = sign;
        this.data = data;
    }

    @Override
    public String toString() {
        return "StResponseBody{" + "sign='" + sign + '\'' + ", data='" + data + '\'' + '}';
    }

    @Override
    public boolean isSuccess() {
        if(StrUtil.isBlankIfStr(sign)  || StrUtil.isBlankIfStr(this.data) ){
            return false;
        }else{
            return true;
        }
    }
}
