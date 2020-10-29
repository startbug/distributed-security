package com.ggs.security.distributed.gateway.config;

import com.ggs.security.distributed.gateway.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author Starbug
 * @Date 2020/10/29 19:16
 */
@Configuration
public class ZuulConfig {

    @Bean
    public AuthFilter preFilter() {
        return new AuthFilter();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //是否支持用户凭证
        config.addAllowedOrigin("*");   //允许请求的源
        config.addAllowedHeader("*");   //允许的请求头
        config.addAllowedHeader("*");   //允许的请求方式
        config.setMaxAge(18000L); //设置预请求可以被客户按缓存多长时间
        source.registerCorsConfiguration("/**", config);    //根据config配置过滤/**匹配的请求
        CorsFilter corsFilter = new CorsFilter(source); //将soruce配置大大啊哦corsFilter过滤器中
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(corsFilter); //注册过滤器
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);    //设置高优先级
        return filterRegistrationBean;
    }

}
