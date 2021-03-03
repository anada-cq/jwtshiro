package com.woniuxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.model.User;
import com.woniuxy.utils.JWTUtil;
import com.woniuxy.utils.SaltUtils;
import com.woniuxy.vo.FilmVO;
import com.woniuxy.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin
public class UserController {

    @Resource
    private UserMapper userMapper;

    //登录 通过编码方式进行验证
    @RequestMapping("login")
    public Result login(@RequestBody UserVO userVO){

        //根据用户名查询数据库,获取一个用户对象
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVO.getUsername());
        User userDB = userMapper.selectOne(wrapper);

        //判断获得用户对象是否为空
        if (!ObjectUtils.isEmpty(userDB)) {

            Md5Hash md5Hash = new Md5Hash(userVO.getPassword(),userDB.getSalt(),1024);
            String password = md5Hash.toHex();
            //如果不为空，判断查询到的用户密码是否和用户登录的密码是否相同
            if (userDB.getPassword().equals(password)) {
                //密码相同判定用户登录成功，创建一个JWT，返回到前端
                HashMap<String, String> map = new HashMap<>();
                    //将用户名保存到JWT中，返回前端
                map.put("username",userVO.getUsername());
                String token = JWTUtil.createToken(map);
                //响应前端
                return  new Result(true,StatusCode.OK,"登录成功",token);
            }else {
                //密码不相同，登录失败
                return  new Result(false,StatusCode.LOGINERROR,"密码错误");
            }
        }

      //如果从数据库中获取的对象为空，则表示该用户名不存在
        return  new Result(false,StatusCode.UNKNOWNUSER,"该用户不存在");

    }

    //注册
    @RequestMapping("register")
    public Result register(@RequestBody User user){
        System.out.println(user);
        //先根据获得username查询数据库中是否有相同的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User userDB = userMapper.selectOne(wrapper);

        //判断获取的对象是否为空，若为空可注册，不为空则回复用户已存在
        if (ObjectUtils.isEmpty(userDB)) {
            //设置盐
            String salt = SaltUtils.getSalt(8);
            //使用md5+盐的方式加密
            Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);

            //重新设置加盐后的密码以及盐
            user.setPassword(md5Hash.toHex()).setSalt(salt);
            //调用新增用户的方法
            userMapper.insert(user);
            return new Result();
        }
        return new Result(false, StatusCode.ERROR,"该用户已存在");
    }


    //查询所有用户
    @RequestMapping("all")
    public Result getAllFilm(@RequestBody FilmVO filmVO){

        //生成分页对象
        Page<User> page = new Page<>(filmVO.getCurrent(), filmVO.getSize());

        //查询对应的分页数据
        IPage<User> userIPage = userMapper.selectPage(page, null);

        //将查询到的用户分页信息返回前端
        return new Result(true,StatusCode.OK,"用户分页信息查询成功",userIPage);
    }

    //查询所有用户
    @RequestMapping("findAll")
    @RequiresRoles(value = {"董事长","人事总监"},logical = Logical.OR)
    public Result getAllUser() {
        List<User> users = userMapper.selectList(null);

        return new Result(true,StatusCode.OK,"用户信息查询成功",users);
    }

}

