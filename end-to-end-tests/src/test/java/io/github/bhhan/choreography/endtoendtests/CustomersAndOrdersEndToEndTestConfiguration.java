package io.github.bhhan.choreography.endtoendtests;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class CustomersAndOrdersEndToEndTestConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverters = Collections.singletonList(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(httpMessageConverters);
        return restTemplate;
    }

    @Bean
    public HttpMessageConverters customConverters(){
        MappingJackson2HttpMessageConverter additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }
}
