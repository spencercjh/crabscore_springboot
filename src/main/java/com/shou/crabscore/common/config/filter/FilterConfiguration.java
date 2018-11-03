package com.shou.crabscore.common.config.filter;

import com.shou.crabscore.filter.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author spencercjh
 */
@Log4j2
@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean securityFilterRegistration() {
        log.debug("security filter regist");
        FilterRegistrationBean<SecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecurityFilter());
        registration.addUrlPatterns("/api/admin/*");
        registration.addUrlPatterns("/api/judge/*");
        registration.addUrlPatterns("/api/staff/*");
        registration.addUrlPatterns("/api/company/*");
        registration.setName("安全校验filter");
        registration.setOrder(1);
        return registration;
    }
}