package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author MyBatisCodeHelperPro
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Company")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_company")
public class Company extends BaseModel {
    public static final String COL_ID = "id";
    public static final String COL_COMPANY_NAME = "company_name";
    public static final String COL_COMPETITION_ID = "competition_id";
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
     * 参选单位名: UNIQUE
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "参选单位名: UNIQUE")
    @NotEmpty
    private String companyName;
    /**
     * 参赛企业所属的比赛,同一企业在不同的赛事中保持独立
     */
    @TableField(value = "competition_id")
    @ApiModelProperty(value = "参赛企业所属的比赛,同一企业在不同的赛事中保持独立")
    @NotNull
    private Integer competitionId;
    /**
     * 参选单位头像URL
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "参选单位头像URL")
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
    private String createUser;
    /**
     * 数据更新用户
     */
    @TableField(value = "update_user")
    @ApiModelProperty(value = "数据更新用户")
    private String updateUser;
}