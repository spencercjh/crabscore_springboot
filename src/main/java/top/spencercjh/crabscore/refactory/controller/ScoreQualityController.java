package top.spencercjh.crabscore.refactory.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.spencercjh.crabscore.refactory.config.security.AuthUtils;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * @author Spencer
 * @date 2020/1/30
 */
@RestController
@RequestMapping("/api/qualityScores")
@Validated
@Slf4j
public class ScoreQualityController {
    private final ScoreQualityService scoreQualityService;

    public ScoreQualityController(ScoreQualityService scoreQualityService) {
        this.scoreQualityService = scoreQualityService;
    }

    @PreAuthorize("hasAnyAuthority('admin','judege','staff')")
    @GetMapping("/{id}")
    public ResponseEntity<Result<ScoreQuality>> getDetail(@PathVariable @Positive Integer id) {
        final ScoreQuality scoreQuality = scoreQualityService.getById(id);
        return scoreQuality == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(scoreQuality);
    }

    @PreAuthorize("hasAnyAuthority('admin','judge')")
    @PutMapping
    public ResponseEntity<Result<ScoreQuality>> updateScoreQuality(@RequestBody @NotNull @javax.validation.constraints.NotNull
                                                                   @Valid ScoreQuality toUpdate) {
        if (toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid competition",
                    HttpStatus.BAD_REQUEST);
        }
        final Authentication authentication = AuthUtils.getAuthentication();
        assert authentication != null;
        return scoreQualityService.updateById(toUpdate
                .setUpdateUser(authentication.getName())
                .setJudgeUsername(authentication.getName())) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
