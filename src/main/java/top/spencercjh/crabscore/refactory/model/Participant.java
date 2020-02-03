package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author MyBatisCodeHelperPro
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Participant")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_participant")
public class Participant extends BaseModel {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_ROLE_ID = "role_id";
    public static final String COL_USERNAME = "username";
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
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名,登录名")
    private String username;
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
}