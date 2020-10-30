package com.ggs.security.distributed.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Starbug
 * @Date 2020/10/27 23:38
 */
@RestController
public class OrderController {

    @GetMapping(value = "r1")
    @PreAuthorize("hasAnyAuthority('p2')")
    public Map s1(Authentication authentication) {
        HashMap hashMap = new HashMap();
        hashMap.put("msg","访问资源1");
        hashMap.put("item",authentication);
        return hashMap;
    }

    @GetMapping(value = "r2")
    @PreAuthorize("hasAuthority('p1')")
    public Map s2(Authentication authentication) {
        HashMap hashMap = new HashMap();
        hashMap.put("msg","访问资源2");
        hashMap.put("item",authentication);
        return hashMap;
    }
}
