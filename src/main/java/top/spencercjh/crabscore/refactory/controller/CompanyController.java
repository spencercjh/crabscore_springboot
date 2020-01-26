package top.spencercjh.crabscore.refactory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.CompanyInfo;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.CompanyInfoService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@RestController
@RequestMapping("/companies")
@Validated
@Slf4j
public class CompanyController {
    private final CompanyInfoService companyInfoService;

    public CompanyController(CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;
    }

    /**
     * 详情
     *
     * @param id id
     * @return detail
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<CompanyInfo>> detail(@PathVariable @Positive Integer id) {
        final CompanyInfo detail = companyInfoService.getById(id);
        return detail == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(detail);
    }

    /**
     * 列表
     *
     * @param size          size;
     * @param page          page;
     * @param companyName   company name;
     * @param competitionId competition id;
     * @return list
     */
    @GetMapping
    public ResponseEntity<Result<IPage<CompanyInfo>>> listSearch(
            @RequestParam(required = false, defaultValue = "15") @Positive Long size,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) @Positive Integer competitionId) {
        final IPage<CompanyInfo> pageResult = companyInfoService.pageQuery(companyName, competitionId, page, size);
        return pageResult.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageResult);
    }

    /**
     * 修改
     *
     * @param image       image;
     * @param companyJson toUpdate;
     * @return updated
     */
    @PutMapping
    public ResponseEntity<Result<CompanyInfo>> updateCompanyInfo(@RequestParam(required = false) MultipartFile image,
                                                                 @RequestParam(name = "company") @NotEmpty String companyJson) {
        final CompanyInfo toUpdate = JacksonUtil.deserialize(companyJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return companyInfoService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Object>> deleteCompanyInfo(@PathVariable @Positive Integer id) {
        return companyInfoService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 新增
     *
     * @param image       image;
     * @param companyJson toInsert;
     * @return updated
     */
    @PostMapping
    public ResponseEntity<Result<CompanyInfo>> insertCompanyInfo(@RequestParam(required = false) MultipartFile image,
                                                                 @RequestParam(name = "company") @NotEmpty String companyJson) {
        final CompanyInfo toInsert = JacksonUtil.deserialize(companyJson, new TypeReference<>() {
        });
        if (toInsert == null || StringUtils.isBlank(toInsert.getCompanyName())) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return companyInfoService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}