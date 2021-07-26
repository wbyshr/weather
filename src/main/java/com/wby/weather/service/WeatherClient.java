package com.wby.weather.service;

import com.wby.weather.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "weather", url = "http://www.weather.com.cn", configuration = FeignConfiguration.class, fallback = WeatherClientFallback.class)
public interface WeatherClient {
    @RequestMapping(value = "/data/city3jdata/china.html", method = RequestMethod.GET)
    String getProvinces();

    @RequestMapping(value = "/data/city3jdata/provshi/{provinceCode}.html", method = RequestMethod.GET)
    String getCities(@PathVariable(value = "provinceCode") String provinceCode);

    @RequestMapping(value = "/data/city3jdata/station/{provinceCode}{cityCode}.html", method = RequestMethod.GET)
    String getCounties(@PathVariable(value = "provinceCode") String provinceCode, @PathVariable(value = "cityCode") String cityCode);

    @RequestMapping(value = "/data/sk/{provinceCode}{cityCode}{countyCode}.html", method = RequestMethod.GET)
    String getTemperature(@PathVariable(value = "provinceCode") String provinceCode, @PathVariable(value = "cityCode") String cityCode, @PathVariable(value = "countyCode") String countyCode);
}
