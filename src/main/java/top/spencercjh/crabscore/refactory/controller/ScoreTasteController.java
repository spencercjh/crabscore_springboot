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
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * The type Score taste controller.
 *
 * @author Spencer
 * @date 2020 /1/30
 */
@RestController
@RequestMapping("/api/tasteScores")
@Validated
@Slf4j
public class ScoreTasteController {
    private final ScoreTasteService scoreTasteService;

    /**
     * Instantiates a new Score taste controller.
     *
     * @param scoreTasteService the score taste service;
     */
    public ScoreTasteController(ScoreTasteService scoreTasteService) {
        this.scoreTasteService = scoreTasteService;
    }

    /**
     * Gets detail.
     *
     * @param id the id;
     * @return the detail;
     */
    @PreAuthorize("hasAnyAuthority('admin','judege','staff')")
    @GetMapping("/{id}")
    public ResponseEntity<Result<ScoreTaste>> getDetail(@PathVariable @Positive Integer id) {
        final ScoreTaste scoreTaste = scoreTasteService.getById(id);
        return scoreTaste == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(scoreTaste);
    }

    /**
     * Update score taste.
     *
     * @param toUpdate the to update;
     * @return the response entity;
     */
    @PreAuthorize("hasAnyAuthority('admin','judge')")
    @PutMapping
    public ResponseEntity<Result<ScoreTaste>> updateScoreTaste(@RequestBody @NotNull @javax.validation.constraints.NotNull
                                                               @Valid ScoreTaste toUpdate) {
        if (toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid competition",
                    HttpStatus.BAD_REQUEST);
        }
        final Authentication authentication = AuthUtils.getAuthentication();
        assert authentication != null;
        return scoreTasteService.updateById(toUpdate
                .setUpdateUser(authentication.getName())
                .setJudgeUsername(authentication.getName())) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
