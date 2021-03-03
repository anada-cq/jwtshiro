package com.woniuxy.service;

import com.woniuxy.model.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */

public interface PermissionService extends IService<Permission> {

    //通过用户id查询权限
    List<Permission> findAllByUserId(Integer uid);

    void test();

}
