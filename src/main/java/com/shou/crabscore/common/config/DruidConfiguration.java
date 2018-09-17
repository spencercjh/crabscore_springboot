package com.shou.crabscore.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.shou.crabscore.common.util.JasyptUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @author spencercjh
 */
@Log4j2
@Configuration
public class DruidConfiguration {

    /**
     * 注册一个DruidDataSource 参数来源于@ConfigurationProperties(prefix = "spring.datasource")
     *
     * @return DataSource
     */
    @SuppressWarnings("ContextJavaBeanUnresolvedMethodsInspection")
    @Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Value("${spring.datasource.druid.stat-view-servlet.enabled}")
    private String enabled;
    @Value("${spring.datasource.druid.stat-view-servlet.url-pattern}")
    private String urlPattern;
    @Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String resetEnable;
    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
    private String loginUsername;
    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
    private String loginPassword;
    @Value("${spring.datasource.druid.stat-view-servlet.allow}")
    private String allow;
    @Value("${spring.datasource.druid.stat-view-servlet.deny}")
    private String deny;

    @Value("${jasypt.encryptor.password}")
    private String password;

    /**
     * 注册一个StatViewServlet
     *
     * @return ServletRegistrationBean<StatViewServlet>
     */
    @Bean

    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(
                new StatViewServlet(), urlPattern);
        servletRegistrationBean.addInitParameter("enabled", enabled);
        servletRegistrationBean.addInitParameter("allow", allow);
        servletRegistrationBean.addInitParameter("deny", deny);
        servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword", JasyptUtil.decryptPwd(password, loginPassword));
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     *
     * @return FilterRegistrationBean<WebStatFilter>
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {

        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());

        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");

        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}