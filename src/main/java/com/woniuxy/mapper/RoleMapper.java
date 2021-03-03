package com.woniuxy.mapper;

import com.woniuxy.model.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    //根据用户id查询用户角色
    @Select("select r.* from t_user as u " +
            "join t_user_role as ur on ur.uid=u.id " +
            "join t_role as r on ur.rid = r.id " +
            "where u.username=#{u.username}")
    List<Role> findRolesByUserName(String username);

}
