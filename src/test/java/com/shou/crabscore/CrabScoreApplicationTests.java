package com.shou.crabscore;

import com.shou.crabscore.common.util.netease.MessageUtil;
import com.shou.crabscore.common.util.JasyptUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrabScoreApplicationTests {


    @Test
    public void contextLoads() {
    }

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Test
    public void testJasypt() {
        log.info("secret:" + JasyptUtil.encryptPwd(password, "123"));
        log.info("key:" + JasyptUtil.encryptPwd(password, "123"));
    }

    @Test
    public void testCode() {
        try {
            MessageUtil.sendCode("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
