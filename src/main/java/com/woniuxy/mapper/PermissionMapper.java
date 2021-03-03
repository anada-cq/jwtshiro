package com.woniuxy.mapper;

import com.woniuxy.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    //通过用户id查询权限
    @Select("SELECT p.* " +
            "FROM t_user AS u " +
            "JOIN t_user_role AS ur " +
            "ON u.id = ur.uid " +
            "JOIN t_role AS r " +
            "ON ur.rid = r.id " +
            "JOIN t_role_permission AS rp " +
            "ON r.id = rp.rid " +
            "JOIN t_permission AS p " +
            "ON rp.pid  = p.id " +
            "WHERE u.id=#{u.id}")
    List<Permission> findAllByUserId(Integer uid);



}
