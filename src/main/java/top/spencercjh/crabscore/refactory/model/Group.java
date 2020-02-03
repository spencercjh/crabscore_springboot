package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author MyBatisCodeHelperPro
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Group")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_group")
public class Group extends BaseModel {
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
}