package io.github.bhhan.choreography.customers.web;

import io.github.bhhan.choreography.common.swagger.CommonSwaggerConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@Import(CommonSwaggerConfiguration.class)
public class CustomerWebConfiguration {
    @Bean
    public HttpMessageConverters customConverters(){
        MappingJackson2HttpMessageConverter additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }
}
