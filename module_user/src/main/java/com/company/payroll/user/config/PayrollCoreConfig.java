package com.company.payroll.user.config;

import com.company.payroll.user.model.PayrollCoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@PropertySource(value = "classpath:user/config.properties")
@PropertySource(value = "classpath:user/config-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class PayrollCoreConfig {

    private final PayrollCoreProperties properties;

    public PayrollCoreConfig(PayrollCoreProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient payrollCoreClientBuilder(WebClient.Builder builder) {
        log.info("user config initialized: {}", properties.getEndpoint());
        return builder.baseUrl(properties.getEndpoint()).build();
    }
}
