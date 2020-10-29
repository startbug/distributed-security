package com.ggs.security.distributed.uaa.dao;

import com.ggs.security.distributed.uaa.entity.UserDto;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Starbug
 * @Date 2020/10/26 18:58
 */
public interface UserDao {

    UserDto findUserByName(@Param("username") String username);
}
