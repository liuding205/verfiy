package com.bdyh;

import com.bdyh.properties.SystemProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SystemProperties.class)
public class BdyhVerifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdyhVerifyApplication.class, args);
    }
}
