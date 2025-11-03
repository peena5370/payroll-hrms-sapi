package com.company.payroll.util.config;

import com.company.payroll.util.model.SnowFlakeUtilProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.payroll.util.util.SnowFlakeIdGenerator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@PropertySource(value = "classpath:util/config.properties")
@PropertySource(value = "classpath:util/config-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class SnowFlakeIdGeneratorWorkerConfig {
    private final SnowFlakeUtilProperties properties;

    public SnowFlakeIdGeneratorWorkerConfig(SnowFlakeUtilProperties properties) {
        this.properties = properties;
    }

	@Bean
	public SnowFlakeIdGenerator createSnowFlakeIdGenerator() {
        log.info("workerId={} datacenterId{}", properties.getWorkerId(), properties.getDatacenterId());
        return new SnowFlakeIdGenerator(properties.getWorkerId(), properties.getDatacenterId());
	}
}
