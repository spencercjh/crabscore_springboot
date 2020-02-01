package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.service.CompanyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyServiceImplTest {
    @Autowired
    private CompanyService companyService;

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 53;
        assertTrue(companyService.removeById(toDelete));
        assertFalse(companyService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page
        final long page = 2;
        final long size = 30;
        final IPage<Company> pageResult = companyService.pageQuery(null, null, page, size);
        pageResult.getRecords().forEach(companyInfo -> {
            assertNotNull(companyInfo);
            log.debug(companyInfo.toString());
            assertTrue(companyInfo.getId() > size);
        });
        // name search
        final String expectName = "删除测试";
        final IPage<Company> oneResult = companyService.pageQuery(expectName, null, 1L, 100L);
        assertEquals(1, oneResult.getRecords().size());
        assertEquals(expectName, oneResult.getRecords().get(0).getCompanyName());
        // competitionId search
        final int competitionId = 99;
        final IPage<Company> searchResult = companyService.pageQuery(null, competitionId, 1L, 100L);
        assertEquals(2, searchResult.getRecords().size());
        assertEquals(competitionId, searchResult.getRecords().get(0).getCompetitionId());
    }

    @Transactional
    @Test
    void commitAndUpdate() throws IOException {
        final int toUpdateId = 54;
        final String updatedName = "Update Test";
        final Company toUpdate = new Company().setId(toUpdateId).setCompanyName(updatedName);
        assertTrue(companyService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Company actual = companyService.getById(toUpdateId);
        assertEquals(updatedName, toUpdate.getCompanyName());
        assertEquals(updatedName, actual.getCompanyName());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(companyService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException {
        final String insertName = "Insert Test";
        final Company toInsert = new Company().setCompanyName(insertName);
        assertTrue(companyService.commitAndInsert(toInsert, new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Company actual = companyService.getOne(
                new QueryWrapper<Company>().eq(Company.COL_COMPANY_NAME, insertName));
        assertEquals(insertName, toInsert.getCompanyName());
        assertEquals(insertName, actual.getCompanyName());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(companyService.commitAndInsert(new Company().setCompanyName("NEW"), null));
    }
}