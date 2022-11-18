package vip.ylove.demo.rest.form;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


@Data
@ToString
public class TestForm {
    @NotNull(message = "[t]t不能为空")
    String t;
    String appId;
    String auth;
    String test;
    String data;
}
