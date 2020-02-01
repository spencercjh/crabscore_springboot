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
import top.spencercjh.crabscore.refactory.MockDataManager;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.model.vo.UserVo;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/2/1
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class UserServiceImplTest {
    public static final int MOCK_DATA_COMPETITION = 1;
    public static final int COMPANY_ROLE_ID = 4;
    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    //    @Test
    void mockData() {
        // mock company users data
        companyService.list(new QueryWrapper<Company>().eq(Company.COL_COMPETITION_ID, MOCK_DATA_COMPETITION))
                .forEach(company -> assertTrue(userService.save(new User().setCompanyId(company.getId())
                        .setPassword("123456")
                        .setEmail(MockDataManager.getRandomEmail())
                        .setRoleId(COMPANY_ROLE_ID)
                        .setCompetitionId(MOCK_DATA_COMPETITION)
                        .setDisplayName(MockDataManager.getRandomDisplayName())
                        .setUserName(MockDataManager.getRandomUsername()))));
        // mock other user data
        IntStream.range(1, 4).mapToObj(roleId -> new User()
                .setRoleId(roleId)
                .setPassword("123456")
                .setEmail(MockDataManager.getRandomEmail())
                .setCompetitionId(MOCK_DATA_COMPETITION)
                .setDisplayName(MockDataManager.getRandomDisplayName())
                .setUserName(MockDataManager.getRandomUsername()))
                .forEach(user -> assertTrue(userService.save(user)));
    }

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 111;
        assertTrue(userService.removeById(toDelete));
        assertFalse(userService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page search
        final long page = 2;
        final long size = 15;
        final IPage<UserVo> userVoIPage = userService.pageQuery(null, null, null,
                null, null, page, size);
        assertEquals(page, userVoIPage.getCurrent());
        assertEquals(size, userVoIPage.getSize());
        assertEquals(userService.count(), userVoIPage.getTotal());
        // companyId search
        final int companyId = 1;
        IPage<UserVo> searchResult = userService.pageQuery(companyId, null, null, null,
                null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(companyId, userVo.getCompanyId());
            assertEquals(companyId, userVo.getCompany().getId());
        });
        // competitionId search
        final int competitionId = 1;
        searchResult = userService.pageQuery(null, competitionId, null, null,
                null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(competitionId, userVo.getCompetitionId());
        });
        // roleId search
        searchResult = userService.pageQuery(null, null, COMPANY_ROLE_ID, null, null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(COMPANY_ROLE_ID, userVo.getRoleId());
        });
        // username search
        final String username = "删除测试";
        searchResult = userService.pageQuery(null, null, null, username, null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(username, userVo.getUserName());
        });
        // display name search
        final String displayName = "删除测试显示名";
        searchResult = userService.pageQuery(null, null, null, null, displayName, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(displayName, userVo.getDisplayName());
        });
    }

    @Transactional
    @Test
    void commitAndUpdate() throws IOException {
        final int toUpdateId = 112;
        final int toUpdateCompetitionId = 0;
        final User toUpdate = new User().setId(toUpdateId)
                .setCompetitionId(toUpdateCompetitionId)
                .setCompanyId(toUpdateCompetitionId)
                .setUserName("commitAndUpdateUser");
        assertTrue(userService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final User actual = userService.getById(toUpdateId);
        assertEquals(toUpdateCompetitionId, toUpdate.getCompanyId());
        assertEquals(toUpdateCompetitionId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(userService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException {
        final int toInsertCompanyId = 0;
        final User toInsert = new User()
                .setCompanyId(toInsertCompanyId)
                .setCompetitionId(toInsertCompanyId)
                .setRoleId(0)
                .setUserName("CommitAndInsertUser")
                .setPassword("CommitAndInsertUser");
        assertTrue(userService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final User actual = userService.getById(toInsert.getId());
        assertEquals(toInsertCompanyId, toInsert.getCompanyId());
        assertEquals(toInsertCompanyId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(userService.commitAndInsert(new User()
                .setCompetitionId(999)
                .setCompanyId(999)
                .setRoleId(0)
                .setUserName("CommitAndInsertUser")
                .setPassword("CommitAndInsertUser"), null));
    }
}