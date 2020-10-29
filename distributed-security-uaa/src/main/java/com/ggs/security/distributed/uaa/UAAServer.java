package com.ggs.security.distributed.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Starbug
 * @Date 2020/10/26 22:52
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@MapperScan("com.ggs.security.distributed.uaa.dao")
@EnableFeignClients(basePackages = {"com.ggs.security.distributed.uaa"})
public class UAAServer {
    public static void main(String[] args) {
        SpringApplication.run(UAAServer.class, args);
    }
}
