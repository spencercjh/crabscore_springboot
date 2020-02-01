package top.spencercjh.crabscore.refactory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.GroupService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * The type Group controller.
 *
 * @author Spencer
 * @date 2020 /1/28
 */
@RestController
@RequestMapping("/groups")
@Validated
@Slf4j
public class GroupController {
    private final GroupService groupService;

    /**
     * Instantiates a new Group controller.
     *
     * @param groupService the group service;
     */
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Gets detail.
     *
     * @param id the id;
     * @return the detail;
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<Group>> getDetail(@PathVariable @Positive Integer id) {
        final Group group = groupService.getById(id);
        return group == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(group);
    }

    /**
     * List search response entity.
     *
     * @param companyId     the company id;
     * @param competitionId the competition id;
     * @param page          the page;
     * @param size          the size;
     * @return the response entity;
     */
    @GetMapping
    public ResponseEntity<Result<IPage<Group>>> listSearch(
            @RequestParam(required = false) @Positive Integer companyId,
            @RequestParam(required = false) @Positive Integer competitionId,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false, defaultValue = "15") @Positive Long size
    ) {
        final IPage<Group> pageQuery = groupService.pageQuery(companyId, competitionId, page, size);
        return pageQuery.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageQuery);
    }

    /**
     * Update group response entity.
     *
     * @param image     the image;
     * @param groupJson the group json;
     * @return the response entity;
     */
    @PutMapping
    public ResponseEntity<Result<Group>> updateGroup(
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(name = "group") @NotEmpty String groupJson) {
        final Group toUpdate = JacksonUtil.deserialize(groupJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid group",
                    HttpStatus.BAD_REQUEST);
        }
        return groupService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete group response entity.
     *
     * @param id the id;
     * @return the response entity;
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Object>> deleteGroup(@PathVariable @Positive Integer id) {
        return groupService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Insert group response entity.
     *
     * @param image     the image;
     * @param groupJson the group json;
     * @return the response entity;
     */
    @PostMapping
    public ResponseEntity<Result<Group>> insertGroup(@RequestParam(required = false) MultipartFile image,
                                                     @RequestParam(name = "group") @NotEmpty String groupJson) {
        final Group toInsert = JacksonUtil.deserialize(groupJson, new TypeReference<>() {
        });
        if (toInsert == null || toInsert.getCompanyId() == null || toInsert.getCompetitionId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return groupService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(toInsert, HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
