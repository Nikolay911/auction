package com.auction1.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component()
@ConfigurationProperties(prefix = "download", ignoreUnknownFields = false)
public class DownloadConfig {

        private String path;

}
