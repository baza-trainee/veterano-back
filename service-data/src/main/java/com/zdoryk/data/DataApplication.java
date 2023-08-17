package com.zdoryk.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = {
        "com.zdoryk"
})
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class,args);
    }
}
