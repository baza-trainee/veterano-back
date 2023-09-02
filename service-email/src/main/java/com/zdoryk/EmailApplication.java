package com.zdoryk;

import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class EmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class,args);
    }


}
