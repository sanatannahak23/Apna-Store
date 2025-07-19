package com.apnaStore.inventory_service.config;

import com.apnaStore.inventory_service.helper.CustomFeignErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public CustomFeignErrorDecoder customFeignErrorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
