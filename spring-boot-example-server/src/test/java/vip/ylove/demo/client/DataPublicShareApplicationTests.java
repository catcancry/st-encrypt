package vip.ylove.demo.client;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.util.StServerUtil;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SpringBootTest
class DataPublicShareApplicationTests {

    @Test
    void contextLoads() {
    }

}
