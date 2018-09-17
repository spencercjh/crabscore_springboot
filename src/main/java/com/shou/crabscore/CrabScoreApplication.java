package com.shou.crabscore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * @author spencercjh
 */
@SpringBootApplication(scanBasePackages = {"com.shou.crabscore"})
@MapperScan(basePackages = {"com.shou.crabscore.*"})
@EntityScan(basePackages = {"com.shou.crabscore.entity"})
@ComponentScan(basePackages = {"com.shou.crabscore.*"}, excludeFilters = {@ComponentScan.Filter(classes = Service.class)})
public class CrabScoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrabScoreApplication.class, args);
    }
}
