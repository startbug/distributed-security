package com.ggs.security.distributed.uaa.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Starbug
 * @Date 2020/10/26 20:25
 */
public interface PermissionDao {
    List<String> findPermissionByUserId(@Param("id") Long id);
}
