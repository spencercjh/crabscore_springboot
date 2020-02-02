package top.spencercjh.crabscore.refactory.controller;

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
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.model.vo.UserVo;
import top.spencercjh.crabscore.refactory.service.UserService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * @author Spencer
 * @date 2020/2/1
 */
@RestController
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Result<User>> getDetail(@PathVariable @Positive Integer id) {
        final User user = userService.getById(id);
        return user == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(user);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping
    public ResponseEntity<Result<IPage<UserVo>>> listSearch(
            @RequestParam(required = false) @Positive Integer companyId,
            @RequestParam(required = false) @Positive Integer competitionId,
            @RequestParam(required = false) @Positive Integer roleId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false, defaultValue = "15") @Positive Long size) {
        final IPage<UserVo> pageQuery = userService.pageQuery(companyId, competitionId, roleId, userName, displayName,
                page, size);
        return pageQuery.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageQuery);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping
    public ResponseEntity<Result<User>> updateUser(@RequestParam(required = false) MultipartFile image,
                                                   @RequestParam(name = "user") @NotEmpty String userJson) {
        final User toUpdate = JacksonUtil.deserialize(userJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid user",
                    HttpStatus.BAD_REQUEST);
        }
        return userService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<User>> deleteUser(@PathVariable @Positive Integer id) {
        return userService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping
    public ResponseEntity<Result<User>> insertUser(@RequestParam(required = false) MultipartFile image,
                                                   @RequestParam(name = "user") @NotEmpty String userJson) {
        final User toInsert = JacksonUtil.deserialize(userJson, new TypeReference<>() {
        });
        if (toInsert == null || toInsert.getCompanyId() == null || toInsert.getCompetitionId() == null ||
                toInsert.getRoleId() == null || StringUtils.isBlank(toInsert.getPassword()) ||
                StringUtils.isBlank(toInsert.getUserName())) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid group",
                    HttpStatus.BAD_REQUEST);
        }
        return userService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(toInsert, HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
