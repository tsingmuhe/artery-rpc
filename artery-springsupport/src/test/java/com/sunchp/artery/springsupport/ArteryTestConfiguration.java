package com.sunchp.artery.springsupport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.sunchp.artery.springsupport.api")
public class ArteryTestConfiguration {
    @Bean
    public ArteryServiceConfig serviceConfig() {
        return new ArteryServiceConfig();
    }
}
