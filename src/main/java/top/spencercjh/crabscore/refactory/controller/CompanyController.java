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
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * The type Company controller.
 */
@RestController
@RequestMapping("/companies")
@Validated
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    /**
     * Instantiates a new Company controller.
     *
     * @param companyService the company service
     */
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Gets detail.
     *
     * @param id the id
     * @return the detail
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<Company>> getDetail(@PathVariable @Positive Integer id) {
        final Company detail = companyService.getById(id);
        return detail == null ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(detail);
    }

    /**
     * List search response entity.
     *
     * @param size          the size
     * @param page          the page
     * @param companyName   the company name
     * @param competitionId the competition id
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<Result<IPage<Company>>> listSearch(
            @RequestParam(required = false, defaultValue = "15") @Positive Long size,
            @RequestParam(required = false, defaultValue = "1") @Positive Long page,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) @Positive Integer competitionId) {
        final IPage<Company> pageResult = companyService.pageQuery(companyName, competitionId, page, size);
        return pageResult.getRecords().isEmpty() ?
                ResponseEntityUtil.fail(HttpStatus.NOT_FOUND) :
                ResponseEntityUtil.success(pageResult);
    }

    /**
     * Update company info response entity.
     *
     * @param image       the image
     * @param companyJson the company json
     * @return the response entity
     */
    @PutMapping
    public ResponseEntity<Result<Company>> updateCompanyInfo(@RequestParam(required = false) MultipartFile image,
                                                             @RequestParam(name = "company") @NotEmpty String companyJson) {
        final Company toUpdate = JacksonUtil.deserialize(companyJson, new TypeReference<>() {
        });
        if (toUpdate == null || toUpdate.getId() == null) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return companyService.commitAndUpdate(toUpdate, image) ?
                ResponseEntityUtil.success(toUpdate) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete company info response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Object>> deleteCompanyInfo(@PathVariable @Positive Integer id) {
        return companyService.removeById(id) ?
                ResponseEntityUtil.success() :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Insert company info response entity.
     *
     * @param image       the image
     * @param companyJson the company json
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Result<Company>> insertCompanyInfo(@RequestParam(required = false) MultipartFile image,
                                                             @RequestParam(name = "company") @NotEmpty String companyJson) {
        final Company toInsert = JacksonUtil.deserialize(companyJson, new TypeReference<>() {
        });
        if (toInsert == null || StringUtils.isBlank(toInsert.getCompanyName())) {
            return ResponseEntityUtil.fail(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE,
                    "invalid companyInfo",
                    HttpStatus.BAD_REQUEST);
        }
        return companyService.commitAndInsert(toInsert, image) ?
                ResponseEntityUtil.success(toInsert, HttpStatus.CREATED) :
                ResponseEntityUtil.fail(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
