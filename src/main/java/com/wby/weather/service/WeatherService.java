package com.wby.weather.service;

import com.wby.weather.common.RestResponse;

public interface WeatherService {
    RestResponse getTemperature(String province, String city, String county);
}
