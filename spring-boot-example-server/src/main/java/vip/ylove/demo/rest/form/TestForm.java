package vip.ylove.demo.rest.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class TestForm {

    String t;

    String appId;

    String auth;

    String test;

    String data;

    Date now = new Date();

    @JsonFormat(pattern = "yyyy年MM月dd日")
    @JsonProperty("dddddddddddddddd")
    Date now2 = new Date();
}
