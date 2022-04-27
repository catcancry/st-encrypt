package vip.ylove.demo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import vip.ylove.annotation.StEnableSecurity;

@EnableFeignClients
@StEnableSecurity
@SpringBootApplication
public class StClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StClientApplication.class, args);
    }

}
