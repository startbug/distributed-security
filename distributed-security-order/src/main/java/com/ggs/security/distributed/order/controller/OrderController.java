package com.ggs.security.distributed.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Starbug
 * @Date 2020/10/27 23:38
 */
@RestController
public class OrderController {

    @GetMapping(value = "r1")
    @PreAuthorize("hasAnyAuthority('p2')")
    public String s1() {
        return "访问资源1";
    }

    @GetMapping(value = "r2")
    @PreAuthorize("hasAuthority('p1')")
    public String s2() {
        return "访问资源2";
    }
}
