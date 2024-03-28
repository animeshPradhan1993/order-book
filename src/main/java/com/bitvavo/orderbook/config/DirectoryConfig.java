package com.bitvavo.orderbook.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix ="order-book")
@Data
public class DirectoryConfig {
    private String inputDirectory;
    private String outputDirectory;
}
