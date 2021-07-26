package com.wby.weather.controller;

import com.wby.weather.WeatherApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WeatherApplication.class)
public class WeatherControllerTest {
    private final static String URLTEMPLATE = "/weather/temperature/{province}/{province}/{county}";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetCountyTemperature() throws Exception {
        String province = "江苏";
        String city = "苏州";
        String county = "苏州";
        mockMvc.perform(MockMvcRequestBuilders.get(URLTEMPLATE, province, city, county)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ok").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.msg").value("Success"))
                .andDo(print());
    }

    @Test
    public void verifyInvalidProvince() throws Exception {
        String province = "1";
        String city = "苏州";
        String county = "苏州";
        mockMvc.perform(MockMvcRequestBuilders.get(URLTEMPLATE, province, city, county)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ok").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.msg").value("The province is not exists"))
                .andDo(print());
    }

    @Test
    public void verifyInvalidCity() throws Exception {
        String province = "江苏";
        String city = "1";
        String county = "苏州";
        mockMvc.perform(MockMvcRequestBuilders.get(URLTEMPLATE, province, city, county)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ok").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.msg").value("The city is not exists"))
                .andDo(print());
    }

    @Test
    public void verifyInvalidCounty() throws Exception {
        String province = "江苏";
        String city = "苏州";
        String county = "1";
        mockMvc.perform(MockMvcRequestBuilders.get(URLTEMPLATE, province, city, county)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ok").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.msg").value("The county is not exists"))
                .andDo(print());
    }
}