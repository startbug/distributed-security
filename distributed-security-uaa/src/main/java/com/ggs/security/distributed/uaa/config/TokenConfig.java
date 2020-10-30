package com.ggs.security.distributed.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @Author Starbug
 * @Date 2020/10/27 11:47
 */
@Configuration
public class TokenConfig {
    /**
     * 密钥,方便实验,使用对称的加密方式(后期可以修改未rsa非对称加密)
     **/
    private static final String SIGNING_KEY = "strabug";
//    //令牌存储策略      version-1
//    @Bean
//    public TokenStore tokenStore(){
//        //内存方式,生成普通令牌
//        return new InMemoryTokenStore();
//    }

    /**
     * 修改为发放jwt token,需要制定token转换器
     **/
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY); //指定签名的密钥
        //TODO 设置密钥对(RSA)
//        jwtAccessTokenConverter.setKeyPair();
        return jwtAccessTokenConverter;
    }

}


//    CREATE TABLE `oauth_client_details` (
//        `client_id` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端标识',
//        `resource_ids` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入资源列表',
//        `client_secret` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端秘钥',
//        `scope` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        `authorized_grant_types` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        `web_server_redirect_uri` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        `authorities` VARCHAR ( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        `access_token_validity` INT ( 11 ) NULL DEFAULT NULL,
//        `refresh_token_validity` INT ( 11 ) NULL DEFAULT NULL,
//        `additional_information` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
//        `create_time` TIMESTAMP ( 0 ) NOT NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) ON UPDATE CURRENT_TIMESTAMP ( 0 ),
//        `archived` TINYINT ( 4 ) NULL DEFAULT NULL,
//        `trusted` TINYINT ( 4 ) NULL DEFAULT NULL,
//        `autoapprove` VARCHAR ( 255 ) CHARACTE RSET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        PRIMARYKEY ( `client_id` ) USING BTREE
//        ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接入客户端信息' ROW_FORMAT = Dynamic;
