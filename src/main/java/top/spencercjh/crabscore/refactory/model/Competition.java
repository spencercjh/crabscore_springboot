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
@ApiModel(value = "Competition")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_competition")
public class Competition {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_YEAR = "competition_year";
    public static final String COL_VAR_FATNESS_M = "var_fatness_m";
    public static final String COL_VAR_FATNESS_F = "var_fatness_f";
    public static final String COL_VAR_WEIGHT_M = "var_weight_m";
    public static final String COL_VAR_WEIGHT_F = "var_weight_f";
    public static final String COL_VAR_MFATNESS_SD = "var_mfatness_sd";
    public static final String COL_VAR_MWEIGHT_SD = "var_mweight_sd";
    public static final String COL_VAR_FFATNESS_SD = "var_ffatness_sd";
    public static final String COL_VAR_FWEIGHT_SD = "var_fweight_sd";
    public static final String COL_RESULT_FATNESS = "result_fatness";
    public static final String COL_RESULT_QUALITY = "result_quality";
    public static final String COL_RESULT_TASTE = "result_taste";
    public static final String COL_NOTE = "note";
    public static final String COL_STATUS = "status";
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
     * 赛事的年份
     */
    @TableField(value = "competition_year")
    @ApiModelProperty(value = "赛事的年份")
    private String competitionYear;
    /**
     * 雄蟹肥满度参数
     */
    @TableField(value = "var_fatness_m")
    @ApiModelProperty(value = "雄蟹肥满度参数")
    private BigDecimal varFatnessM;
    /**
     * 雌蟹肥满度参数
     */
    @TableField(value = "var_fatness_f")
    @ApiModelProperty(value = "雌蟹肥满度参数")
    private BigDecimal varFatnessF;
    /**
     * 雄蟹体重参数
     */
    @TableField(value = "var_weight_m")
    @ApiModelProperty(value = "雄蟹体重参数")
    private BigDecimal varWeightM;
    /**
     * 雌蟹体重参数
     */
    @TableField(value = "var_weight_f")
    @ApiModelProperty(value = "雌蟹体重参数")
    private BigDecimal varWeightF;
    /**
     * 雄蟹肥满度标准差参数
     */
    @TableField(value = "var_mfatness_sd")
    @ApiModelProperty(value = "雄蟹肥满度标准差参数")
    private BigDecimal varMfatnessSd;
    /**
     * 雄蟹体重参数
     */
    @TableField(value = "var_mweight_sd")
    @ApiModelProperty(value = "雄蟹体重参数")
    private BigDecimal varMweightSd;
    /**
     * 雌蟹肥满度标准差参数
     */
    @TableField(value = "var_ffatness_sd")
    @ApiModelProperty(value = "雌蟹肥满度标准差参数")
    private BigDecimal varFfatnessSd;
    /**
     * 雌蟹体重标准差参数
     */
    @TableField(value = "var_fweight_sd")
    @ApiModelProperty(value = "雌蟹体重标准差参数")
    private BigDecimal varFweightSd;
    /**
     * 1:允许查看排名,0不允许查看排名
     */
    @TableField(value = "result_fatness")
    @ApiModelProperty(value = "1:允许查看排名,0不允许查看排名")
    private Byte resultFatness;
    /**
     * 1:允许查看排名,0不允许查看排名
     */
    @TableField(value = "result_quality")
    @ApiModelProperty(value = "1:允许查看排名,0不允许查看排名")
    private Byte resultQuality;
    /**
     * 1:允许查看排名,0不允许查看排名
     */
    @TableField(value = "result_taste")
    @ApiModelProperty(value = "1:允许查看排名,0不允许查看排名")
    private Byte resultTaste;
    /**
     * 注备
     */
    @TableField(value = "note")
    @ApiModelProperty(value = "注备")
    private String note;
    /**
     * 1：可用 0：禁用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "1：可用 0：禁用")
    private Byte status;
    /**
     * 大赛默认图标URL
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "大赛默认图标URL")
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