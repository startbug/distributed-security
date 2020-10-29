package com.ggs.security.distributed.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Starbug
 * @Date 2020/10/27 23:18
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServer {

    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class, args);
    }

}
