package com.ggs.security.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author Starbug
 * @Date 2020/10/27 23:19
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
//                .tokenServices(tokenServices())
                .tokenStore(tokenStore)
                .stateless(true); //设置资源仅允许基于令牌的身份验证
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasAnyScope('all','ROLE_USER')")
                .and().csrf().disable()
                //设置策略:无状态,不创建会话session,也绝不会去使用session,因为使用了token
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //资源服务令牌解析服务(远程,使用http通信)
//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        //使用远程服务请求授权服务器校验token,必须指定家坳眼token的url、client_id、client_secret
//        RemoteTokenServices services = new RemoteTokenServices();
//        services.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
//        services.setClientId("c1");
//        services.setClientSecret("secret");
//        return services;
//    }


}
