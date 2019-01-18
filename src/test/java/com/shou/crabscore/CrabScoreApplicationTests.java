package com.shou.crabscore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shou.crabscore.common.util.JasyptUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Log4j2
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CrabScoreApplicationTests {


    @Value("${jasypt.encryptor.password}")
    private String password;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testJasypt() {
        log.info("appkey:" + JasyptUtil.encryptPwd(password, "123"));
        log.info("appsecret:" + JasyptUtil.encryptPwd(password, "123"));
    }

    @Test
    public void testBytes() {
        String bytes = "[-59,-77,-31,116,94,-49,-127,114,90,38,32,-45,-18,84,-116,115]";
        byte[] key = new byte[16];
        int i = 0;
        JSONArray jsonArray = JSON.parseArray(bytes);
        for (Object object :
                jsonArray) {
            key[i++] = (byte) (((Integer) (object)).intValue());
        }
        for (byte b :
                key) {
            System.out.println(b);
        }
    }
}
