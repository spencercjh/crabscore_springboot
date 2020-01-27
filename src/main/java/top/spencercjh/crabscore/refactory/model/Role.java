package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author MyBatisCodeHelperPro
 */
@ApiModel(value = "Role")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_role")
public class Role {
    public static final String COL_ID = "id";
    public static final String COL_ROLE_NAME = "role_name";
    public static final String COL_VERSION = "version";
    public static final String COL_CREATE_DATE = "create_date";
    public static final String COL_CREATE_USER = "create_user";
    public static final String COL_UPDATE_DATE = "update_date";
    public static final String COL_UPDATE_USER = "update_user";
    /**
     * "角色ID:1：管理员
     * 2：评委 3：工作人员 4:参选单位"
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "角色ID:1：管理员, 2：评委 3：工作人员 4:参选单位")
    private Integer id;
    @TableField(value = "role_name")
    @ApiModelProperty(value = "")
    private String roleName;
    @Version
    @TableField(value = "version")
    @ApiModelProperty(value = "", example = "2020-01-01 00:00:00")
    private Date version;
    /**
     * 数据创建日期
     */
    @TableField(value = "create_date")
    @ApiModelProperty(value = "数据创建日期", example = "2020-01-01 00:00:00")
    private Date createDate;
    /**
     * 数据创建用户
     */
    @TableField(value = "create_user")
    @ApiModelProperty(value = "数据创建用户")
    private Integer createUser;
    /**
     * 数据更新用户
     */
    @TableField(value = "update_user")
    @ApiModelProperty(value = "数据更新用户")
    private Integer updateUser;
}