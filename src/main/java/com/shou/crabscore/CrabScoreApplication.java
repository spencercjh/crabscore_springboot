package com.shou.crabscore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

/**
 * @author spencercjh
 */
@SpringBootApplication(scanBasePackages = {"com.shou.crabscore"})
@MapperScan(basePackages = {"com.shou.crabscore.dao"}, annotationClass = Repository.class)
@EntityScan(basePackages = {"com.shou.crabscore.entity"})
@ComponentScan(basePackages = {"com.shou.crabscore.*"})
@ServletComponentScan(basePackages = {"com.shou.crabscore.filter"})
public class CrabScoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrabScoreApplication.class, args);
    }
}
