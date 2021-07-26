package com.wby.weather.common;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Controller
public class SwaggerConfig {
    @GetMapping("/doc")
    public String doc() {
        return "redirect:swagger-ui.html";
    }

    @GetMapping("/")
    String index() {
        return "redirect:/doc";
    }

    @Value("${api.doc.enable}")
    private boolean enable;

    @Value("${server.port}")
    private int port;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(PathSelectors.regex("/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("weather api doc")
                .contact(new Contact("wbyshr", "", "wbyshr@outlook.com"))
                .description("SWAGGER_2")
                .termsOfServiceUrl("NO terms of service")
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("v1.0")
                .build();
    }
}