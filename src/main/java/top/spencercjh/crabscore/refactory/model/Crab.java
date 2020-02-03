package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;

import java.math.BigDecimal;

/**
 * @author MyBatisCodeHelperPro
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Crab")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rxpb.rxpb_crab")
public class Crab extends BaseModel implements Cloneable {
    public static final String COL_ID = "id";
    public static final String COL_COMPETITION_ID = "competition_id";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_CRAB_SEX = "crab_sex";
    public static final String COL_CRAB_LABEL = "crab_label";
    public static final String COL_CRAB_WEIGHT = "crab_weight";
    public static final String COL_CRAB_LENGTH = "crab_length";
    public static final String COL_CRAB_FATNESS = "crab_fatness";
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
    @TableField(value = "group_id")
    @ApiModelProperty(value = "")
    private Integer groupId;
    /**
     * 0:雌 1：雄
     */
    @TableField(value = "crab_sex")
    @ApiModelProperty(value = "0:雌 1：雄")
    private SexEnum crabSex;
    /**
     * 四位的蟹标识
     */
    @TableField(value = "crab_label")
    @ApiModelProperty(value = "四位的蟹标识")
    private String crabLabel;
    /**
     * 体重
     */
    @TableField(value = "crab_weight")
    @ApiModelProperty(value = "体重")
    private BigDecimal crabWeight;
    /**
     * 壳长
     */
    @TableField(value = "crab_length")
    @ApiModelProperty(value = "壳长")
    private BigDecimal crabLength;
    /**
     * 肥满度
     */
    @TableField(value = "crab_fatness")
    @ApiModelProperty(value = "肥满度")
    private BigDecimal crabFatness;

    @Override
    public Crab clone() {
        try {
            return (Crab) super.clone();
        } catch (CloneNotSupportedException ignore) {
            ignore.printStackTrace();
            return this;
        }
    }
}