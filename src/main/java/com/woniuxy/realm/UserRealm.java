package com.woniuxy.realm;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniuxy.component.JwtToken;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.model.Permission;
import com.woniuxy.model.Role;
import com.woniuxy.model.User;
import com.woniuxy.utils.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

//自定义领域
@Component
public class UserRealm extends AuthorizingRealm {

    //用户对用户进行操作
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;


    //重写supports方法，不写新的token无法使用
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取授权对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //获取token中的username
        String token = (String) principalCollection.getPrimaryPrincipal();

        //解析token，获取用户名
        DecodedJWT decodedJWT = JWTUtil.decodedJWT(token);
        String username = decodedJWT.getClaim("username").asString();

        //查询数据库，得到当前用户具有的角色
        List<Role> roles = roleMapper.findRolesByUserName(username);
        //遍历授权
        roles.forEach(role -> {
            info.addRole(role.getRolename());
        });

        return info;
    }



    //认证的方法,不再查询数据库，直接从jwt中获取用户名，判断是否为空，为空则抛异常
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //从jwt中获取token
        String token = (String) authenticationToken.getPrincipal();

        //解析token，获取用户名
        DecodedJWT decodedJWT = JWTUtil.decodedJWT(token);
        String username = decodedJWT.getClaim("username").asString();
        System.out.println(username);
        System.out.println(token);

        //判断用户名是否为空
        if (!StringUtils.hasLength(username)) {
            throw new AuthenticationException("token验证失败");
        }

        //用户名不为空，验证成功
        return new SimpleAuthenticationInfo(token,token,this.getName());
    }
}
