package com.ggs.security.distributed.uaa.entity;

import lombok.Data;

/**
 * @Author Starbug
 * @Date 2020/10/26 18:57
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private String mobile;
}
