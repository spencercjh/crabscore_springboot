package com.shou.crabscore.common.config.filter;

import com.shou.crabscore.filter.SecurityFilter;
import com.shou.crabscore.service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author spencercjh
 */
@Log4j2
@Configuration
public class FilterConfiguration {

    private final SecurityService securityService;

    @Autowired
    public FilterConfiguration(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Bean
    public FilterRegistrationBean securityFilterRegistration() {
        log.debug("security filter regist");
        FilterRegistrationBean<SecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecurityFilter(securityService));
        registration.addUrlPatterns("/api/admin/*");
        registration.addUrlPatterns("/api/judge/*");
        registration.addUrlPatterns("/api/staff/*");
        registration.addUrlPatterns("/api/company/*");
        registration.setName("安全校验filter");
        registration.setOrder(1);
        return registration;
    }
}