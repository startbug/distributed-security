package com.ggs.security.distributed.uaa.service;

import com.ggs.security.distributed.uaa.dao.PermissionDao;
import com.ggs.security.distributed.uaa.dao.UserDao;
import com.ggs.security.distributed.uaa.entity.UserDto;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Starbug
 * @Date 2020/10/27 13:32
 */
@Service
public class SpringDataUserDetailsService implements UserDetailsService {
    @Resource
    private UserDao userDao;

    @Resource
    private PermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        UserDto user = userDao.findUserByName(username);
        if (user == null) {
            return null;
        }

        List<String> permissionList = permissionDao.findPermissionByUserId(user.getId());

//        UserDetails userDetails = User.withUsername("zhangsan").password("$2a$10$D7nYjcZ77dxb/PWfJb.jOe5ak0lD33Rv/1zUm3vcMnUkqCOQvtKtK").authorities("p1").build();
        User securityUser = new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(permissionList.toArray(new String[permissionList.size()])));
//        User securityUser = new User(user.getUsername(), user.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        return securityUser;
    }
}
