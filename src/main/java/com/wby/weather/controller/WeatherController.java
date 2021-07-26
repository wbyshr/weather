package com.wby.weather.controller;

import com.wby.weather.common.RateLimiter;
import com.wby.weather.common.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.wby.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Api(tags = "weather")
@Slf4j
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RateLimiter(value = 100, timeout = 300)
    @GetMapping("/temperature/{province}/{city}/{county}")
    @ApiOperation(value = "temperature", notes = "county temperature")
    public RestResponse getTemperature(@PathVariable String province, @PathVariable String city, @PathVariable String county) {
        log.info(String.format("province:%s,city:%s,county:%s", province, city, county));
        return weatherService.getTemperature(province, city, county);
    }
}
