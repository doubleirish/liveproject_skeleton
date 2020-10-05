package com.laurentiuspilca.liveproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfiguration {

}
