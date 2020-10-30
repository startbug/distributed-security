package com.ggs.security.distributed.order.filter;

import com.ggs.security.distributed.order.common.EncryptUtil;
import com.ggs.security.distributed.order.entity.UserDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author Starbug
 * @Date 2020/10/29 19:46
 * OncePerRequestFilter过滤器每次请求都会执行,非常适合用来解析token
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenBase64 = request.getHeader("json-token");
        String jsonToken = EncryptUtil.decodeUTF8StringBase64(tokenBase64);
        if (StringUtils.isNotBlank(jsonToken)) {
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(jsonToken, Map.class);

            //认证服务中将整个用户对象放入principal中(json格式)
            String principal = (String) map.get("principal");
            UserDto userDto = gson.fromJson(principal, UserDto.class);

            String authoritiesStr = (String) map.get("authorities");
            List<String> authorities = gson.fromJson(authoritiesStr, List.class);
            String[] authoritiesStrArr = new String[authorities.size()];
            for (int i = 0; i < authorities.size(); i++) {
                authoritiesStrArr[i] = authorities.get(i);
            }

//            UserDto userDto = new UserDto();
//            userDto.setUsername(username);
            //2.将用户信息填充到一个认证成功的token中
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDto, null, AuthorityUtils.createAuthorityList(authoritiesStrArr));
            //3.放入到SpringSecurity上下文中,方便之后使用
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        //让过滤器链继续执行下去
        filterChain.doFilter(request, response);
    }
}
