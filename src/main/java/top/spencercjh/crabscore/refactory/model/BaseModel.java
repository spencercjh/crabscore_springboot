package top.spencercjh.crabscore.refactory.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Spencer
 * @date 2020/2/3
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseModel {
    /**
     * 小组图标URL
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "图标URL")
    private String avatarUrl;
    @Version
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "version")
    @ApiModelProperty(value = "", example = "2020-01-01 00:00:00")
    private Date version;
    /**
     * 数据创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
