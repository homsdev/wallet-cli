package com.homsdev.account_manager_cli.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:queries.properties")
public class CustomPropertiesConfig {
}
