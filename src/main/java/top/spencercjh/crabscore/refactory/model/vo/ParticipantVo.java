package top.spencercjh.crabscore.refactory.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.Participant;

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
public class ParticipantVo extends Participant {
    private Company company;

    public ParticipantVo(@NotNull Participant participant, Company company) {
        super(participant.getId(), participant.getCompetitionId(), participant.getCompanyId(), participant.getRoleId(), participant.getUsername(),
                participant.getPassword(), participant.getDisplayName(), participant.getStatus(), participant.getEmail());
        this.setUpdateUser(participant.getUpdateUser())
                .setAvatarUrl(participant.getAvatarUrl())
                .setCreateDate(participant.getCreateDate())
                .setCreateUser(participant.getCreateUser())
                .setVersion(participant.getVersion());
        this.company = company;
    }
}
