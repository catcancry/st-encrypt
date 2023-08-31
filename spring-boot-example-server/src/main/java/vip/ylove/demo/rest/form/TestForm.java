package vip.ylove.demo.rest.form;

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

    Date now2 = new Date();
}
