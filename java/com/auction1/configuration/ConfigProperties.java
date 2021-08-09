package com.auction1.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component()
@ConfigurationProperties(prefix = "connect", ignoreUnknownFields = false)
public class ConfigProperties {

    private String URL;
    private String USERNAME;
    private String PASSWORD;

}
