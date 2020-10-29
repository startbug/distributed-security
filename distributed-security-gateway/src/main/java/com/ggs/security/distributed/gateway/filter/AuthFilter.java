package com.ggs.security.distributed.gateway.filter;

import com.ggs.security.distributed.gateway.common.EncryptUtil;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Starbug
 * @Date 2020/10/29 18:54
 */
public class AuthFilter extends ZuulFilter {
    //可以返回的值有pre route post error static(看源码知道)
    //设置为pre,表示在路由之前执行该过滤器
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    //返回true,会run()方法,设置为false则不会调用run()
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //1.获取令牌内容
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //无token访问网关内资源的情况,目前仅有uua服务器直接暴露
        if (!(authentication instanceof OAuth2Authentication)) {
            //如果不是OAuth2的token,则返回空
            return null;
        }

        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oauth2Authentication.getUserAuthentication();

//        Object principal = userAuthentication.getPrincipal();

        //2.组装明文token,转发给微服务,放入header,名称为json-token
        List<String> list = new ArrayList<>();
        if (userAuthentication == null) {
            return null;
        }
        userAuthentication.getAuthorities().stream().forEach(a -> {
            list.add(a.getAuthority());
        });

        //本次请求中可能还有其它参数,将其他参数和principal(用户名)、权限authorities放一起
        OAuth2Request oAuth2Request = oauth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map jsonToken = new HashMap(requestParameters);

        Gson gson = new Gson();
        jsonToken.put("principal", userAuthentication.getName());
        jsonToken.put("authorities", gson.toJson(list));
        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(gson.toJson(jsonToken)));

        return null;
    }
}
