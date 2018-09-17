package com.shou.crabscore.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author spencercjh
 * rxpb_role_info
 */
@Data
@ApiModel(value="用户组对象模型")
public class Role {

    @ApiModelProperty(value = "唯一标识 用户组id")
    private Integer roleId;

    @ApiModelProperty(value = "用户组名称")
    private String roleName;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建用户")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新用户")
    private String updateUser;

}