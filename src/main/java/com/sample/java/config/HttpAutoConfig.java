package com.sample.java.config;

import com.sample.java.wrapper.HttpClient;
import com.sample.java.wrapper.HttpWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpAutoConfig {
    @Bean
    public HttpClient httpClient(){
        return new HttpClient();
    }

    @Bean
    public HttpWrapper httpWrapper(){
        return new HttpWrapper();
    }
}
