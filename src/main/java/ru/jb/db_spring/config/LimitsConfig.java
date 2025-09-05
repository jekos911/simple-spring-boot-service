package ru.jb.db_spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(LimitsProperties.class)
@EnableScheduling
public class LimitsConfig {
}
