package top.spencercjh.crabscore.refactory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.CompetitionService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * The type Competition controller.
 *
 * @author Spencer
 */
@RestController
@RequestMapping("/api/competitions")
@Validated
@Slf4j
public class CompetitionController {
    private final CompetitionService competitionService;

    /**
     * Instantiates a new Competition controller.
     *
     * @param competitionService the competition service;
     */
    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PreAuthorize("hasAnyAuthority('admin','staff','judge','company')")
    @GetMapping("/current")
    public ResponseEntity<Result<List<Competition>>> getCurrent() {
        final List<Competition> competitionList = competitionService.list(new QueryWrapper<Competition>().eq(Competition.COL_STATUS, 1));
        return competitionList.isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(competitionList);
    }

    /**
     * Gets detail.
     *
     * @param id the id;
     * @return the detail;
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Result<Competition>> getDetail(@PathVariable @Positive Integer id) {
        final Competition competition = competitionService.getById(id);
        return competition == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(competition);
    }

    /**
     * List search response entity.
     *
     * @param year   the year;
     * @param status the status;
     * @param page   the page;
     * @param size   the size;
     * @return the response entity;
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping
    public ResponseEntity<Result<IPage<Competition>>> listSearch(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) @Positive Byte status,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false, defaultValue = "15") @Positive Long size) {
        final IPage<Competition> pageResult = competitionService.pageQuery(year, status, page, size);
        return pageResult.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageResult);
    }

    /**
     * Update competition response entity.
     *
     * @param image           the image;
     * @param competitionJson the competition json;
     * @return the response entity;
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping
    public ResponseEntity<Result<Competition>> updateCompetition(
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(name = "competition") @NotEmpty String competitionJson) {
        final Competition toUpdate = JacksonUtil.deserialize(competitionJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid competition",
                    HttpStatus.BAD_REQUEST);
        }
        return competitionService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete competition response entity.
     *
     * @param id the id;
     * @return the response entity;
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Object>> deleteCompetition(@PathVariable @Positive Integer id) {
        return competitionService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Insert competition response entity.
     *
     * @param image           the image;
     * @param competitionJson the competition json;
     * @return the response entity;
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping
    public ResponseEntity<Result<Competition>> insertCompetition(@RequestParam(required = false) MultipartFile image,
                                                                 @RequestParam(name = "competition") @NotEmpty String competitionJson) {
        final Competition toInsert = JacksonUtil.deserialize(competitionJson, new TypeReference<>() {
        });
        if (toInsert == null || StringUtils.isBlank(toInsert.getCompetitionYear())) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return competitionService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(toInsert, HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
