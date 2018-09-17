package com.shou.crabscore;

import com.shou.crabscore.common.util.JasyptUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrabScoreApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(CrabScoreApplicationTests.class);

    @Test
    public void contextLoads() {
    }

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Test
    public void testJasypt() {

        //加密
        logger.error(JasyptUtil.encryptPwd(password, "123456"));
        //解密
        logger.error(JasyptUtil.decryptPwd(password, "jWR3Xp1BAWv3CgVvulTxOw=="));
    }
}
