package top.spencercjh.crabscore.refactory.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;

/**
 * @author Spencer
 * @date 2020/1/30
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CrabVo")
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CrabVo extends Crab {
    private ScoreTaste scoreTaste;
    private ScoreQuality scoreQuality;

    public CrabVo(@NotNull Crab crab, ScoreTaste scoreTaste, ScoreQuality scoreQuality) {
        super(crab.getId(), crab.getCompetitionId(), crab.getGroupId(), crab.getCrabSex(), crab.getCrabLabel(),
                crab.getCrabWeight(), crab.getCrabLength(), crab.getCrabFatness());
        this.setUpdateUser(crab.getUpdateUser())
                .setAvatarUrl(crab.getAvatarUrl())
                .setCreateDate(crab.getCreateDate())
                .setCreateUser(crab.getCreateUser())
                .setVersion(crab.getVersion());
        this.scoreTaste = scoreTaste;
        this.scoreQuality = scoreQuality;
    }
}
