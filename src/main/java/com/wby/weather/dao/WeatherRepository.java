package com.wby.weather.dao;

import com.wby.weather.common.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class WeatherRepository {

    @Autowired
    @Qualifier("redis6399db0Template")
    private StringRedisTemplate db0;

    public boolean existsProvinces() {
        return db0.hasKey(RedisKeys.WEATHER_PROVINCES);
    }

    public void setProvinces(Map<String, String> map) {
        String key = RedisKeys.WEATHER_PROVINCES;
        if (!existsProvinces() && !map.isEmpty()) {
            db0.opsForHash().putAll(key, map);
        }
    }

    public boolean existsProvince(String province) {
        return db0.opsForHash().hasKey(RedisKeys.WEATHER_PROVINCES, province);
    }

    public String getProvinceCode(String province) {
        return (String) db0.opsForHash().get(RedisKeys.WEATHER_PROVINCES, province);
    }

    public boolean existsCities(String provinceCode) {
        String redisKey = String.format(RedisKeys.WEATHER_CITIES, provinceCode);
        return db0.hasKey(redisKey);
    }

    public void setCities(String provinceCode, Map<String, String> map) {
        String redisKey = String.format(RedisKeys.WEATHER_CITIES, provinceCode);
        if (!existsCities(provinceCode) && !map.isEmpty()) {
            db0.opsForHash().putAll(redisKey, map);
        }
    }

    public boolean existsCity(String provinceCode, String city) {
        String redisKey = String.format(RedisKeys.WEATHER_CITIES, provinceCode);
        return db0.opsForHash().hasKey(redisKey, city);
    }

    public String getCityCode(String provinceCode, String city) {
        String redisKey = String.format(RedisKeys.WEATHER_CITIES, provinceCode);
        return (String) db0.opsForHash().get(redisKey, city);
    }

    public boolean existsCounties(String provinceCode, String cityCode) {
        String redisKey = String.format(RedisKeys.WEATHER_COUNTIES, provinceCode, cityCode);
        return db0.hasKey(redisKey);
    }

    public void setCounties(String provinceCode, String cityCode, Map<String, String> map) {
        String redisKey = String.format(RedisKeys.WEATHER_COUNTIES, provinceCode, cityCode);
        if (!existsCounties(provinceCode, cityCode) && !map.isEmpty()) {
            db0.opsForHash().putAll(redisKey, map);
        }
    }

    public boolean existsCounty(String provinceCode, String cityCode, String county) {
        String redisKey = String.format(RedisKeys.WEATHER_COUNTIES, provinceCode, cityCode);
        return db0.opsForHash().hasKey(redisKey, county);
    }

    public String getCountyCode(String provinceCode, String cityCode, String county) {
        String redisKey = String.format(RedisKeys.WEATHER_COUNTIES, provinceCode, cityCode);
        return (String) db0.opsForHash().get(redisKey, county);
    }

    public boolean existsTemperature(String provinceCode, String cityCode, String countyCode) {
        String redisKey = String.format(RedisKeys.WEATHER_TEMPERATURE, provinceCode, cityCode, countyCode);
        return db0.hasKey(redisKey);
    }

    public void setTemperature(String provinceCode, String cityCode, String countyCode, String temperature) {
        String redisKey = String.format(RedisKeys.WEATHER_TEMPERATURE, provinceCode, cityCode, countyCode);
        db0.opsForValue().set(redisKey, temperature, 300, TimeUnit.SECONDS);
    }

    public String getTemperature(String provinceCode, String cityCode, String countyCode) {
        String redisKey = String.format(RedisKeys.WEATHER_TEMPERATURE, provinceCode, cityCode, countyCode);
        return db0.opsForValue().get(redisKey);
    }
}
