package com.wby.weather.service;

import com.wby.weather.common.RestResponse;
import com.wby.weather.dao.WeatherRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCountyTemperature() {
        String province = "江苏";
        String provinceCode = "10119";
        String city = "苏州";
        String cityCode = "04";
        String county = "苏州";
        String countyCode = "01";
        String temperature = "33";

        RestResponse restResponse = new RestResponse.Ok("Success", temperature);

        when(weatherRepository.existsProvinces()).thenReturn(true);
        when(weatherRepository.existsProvince(province)).thenReturn(true);
        when(weatherRepository.getProvinceCode(province)).thenReturn(provinceCode);

        when(weatherRepository.existsCities(provinceCode)).thenReturn(true);
        when(weatherRepository.existsCity(provinceCode, city)).thenReturn(true);
        when(weatherRepository.getCityCode(provinceCode, city)).thenReturn(cityCode);

        when(weatherRepository.existsCounties(provinceCode, cityCode)).thenReturn(true);
        when(weatherRepository.existsCounty(provinceCode, cityCode, county)).thenReturn(true);
        when(weatherRepository.getCountyCode(provinceCode, cityCode, county)).thenReturn(countyCode);

        when(weatherRepository.existsTemperature(provinceCode, cityCode, countyCode)).thenReturn(true);
        when(weatherRepository.getTemperature(provinceCode, cityCode, countyCode)).thenReturn(temperature);

        RestResponse rst = weatherService.getTemperature(province, city, county);

        assertEquals(restResponse.isOk(), rst.isOk());
        assertEquals(restResponse.getCode(), rst.getCode());
        assertEquals(restResponse.getMsg(), rst.getMsg());
        assertEquals(restResponse.getData(), rst.getData());
    }
}