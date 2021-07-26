package com.wby.weather;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer feignRetryer() {
        Retryer retryer = new Retryer.Default(100, 1000, 3);
        return retryer;
    }
}
