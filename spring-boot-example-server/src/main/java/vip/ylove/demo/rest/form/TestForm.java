package vip.ylove.demo.rest.form;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Data
@ToString
public class TestForm {
    String t;
    String appId;
    String auth;
    String test;
    String data;
}
