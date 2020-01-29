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

/**
 * @author MyBatisCodeHelperPro
 */
@ApiModel(value = "ScoreQuality")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_score_quality")
public class ScoreQuality {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_CRAB_ID = "crab_id";
    public static final String COL_JUDGES_ID = "judges_id";
    public static final String COL_SCORE_FIN = "score_fin";
    public static final String COL_SCORE_BTS = "score_bts";
    public static final String COL_SCORE_FTS = "score_fts";
    public static final String COL_SCORE_EC = "score_ec";
    public static final String COL_SCORE_DSCC = "score_dscc";
    public static final String COL_SCORE_BBYZT = "score_bbyzt";
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
    @TableField(value = "group_id")
    @ApiModelProperty(value = "")
    private Integer groupId;
    @TableField(value = "crab_id")
    @ApiModelProperty(value = "对应的螃蟹id")
    private Integer crabId;
    @TableField(value = "judges_id")
    @ApiModelProperty(value = "")
    private Integer judgesId;
    /**
     * 最终给分
     */
    @TableField(value = "score_fin")
    @ApiModelProperty(value = "最终给分")
    private BigDecimal scoreFin;
    /**
     * 体色(背)
     */
    @TableField(value = "score_bts")
    @ApiModelProperty(value = "体色(背)")
    private BigDecimal scoreBts;
    /**
     * 体色(腹)
     */
    @TableField(value = "score_fts")
    @ApiModelProperty(value = "体色(腹)")
    private BigDecimal scoreFts;
    /**
     * 额齿
     */
    @TableField(value = "score_ec")
    @ApiModelProperty(value = "额齿")
    private BigDecimal scoreEc;
    /**
     * 第4侧齿
     */
    @TableField(value = "score_dscc")
    @ApiModelProperty(value = "第4侧齿")
    private BigDecimal scoreDscc;
    /**
     * 背部疣状突
     */
    @TableField(value = "score_bbyzt")
    @ApiModelProperty(value = "背部疣状突")
    private BigDecimal scoreBbyzt;
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