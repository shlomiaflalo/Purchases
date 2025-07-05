package com.johnbryce.coupon_system_final;

import com.johnbryce.coupon_system_final.security.TokenInformation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    @Bean
    public Map<UUID, TokenInformation> tokens(){
        return new HashMap<>();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().
                //For deep mapping
                        setFieldMatchingEnabled(true)
                .setFieldAccessLevel
                        (org.modelmapper.config.
                                Configuration.AccessLevel.
                                PRIVATE);
        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
