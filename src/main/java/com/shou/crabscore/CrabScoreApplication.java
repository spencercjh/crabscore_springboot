package com.shou.crabscore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

/**
 * @author spencercjh
 */
@SpringBootApplication(scanBasePackages = {"com.shou.crabscore"})
@MapperScan(basePackages = {"com.shou.crabscore.dao"}, annotationClass = Repository.class)
@EntityScan(basePackages = {"com.shou.crabscore.entity"})
@ComponentScan(basePackages = {"com.shou.crabscore.*"})
@EnableCaching
public class CrabScoreApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrabScoreApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CrabScoreApplication.class, args);
    }
}
