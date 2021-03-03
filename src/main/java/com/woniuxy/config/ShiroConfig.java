package com.woniuxy.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.woniuxy.filter.JwtFilter;
import com.woniuxy.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

//shiro配置类
@Configuration
public class ShiroConfig {

    //注册realm组件
    @Bean
    public Realm realm(){
        //先new一个自定义领域对象
        UserRealm userRealm = new UserRealm();

//        //获取一个哈希凭证匹配器
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        //设置加密算法
//        matcher.setHashAlgorithmName("md5");
//        //设置散列次数
//        matcher.setHashIterations(1024);

        //将哈希凭证匹配器注入到自定义领域中
//        userRealm.setCredentialsMatcher(matcher);

        //将自定义领域返回
        return userRealm;
    }

    //注册安全管理器组件
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        //new 一个安全管理器对象
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        //将自定义领域注入到哦安全管理器对象中
        manager.setRealm(realm());

        //将记忆组件注入到安全管理器中
        manager.setRememberMeManager(cookieRememberMeManager());

        //将安全管理器对象返回
        return  manager;
    }

    //注册过滤器组件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        //new一个shiro过滤器工厂对象
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        //将安全管理器注入到过滤器工厂对象中
        factoryBean.setSecurityManager(defaultWebSecurityManager());

        //向过滤其中添加jwt过滤器
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("jwt",new JwtFilter());

        //创建一个linkedhashmap 用于设置黑白名单
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        //设置白名单
        map.put("/js/**","anon");  //authc 和 user 用于设置黑名单
        map.put("/css/**","anon");
        map.put("/user/login","anon");
        map.put("/user/register","anon");
        map.put("/permission/menus","anon");
        //设置注销功能
        map.put("/user/logout","logout");
        map.put("/page/register.html","anon");
        map.put("/page/login.html","anon");
        //拦截全部
        map.put("/**","jwt");//使用自定义的过滤器进行处理
        //将map集合设置到过滤器工厂对象中
        factoryBean.setFilterChainDefinitionMap(map);
        //设置当被拦截后跳转后的页面
//        factoryBean.setLoginUrl("/page/login.html");
        //前后端分离使用的跳转页面
//        factoryBean.setLoginUrl("http://localhost:8080/login");


        //将过滤器工厂对象返回
        return factoryBean;
    }

    //注册rememberMe组件
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        //new一个对象
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();

        //new一个简单数据对象，并设置名称
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

        //设置记忆的最长时间
        simpleCookie.setMaxAge(60*60);

        //将简单数据对象注入到数据记忆管理中
        rememberMeManager.setCookie(simpleCookie);

        //设置加密
        rememberMeManager.setCipherKey(Base64.decode("asdfghjklzxcvbnmqwerty=="));

        //返回管理器对象
        return rememberMeManager;
    }

    //注册分页组件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    //设置跨域
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return  new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //设置哪些路径可以跨域访问
                registry.addMapping("/**")
                        //授权的源（哪些的源头发送的请求可以跨域
//                        .allowedOriginPatterns("*")  //高版本springboot
                        .allowedOrigins("*")  //低版本
                        //设置发送跨域的请求方式
                        .allowedMethods("*")
                        //设置哪些请求头可以发送跨域请求
                        .allowedHeaders("*")
                        //是否允许ajax附带cookie
                        .allowCredentials(true)
                        //设置跨域的最大时长
                        .maxAge(3000);
            }
        };
    }
}
