package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "GroupInfo")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_group_info")
public class GroupInfo {
    public static final String COL_ID = "id";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_FATNESS_SCORE_M = "fatness_score_m";
    public static final String COL_QUALITY_SCORE_M = "quality_score_m";
    public static final String COL_TASTE_SCORE_M = "taste_score_m";
    public static final String COL_FATNESS_SCORE_F = "fatness_score_f";
    public static final String COL_QUALITY_SCORE_F = "quality_score_f";
    public static final String COL_TASTE_SCORE_F = "taste_score_f";
    public static final String COL_AVATAR_URL = "avatar_url";
    public static final String COL_VERSION = "version";
    public static final String COL_CREATE_DATE = "create_date";
    public static final String COL_CREATE_USER = "create_user";
    public static final String COL_UPDATE_DATE = "update_date";
    public static final String COL_UPDATE_USER = "update_user";
    /**
     * 组ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "组ID")
    private Integer id;
    /**
     * 参赛单位id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "参赛单位id")
    private Integer companyId;
    /**
     * 比赛ID
     */
    @TableField(value = "competition_id")
    @ApiModelProperty(value = "比赛ID")
    private Integer competitionId;
    /**
     * 雄蟹肥满度评分
     */
    @TableField(value = "fatness_score_m")
    @ApiModelProperty(value = "雄蟹肥满度评分")
    private BigDecimal fatnessScoreM;
    /**
     * 雄蟹种质评分
     */
    @TableField(value = "quality_score_m")
    @ApiModelProperty(value = "雄蟹种质评分")
    private BigDecimal qualityScoreM;
    /**
     * 雄蟹口感评分
     */
    @TableField(value = "taste_score_m")
    @ApiModelProperty(value = "雄蟹口感评分")
    private BigDecimal tasteScoreM;
    /**
     * 雌蟹肥满度评分
     */
    @TableField(value = "fatness_score_f")
    @ApiModelProperty(value = "雌蟹肥满度评分")
    private BigDecimal fatnessScoreF;
    /**
     * 雌蟹种质评分
     */
    @TableField(value = "quality_score_f")
    @ApiModelProperty(value = "雌蟹种质评分")
    private BigDecimal qualityScoreF;
    /**
     * 雌蟹口感评分
     */
    @TableField(value = "taste_score_f")
    @ApiModelProperty(value = "雌蟹口感评分")
    private BigDecimal tasteScoreF;
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