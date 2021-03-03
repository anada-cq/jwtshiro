package com.woniuxy.service.impl;

import com.woniuxy.model.Permission;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;


    
    @Override
    public List<Permission> findAllByUserId(Integer uid) {

        //调用方法查询到所有的权限
        List<Permission> permissions = permissionMapper.findAllByUserId(uid);

        //新建一个集合用于保存所有的一级权限
        ArrayList<Permission> rootMenus = new ArrayList<>();

        //遍历权限
        permissions.forEach(permission -> {
            //当level为1的时候为一级权限，将这些权限保存至rootMenu中
            if (permission.getLevel()==1) {
                permission.setChildren(new ArrayList<Permission>());
                rootMenus.add(permission);
            }
        });

        //再次遍历权限
        permissions.forEach(permission -> {
            //遍历一级权限
            rootMenus.forEach(rootMenu->{
                //当权限的pid与一级权限的id相同时，则表示此权限被包含于一级权限，并将这些权限保存于一级权限中
                if (permission.getPid()==rootMenu.getId()) {
                    rootMenu.getChildren().add(permission);
                    return;
                }
            });
        });

        return rootMenus;
    }

    @Override
    public void test() {
        System.out.println("test测试");
    }
}
