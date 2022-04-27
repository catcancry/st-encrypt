package vip.ylove.demo.client.common;

import vip.ylove.sdk.dto.StResponseBody;

public class EncryptResult extends StResponseBody implements Result {

    /**
     * 状态编码 参考BaseResultCode
     */
    int code = 0;
    /**
     * 结果描述
     */
    String msg = "成功";


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        if(code == 0){
            return true;
        }
        return false;
    }
}
