package vip.ylove.demo.client.common;

import lombok.Data;
import vip.ylove.sdk.dto.StResponseBody;

@Data
public class EncryptResult extends StResponseBody implements Result {

    /**
     * 状态编码 参考BaseResultCode
     */
    int code = 0;
    /**
     * 结果描述
     */
    String msg = "成功";

    public boolean isSuccess(){
        if(code == 0){
            return true;
        }
        return false;
    }
}
