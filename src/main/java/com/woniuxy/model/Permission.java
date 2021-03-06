package com.woniuxy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_permission")
@ApiModel(value="Permission对象", description="")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "菜单项")
        private String element;

        @ApiModelProperty(value = "点击某个菜单之后跳转的路径")
        private String url;

        @ApiModelProperty(value = "菜单等级")
        private Integer level;

        @ApiModelProperty(value = "父菜单ID")
        private Integer pid;


        //新增属性用于保存下一级权限
        private List<Permission> children;
}
