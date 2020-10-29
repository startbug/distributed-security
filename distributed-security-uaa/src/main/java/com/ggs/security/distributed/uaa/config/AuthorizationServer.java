package com.ggs.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @Author Starbug
 * @Date 2020/10/27 11:33
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    //配置授权的客户端的配置
    //并不是什么客户端都可以来获取用户信息,必选是要本平台认证过的第三方,并且登记过clientId和secretId才可以
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //Mysql中存储客户端的配置
        clients.withClientDetails(clientDetailsService); //配置JdbcClientDetailsService到客户端服务详情中

        //内存中存储客户端的配置
//        clients.inMemory()
//                .withClient("c1") //授权客户端的id值
//                .secret(new BCryptPasswordEncoder().encode("secret")) //客户端密钥
//                .resourceIds("res1") //该第三方平台允许访问的资源的列表
//                //该client允许的的授权类型 "authorization_code","password","client_credentials","implicit","refresh_token"
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
//                .scopes("all") //允许授权的范围
//                .autoApprove(false) //false: 授权时跳转授权页面; true: 直接授权
//                .redirectUris("https://www.baidu.com/");//回调地址
    }

    //认证服务器的token服务,指定token的配置
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService); //客户端信息服务
        services.setSupportRefreshToken(true); //是否产生刷新令牌
        services.setTokenStore(tokenStore); //令牌存储策略
        services.setAccessTokenValiditySeconds(60 * 60 * 2);  //令牌默认有效期2小时
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);    //刷新令牌默认有效期3天
        //---------------jwt token配置------------------
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        //----------------------------------------------
        return services;
    }

    //令牌访问端点配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)   //密码模式需要,SpringSecurity配置类中可获取
                .authorizationCodeServices(authorizationCodeServices)   //授权码模式需要
                .tokenServices(tokenServices()) //令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);   //允许post提交
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        //设置授权码模式的授权码存取,暂时采用内存方式
//        return new InMemoryAuthorizationCodeServices();

        //授权码存储在数据库中
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")  // 公开 /oauth/token_key 接口
                .checkTokenAccess("permitAll()")    // 公开 /oauth/check_token 接口
                .allowFormAuthenticationForClients();   //表单认证,申请令牌
    }
}
