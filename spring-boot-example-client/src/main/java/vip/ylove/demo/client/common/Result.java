package vip.ylove.demo.client.common;

import vip.ylove.sdk.dto.StResult;

public interface Result extends StResult{

    public int getCode();

    public String getMsg();

    public boolean isSuccess();
}
