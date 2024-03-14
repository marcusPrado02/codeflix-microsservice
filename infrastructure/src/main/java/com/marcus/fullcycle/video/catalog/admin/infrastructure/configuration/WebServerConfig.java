package com.marcus.fullcycle.video.catalog.admin.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// Looks for classes under this package to create beans to be used by the server
@ComponentScan("com.marcus.fullcycle.video.catalog.admin")
public class WebServerConfig {
}
