package com.auction1;

import com.auction1.jdbcconf.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(ConfigProperties.class)
public class Auction1Application {

    public static void main(String[] args) {

        SpringApplication.run(Auction1Application.class, args);
    }

}
