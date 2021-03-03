package com.woniuxy.controller;


import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.woniuxy.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/permission")
//@CrossOrigin
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private PermissionMapper permissionMapper;


    //查询所有用户权限（只有一级和二级）
    @RequestMapping("menus")
    public Result findAll(){

        //通过shiro获取当前用户信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        System.out.println(user);

        List<Permission> menus =permissionService.findAllByUserId(user.getId());

        return  new Result(true, StatusCode.OK,"权限查询成功",menus);
    }

    //查询所有用户权限
    @RequestMapping("all")
    public Result findAllPermissionById(){

        //通过shiro获取当前用户信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        //查询权限
        List<Permission> permissions = permissionMapper.findAllByUserId(user.getId());

        return  new Result(true, StatusCode.OK,"权限查询成功",permissions);
    }

}

