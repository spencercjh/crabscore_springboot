package top.spencercjh.crabscore.refactory.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.User;

/**
 * @author Spencer
 * @date 2020/2/1
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserVo")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends User {
    private Company company;

    public UserVo(@NotNull User user, Company company) {
        super(user.getId(), user.getCompetitionId(), user.getCompanyId(), user.getRoleId(), user.getUserName(),
                user.getPassword(), user.getDisplayName(), user.getStatus(), user.getEmail(), user.getAvatarUrl(), user.getVersion(), user.getCreateDate(), user.getCreateUser(), user.getUpdateUser());
        this.company = company;
    }
}
