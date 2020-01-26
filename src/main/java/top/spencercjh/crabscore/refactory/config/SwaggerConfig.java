package top.spencercjh.crabscore.refactory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("CrabScore-Refactory Api Doc")
                        .description("CrabScore-Refactory Api Doc")
                        .version("3.0.0-SNAPSHOT")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.spencercjh.crabscore.refactory.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}