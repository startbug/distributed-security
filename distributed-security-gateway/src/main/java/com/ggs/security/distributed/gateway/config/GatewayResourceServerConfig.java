package com.ggs.security.distributed.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author Starbug
 * @Date 2020/10/29 12:43
 */
@Configuration
public class GatewayResourceServerConfig {

    public static final String RESOURCE_ID = "res1";

    /**
     * @Author lhh
     * @Description 统一认证服务(UAA) 资源拦截
     * @Date 2020/10/29 12:43
     **/
    @Configuration
    @EnableResourceServer
    public class GatewayUAAServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/uaa/**").permitAll();
        }
    }

    /**
     * @Author lhh
     * @Description 订单服务
     * @Date 2020/10/29 12:46
     **/
    @Configuration
    @EnableResourceServer
    public class GatewayOrderServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/order/**").access("#oauth2.hasScope('ROLE_API')");
        }
    }
}
