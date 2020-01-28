package top.spencercjh.crabscore.refactory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Crab controller.
 *
 * @author Spencer
 * @date 2020 /1/27
 */
@RestController
@RequestMapping("/crabs")
@Validated
@Slf4j
public class CrabController {
    private final CrabService crabService;

    /**
     * Instantiates a new Crab controller.
     *
     * @param crabService the crab service;
     */
    public CrabController(CrabService crabService) {
        this.crabService = crabService;
    }

    /**
     * Gets detail.
     *
     * @param id the id;
     * @return the detail;
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<Crab>> getDetail(@PathVariable @Positive Integer id) {
        final Crab crab = crabService.getById(id);
        return crab == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(crab);
    }

    /**
     * List search response entity.
     *
     * @param competitionId the competition id;
     * @param groupId       the group id;
     * @param sex           the sex;
     * @param beginTime     the begin time;
     * @param endTime       the end time;
     * @param page          the page;
     * @param size          the size;
     * @return the response entity;
     */
    @GetMapping
    public ResponseEntity<Result<IPage<Crab>>> listSearch(
            @RequestParam(required = false) @Positive Integer competitionId,
            @RequestParam(required = false) @Positive Integer groupId,
            @RequestParam(required = false) SexEnum sex,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false, defaultValue = "15") @Positive Long size) {
        final IPage<Crab> pageResult = crabService.pageQuery(competitionId, groupId, sex, beginTime, endTime, page, size);
        return pageResult.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageResult);
    }

    /**
     * Commit and update response entity.
     *
     * @param image    the image;
     * @param crabJson the crab json;
     * @return the response entity;
     */
    @PutMapping
    public ResponseEntity<Result<Crab>> commitAndUpdate(
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(name = "crab") @NotEmpty String crabJson) {
        final Crab toUpdate = JacksonUtil.deserialize(crabJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null || toUpdate.getCompetitionId() == null ||
                toUpdate.getGroupId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid competition",
                    HttpStatus.BAD_REQUEST);
        }
        return crabService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Delete crab response entity.
     *
     * @param id the id;
     * @return the response entity;
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Object>> deleteCrab(@PathVariable @Positive Integer id) {
        return crabService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete crabs response entity.
     * <p>
     * Delete based on ids, groupId, or groupId and Sex.
     *
     * @param ids     the ids;
     * @param groupId the group id;
     * @param sex     the sex;
     * @return the response entity;
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteCrabs(@RequestParam(required = false) List<Integer> ids,
                                                      @RequestParam(required = false) Integer groupId,
                                                      @RequestParam(required = false) SexEnum sex) {
        // remove by ids first
        if (ids != null && !ids.isEmpty()) {
            return crabService.removeByIds(ids) ?
                    ResponseEntityUtil.success() :
                    ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (groupId != null) {
            // remove by group and sex
            if (sex != null) {
                return crabService.remove(new QueryWrapper<Crab>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_CRAB_SEX, sex)) ?
                        ResponseEntityUtil.success() :
                        ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // remove by group
            return crabService.remove(new QueryWrapper<Crab>().eq(Crab.COL_GROUP_ID, groupId)) ?
                    ResponseEntityUtil.success() :
                    ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // bad request
        return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                "invalid companyInfo",
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Insert crab response entity.
     * <p>
     * Insert one crab with an image (or without) or insert same crab without image in {@code repeat} times;
     *
     * @param image    the image;
     * @param crabJson the crab json;
     * @param repeat   the repeat;
     * @return the response entity;
     */
    @PostMapping
    public ResponseEntity<Result<Object>> insertCrab(
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(name = "crab") @NotEmpty String crabJson,
            @RequestParam(required = false) @Positive Integer repeat) {
        final Crab toInsert = JacksonUtil.deserialize(crabJson, new TypeReference<>() {
        });
        if (toInsert == null || toInsert.getGroupId() == null || toInsert.getCompetitionId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        // repeat inserting X times
        if (repeat != null) {
            List<Crab> toBatchInsert = new ArrayList<>(repeat + 1);
            for (int i = 0; i < repeat; i++) {
                toBatchInsert.add(toInsert);
            }
            return crabService.saveBatch(toBatchInsert) ?
                    ResponseEntityUtil.success(toBatchInsert, HttpStatus.CREATED) :
                    ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return crabService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(toInsert, HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
