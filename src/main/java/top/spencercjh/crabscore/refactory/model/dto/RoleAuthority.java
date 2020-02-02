package top.spencercjh.crabscore.refactory.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Spencer
 * @date 2020/2/3
 */
@ApiModel(value = "RoleAuthority")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthority implements GrantedAuthority {
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
