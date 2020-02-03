package top.spencercjh.crabscore.refactory.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @author Spencer
 * @date 2020/2/3
 */
@ApiModel(value = "LoginUser")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    @ApiModelProperty(example = "emeiqp")
    @NotEmpty
    private String username;
    @ApiModelProperty(example = "123456")
    @NotEmpty
    private String password;
}
