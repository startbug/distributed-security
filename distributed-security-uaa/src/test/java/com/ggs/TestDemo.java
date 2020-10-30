package com.ggs;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author Starbug
 * @Date 2020/10/28 21:18
 */
public class TestDemo {
    @Test
    public void test1(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("secret"));
    }
}
