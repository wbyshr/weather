package com.wby.weather.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wby.weather.common.RestResponse;
import com.wby.weather.common.Utils;
import com.wby.weather.dao.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherClient weatherClient;

    @Autowired
    private WeatherRepository weatherRepository;

    //get temperature by provinceCode+cityCode+CountyCode
    @Override
    public RestResponse getTemperature(String province, String city, String county) {
        if (!weatherRepository.existsProvinces()) {
            initProvinces();
        }
        String provinceCode = weatherRepository.existsProvince(province) ? weatherRepository.getProvinceCode(province) : "";
        if (StringUtils.isEmpty(provinceCode)) {
            return new RestResponse.Fail(false, -1, "The province is not exists");
        }

        if (!weatherRepository.existsCities(provinceCode)) {
            initCities(provinceCode);
        }
        String cityCode = weatherRepository.existsCity(provinceCode, city) ? weatherRepository.getCityCode(provinceCode, city) : "";
        if (StringUtils.isEmpty(cityCode)) {
            return new RestResponse.Fail(false, -1, "The city is not exists");
        }

        if (!weatherRepository.existsCounties(provinceCode, cityCode)) {
            initCounties(provinceCode, cityCode);
        }
        String countyCode = weatherRepository.existsCounty(provinceCode, cityCode, county) ? weatherRepository.getCountyCode(provinceCode, cityCode, county) : "";
        if (StringUtils.isEmpty(countyCode)) {
            return new RestResponse.Fail(false, -1, "The county is not exists");
        }

        if (!weatherRepository.existsTemperature(provinceCode, cityCode, countyCode)) {
            initTemperature(provinceCode, cityCode, countyCode);
        }
        String temperature = weatherRepository.getTemperature(provinceCode, cityCode, countyCode);
        if (StringUtils.isEmpty(temperature)) {
            return new RestResponse.Fail(false, -1, "Failed to get temperature");
        }
        return new RestResponse.Ok("Success", temperature);
    }

    /* init province data to redis */
    private void initProvinces() {
        String jsonStr = weatherClient.getProvinces();
        if (!StringUtils.isEmpty(jsonStr) && Utils.isJSONStr(jsonStr)) {
            Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
            });
            Map<String, String> mapInversed = map.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            weatherRepository.setProvinces(mapInversed);
        }
    }

    /* init city data to redis */
    private void initCities(String provinceCode) {
        String jsonStr = weatherClient.getCities(provinceCode);
        if (!StringUtils.isEmpty(jsonStr) && Utils.isJSONStr(jsonStr)) {
            Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
            });
            Map<String, String> mapInversed = map.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            weatherRepository.setCities(provinceCode, mapInversed);
        }
    }

    /* init county data to redis */
    private void initCounties(String provinceCode, String cityCode) {
        String jsonStr = weatherClient.getCounties(provinceCode, cityCode);
        if (!StringUtils.isEmpty(jsonStr) && Utils.isJSONStr(jsonStr)) {
            Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
            });
            Map<String, String> mapInversed = map.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            weatherRepository.setCounties(provinceCode, cityCode, mapInversed);
        }
    }

    private void initTemperature(String provinceCode, String cityCode, String countyCode) {
        String jsonStr = weatherClient.getTemperature(provinceCode, cityCode, countyCode);
        if (!StringUtils.isEmpty(jsonStr) && Utils.isJSONStr(jsonStr)) {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            String temperature = "";
            if (jsonObject.containsKey("weatherinfo")) {
                temperature = jsonObject.getJSONObject("weatherinfo").getString("temp");
            }
            if (!StringUtils.isEmpty(temperature)) {
                weatherRepository.setTemperature(provinceCode, cityCode, countyCode, temperature);
            }
        }
    }
}
