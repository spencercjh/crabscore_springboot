package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@ApiModel(value = "UserInfo")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_user_info")
public class UserInfo {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_ROLE_ID = "role_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_PASSWORD = "password";
    public static final String COL_DISPLAY_NAME = "display_name";
    public static final String COL_STATUS = "status";
    public static final String COL_EMAIL = "email";
    public static final String COL_AVATAR_URL = "avatar_url";
    public static final String COL_VERSION = "version";
    public static final String COL_CREATE_DATE = "create_date";
    public static final String COL_CREATE_USER = "create_user";
    public static final String COL_UPDATE_DATE = "update_date";
    public static final String COL_UPDATE_USER = "update_user";
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 比赛ID
     */
    @TableField(value = "competition_id")
    @ApiModelProperty(value = "比赛ID")
    private Integer competitionId;
    /**
     * 对应的参选单位
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "对应的参选单位")
    private Integer companyId;
    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;
    /**
     * 用户名,登录名
     */
    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户名,登录名")
    private String userName;
    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 显示名称，姓名或单位名
     */
    @TableField(value = "display_name")
    @ApiModelProperty(value = "显示名称，姓名或单位名")
    private String displayName;
    /**
     * 用户状态 1：可用 0：禁用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "用户状态 1：可用 0：禁用")
    private Byte status;
    @TableField(value = "email")
    @ApiModelProperty(value = "")
    private String email;
    /**
     * 小组图标URL
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "小组图标URL")
    private String avatarUrl;
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