package io.github.bhhan.choreography.common.swagger;

import io.eventuate.util.spring.swagger.EventuateSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonSwaggerConfiguration {
    @Bean
    public EventuateSwaggerConfig eventuateSwaggerConfig(){
        return () -> "io.github.bhhan.choreography";
    }
}
