package com.shou.crabscore;

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

    }
}
