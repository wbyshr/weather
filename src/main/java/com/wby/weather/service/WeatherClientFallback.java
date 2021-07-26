package com.wby.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WeatherClientFallback implements WeatherClient {

    @Override
    public String getProvinces() {
        log.warn("getProvinces fallback");
        return "";
    }

    @Override
    public String getCities(String provinceCode) {
        log.warn(String.format("getCities fallback:[%s]", provinceCode));
        return "";
    }

    @Override
    public String getCounties(String provinceCode, String cityCode) {
        log.warn(String.format("getCounties fallback:[%s_%s]", provinceCode, cityCode));
        return "";
    }

    @Override
    public String getTemperature(String provinceCode, String cityCode, String countyCode) {
        log.warn(String.format("getTemperature fallback:[%s_%s_%s]", provinceCode, cityCode, countyCode));
        return "";
    }
}
