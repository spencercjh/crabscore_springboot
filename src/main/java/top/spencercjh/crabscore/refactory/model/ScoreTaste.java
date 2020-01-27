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
@ApiModel(value = "ScoreTaste")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_score_taste")
public class ScoreTaste {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_CRAB_SEX = "crab_sex";
    public static final String COL_JUDGES_ID = "judges_id";
    public static final String COL_SCORE_FIN = "score_fin";
    public static final String COL_SCORE_YGYS = "score_ygys";
    public static final String COL_SCORE_SYS = "score_sys";
    public static final String COL_SCORE_GHYS = "score_ghys";
    public static final String COL_SCORE_XWXW = "score_xwxw";
    public static final String COL_SCORE_GH = "score_gh";
    public static final String COL_SCORE_FBJR = "score_fbjr";
    public static final String COL_SCORE_BZJR = "score_bzjr";
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
    /**
     * 0:雌 1：雄
     */
    @TableField(value = "crab_sex")
    @ApiModelProperty(value = " 0:雌 1：雄")
    private Byte crabSex;
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
     * 蟹盖颜色
     */
    @TableField(value = "score_ygys")
    @ApiModelProperty(value = "蟹盖颜色")
    private BigDecimal scoreYgys;
    /**
     * 鳃颜色
     */
    @TableField(value = "score_sys")
    @ApiModelProperty(value = "鳃颜色")
    private BigDecimal scoreSys;
    /**
     * 膏、黄颜色
     */
    @TableField(value = "score_ghys")
    @ApiModelProperty(value = "膏、黄颜色")
    private BigDecimal scoreGhys;
    /**
     * 腥味、香味
     */
    @TableField(value = "score_xwxw")
    @ApiModelProperty(value = "腥味、香味")
    private BigDecimal scoreXwxw;
    /**
     * 膏、黄
     */
    @TableField(value = "score_gh")
    @ApiModelProperty(value = "膏、黄")
    private BigDecimal scoreGh;
    /**
     * 腹部肌肉
     */
    @TableField(value = "score_fbjr")
    @ApiModelProperty(value = "腹部肌肉")
    private BigDecimal scoreFbjr;
    /**
     * 第二、三步足肌肉
     */
    @TableField(value = "score_bzjr")
    @ApiModelProperty(value = "第二、三步足肌肉")
    private BigDecimal scoreBzjr;
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