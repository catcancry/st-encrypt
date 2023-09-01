package vip.ylove.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vip.ylove.annotation.StEnableSecurity;


@StEnableSecurity
@SpringBootApplication
public class StServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StServerApplication.class, args);
    }


}
